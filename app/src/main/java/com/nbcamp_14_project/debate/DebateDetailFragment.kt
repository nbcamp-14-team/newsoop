package com.nbcamp_14_project.debate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.FragmentDebatedetailBinding

class DebateDetailFragment : Fragment() {
    private var _binding: FragmentDebatedetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DebateDetailListAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    //    private val debateList = ArrayList<DebateItem>()
    private val debatedetailList = ArrayList<DebateDetailItem>()
    private val viewModel: DebateViewModel by activityViewModels()
    private var agreeClicked = false
    private var oppositeClicked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDebatedetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val agreeImageView = binding.icAgree
        val tvAgree = binding.tvAgree


        checkAgreeVoteStatus()


        agreeImageView.setOnClickListener {
            if (oppositeClicked) {}else{
                if (agreeClicked) {
                    // 이미 클릭되었을 때
                    agreeImageView.setImageResource(R.drawable.ic_agreex)
                    tvAgree.text = (tvAgree.text.toString().toInt() - 1).toString()
                    removeAgreeVote() // 투표 제거
                } else {
                    // 클릭되지 않았을 때
                    agreeImageView.setImageResource(R.drawable.ic_agree)
                    tvAgree.text = (tvAgree.text.toString().toInt() + 1).toString()
                    addAgreeVote() // 투표 추가
                }
                agreeClicked = !agreeClicked // 상태를 토글
            }
        }

        updateAgreeCount()

        updateOppositeCount()


        val oppositeImageView = binding.icOpposite
        val tvOpposite = binding.tvOpposite

        checkOppositeVoteStatus()

        oppositeImageView.setOnClickListener {
            if (agreeClicked) {
            } else {
                if (oppositeClicked) {
                    oppositeImageView.setImageResource(R.drawable.ic_oppositex)
                    tvOpposite.text = (tvOpposite.text.toString().toInt() - 1).toString()
                    removeOppositeVote()
                } else {
                    oppositeImageView.setImageResource(R.drawable.ic_opposite)
                    tvOpposite.text = (tvOpposite.text.toString().toInt() + 1).toString()
                    addOppositeVote()
                }
                oppositeClicked = !oppositeClicked
            }
        }


        binding.imgBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val title = viewModel.title

        if (title != null) {
            binding.tvTitle.text = title
        }

        adapter = DebateDetailListAdapter(debatedetailList)
        binding.debatedetailList.layoutManager = LinearLayoutManager(context)
        binding.debatedetailList.adapter = adapter

        adapter.itemClick = object : DebateDetailListAdapter.ItemClick {

            //            override fun onDeleteCommentClick(position: Int) {
//                val commentToDelete = debatedetailList[position]
//                val debateId = viewModel.debateId // 토론 글 ID // 댓글의 고유 ID
//                val user = FirebaseAuth.getInstance().currentUser
//
//                if (debateId != null) {
//                    val firestore = FirebaseFirestore.getInstance()
//
//                    // 댓글 삭제 처리
//                    firestore.collection("Debates")
//                        .document(debateId)
//                        .collection("Comments")
//                        .document(commentToDelete.id) // 정확한 경로 지정
//                        .delete()
//                        .addOnSuccessListener {
//                            // Firestore에서 삭제 성공
//                            debatedetailList.removeAt(position)
//                            adapter.notifyItemRemoved(position)
//                        }
//                        .addOnFailureListener { e ->
//                            // 삭제 실패
//                            // 실패 시에 사용자에게 메시지를 표시할 수 있습니다.
//                        }
//                }
//            }
            override fun onDeleteCommentClick(position: Int) {
                val commentToDelete = debatedetailList[position]
                val debateId = viewModel.debateId // 논쟁 ID를 가져옵니다
                val user = FirebaseAuth.getInstance().currentUser

                if (user != null && debateId != null) {
                    val userUID = user.uid
                    val commentUserUID = commentToDelete.userUID

                    if (userUID == commentUserUID) {
                        // 본인이 작성한 댓글만 삭제 가능

                        // Firestore에서 댓글 문서에 올바른 경로를 구성합니다
                        val firestore = FirebaseFirestore.getInstance()
                        val commentRef = firestore.collection("User")
                            .document(userUID)
                            .collection("Debates")
                            .document(debateId)
                            .collection("Comments")
                            .document(commentToDelete.id) // 댓글 ID를 여기에 사용합니다

                        // 댓글 문서를 삭제합니다
                        commentRef.delete()
                            .addOnSuccessListener {
                                // Firestore에서 삭제가 성공적으로 수행됨
                                debatedetailList.removeAt(position)
                                adapter.notifyItemRemoved(position)
                            }
                            .addOnFailureListener { e ->
                                // 삭제가 실패한 경우
                                // 사용자에게 오류 메시지를 표시할 수 있습니다
                            }
                    } else {
                        // 본인이 작성한 댓글이 아닌 경우
                        Toast.makeText(requireContext(), "본인이 작성한 댓글만 삭제 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 사용자가 로그인하지 않은 경우를 처리합니다
                    Toast.makeText(requireContext(), "삭제 할 수 없습니다. 로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }



        }


        val debateId = viewModel.debateId
        Log.d("hyunsik", "debateId = $debateId")
        if (debateId != null) {
            loadComments()
            Log.d("hyunsik", "load = ${loadComments()}")
        }

        binding.searchBtn.setOnClickListener {
            val commentText = binding.searchInput.text.toString().trim()

            if (commentText.isNotEmpty()) {
                val user = auth.currentUser

                user?.let { currentUser ->
                    val userUID = currentUser.uid
                    val userUID2 = viewModel.userUID

                    if (debateId != null) {
                        val commentCollection = firestore.collection("User").document(userUID2.toString())
                            .collection("Debates").document(debateId).collection("Comments")

                        val newComment = DebateDetailItem(
                            text = commentText,
                            user = "정현식",
                            date = "날짜",
                            userUID = userUID
                        )
                        val newRef = commentCollection.document()
                        newComment.id = newRef.id


                        newRef.set(newComment)
                            .addOnSuccessListener { documentReference ->
//                                val newCommentId = documentReference.id
//                                newComment.id = newCommentId
                                debatedetailList.add(newComment)
                                Log.d("hyunsik", "newComment = $newComment")
                                adapter.notifyItemInserted(debatedetailList.size - 1)
                                binding.searchInput.text.clear()
                            }
                            .addOnFailureListener { e ->

                            }
                    }
                }
            }
        }

    }


    private fun loadComments() {
        val firestore = FirebaseFirestore.getInstance()
        val debateId = viewModel.debateId
        val userUID = viewModel.userUID

        if (debateId != null) {
            if (userUID != null) {
                firestore.collection("User")
                    .document(userUID)
                    .collection("Debates")
                    .document(debateId)
                    .collection("Comments")
                    .get()
                    .addOnSuccessListener { documents ->
                        Log.d("hyunsik", "documents = $documents")
                        debatedetailList.clear()
                        for (document in documents) {
                            val text = document.getString("text")
                            val user = document.getString("user")
                            val date = document.getString("date")
                            val commentId = document.id // Firestore에서 댓글 ID를 가져옵니다
                            if (text != null && user != null && date != null) {
                                debatedetailList.add(
                                    DebateDetailItem(
                                        commentId,
                                        text,
                                        user,
                                        date,
                                        userUID
                                    )
                                )

                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { e ->
                        Log.d("hyunsik", "error")
                    }
            }
        }
    }

    private fun addAgreeVote() {
        val user = auth.currentUser
        val debateId = viewModel.debateId
        val userUID2 = viewModel.userUID

        if (user != null && debateId != null) {
            val userUID = user.uid

            // Firestore에서 올바른 경로로 Agree 투표 문서를 설정합니다
            val voteRef = firestore.collection("User")
                .document(userUID2.toString())
                .collection("Debates")
                .document(debateId)
                .collection("AgreeVotes")
                .document(userUID) // 사용자의 UID를 문서 ID로 사용합니다

            // 투표 문서를 추가합니다
            voteRef.set(mapOf("userUID" to userUID))
                .addOnSuccessListener {
                    // 성공적으로 추가된 경우
                    // 여기에 추가 작업을 수행할 수 있습니다.
                }
                .addOnFailureListener { e ->
                    // 실패한 경우 처리
                }
        }
    }

    private fun removeAgreeVote() {
        val user = auth.currentUser
        val debateId = viewModel.debateId
        val userUID2 = viewModel.userUID

        if (user != null && debateId != null) {
            val userUID = user.uid

            // Firestore에서 Agree 투표 문서를 삭제합니다
            val voteRef = firestore.collection("User")
                .document(userUID2.toString())
                .collection("Debates")
                .document(debateId)
                .collection("AgreeVotes")
                .document(userUID)

            voteRef.delete()
        }
    }

    private fun updateAgreeCount() {
        val debateId = viewModel.debateId
        val tvAgree = binding.tvAgree
        val userUID = viewModel.userUID


        if (debateId != null) {
            val agreeVotesRef = firestore.collection("User")
                .document(userUID.toString())
                .collection("Debates")
                .document(debateId)
                .collection("AgreeVotes")

            agreeVotesRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val agreeCount = querySnapshot.size()
                    tvAgree.text = agreeCount.toString()
                }
                .addOnFailureListener { e ->
                    // 실패 시에 대응하는 로직을 추가할 수 있습니다.
                    Log.e("hyunsik", "Failed to update Agree count: $e")
                }
        }
    }

    // 사용자의 userUID가 AgreeVotes에 있는지 확인하는 함수
    private fun checkAgreeVoteStatus() {
        val user = auth.currentUser
        val debateId = viewModel.debateId
        val userUID2 = viewModel.userUID

        if (user != null && debateId != null) {
            val userUID = user.uid

            // 사용자의 투표 문서에 대한 참조
            val voteRef = firestore.collection("User")
                .document(userUID2.toString())
                .collection("Debates")
                .document(debateId)
                .collection("AgreeVotes")
                .document(userUID)

            voteRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // 사용자가 이미 투표했음, agreeClicked를 true로 설정
                        agreeClicked = true
                        binding.icAgree.setImageResource(R.drawable.ic_agree)
                    } else {
                        // 사용자가 아직 투표하지 않음, agreeClicked를 false로 설정
                        agreeClicked = false
                        binding.icAgree.setImageResource(R.drawable.ic_agreex)
                    }
                }
                .addOnFailureListener { e ->
                    // 실패 처리
                }
        }
    }

    private fun addOppositeVote() {
        val user = auth.currentUser
        val debateId = viewModel.debateId
        val userUID2 = viewModel.userUID

        if (user != null && debateId != null) {
            val userUID = user.uid

            val voteRef = firestore.collection("User")
                .document(userUID2.toString())
                .collection("Debates")
                .document(debateId)
                .collection("OppositeVotes")
                .document(userUID)

            voteRef.set(mapOf("userUID" to userUID))
                .addOnSuccessListener {
                    // 투표 추가 성공
                }
                .addOnFailureListener { e ->
                    // 투표 추가 실패 처리
                }
        }
    }

    private fun removeOppositeVote() {
        val user = auth.currentUser
        val debateId = viewModel.debateId
        val userUID2 = viewModel.userUID

        if (user != null && debateId != null) {
            val userUID = user.uid

            val voteRef = firestore.collection("User")
                .document(userUID2.toString())
                .collection("Debates")
                .document(debateId)
                .collection("OppositeVotes")
                .document(userUID)

            voteRef.delete()
        }
    }

    private fun updateOppositeCount() {
        val debateId = viewModel.debateId
        val tvOpposite = binding.tvOpposite
        val userUID = viewModel.userUID

        if (debateId != null) {
            val oppositeVotesRef = firestore.collection("User")
                .document(userUID.toString())
                .collection("Debates")
                .document(debateId)
                .collection("OppositeVotes")

            oppositeVotesRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val oppositeCount = querySnapshot.size()
                    tvOpposite.text = oppositeCount.toString()
                }
                .addOnFailureListener { e ->
                    Log.e("hyunsik", "Failed to update Opposite count: $e")
                }
        }
    }

    private fun checkOppositeVoteStatus() {
        val user = auth.currentUser
        val debateId = viewModel.debateId
        val userUID2 = viewModel.userUID

        if (user != null && debateId != null) {
            val userUID = user.uid

            val voteRef = firestore.collection("User")
                .document(userUID2.toString())
                .collection("Debates")
                .document(debateId)
                .collection("OppositeVotes")
                .document(userUID)

            voteRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        oppositeClicked = true
                        binding.icOpposite.setImageResource(R.drawable.ic_opposite)
                    } else {
                        oppositeClicked = false
                        binding.icOpposite.setImageResource(R.drawable.ic_oppositex)
                    }
                }
                .addOnFailureListener { e ->
                    // 실패 처리
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}