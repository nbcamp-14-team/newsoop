package com.nbcamp_14_project.debate

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.FragmentDebatedetailBinding
import com.nbcamp_14_project.ui.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.Date

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
    private var isAgreeButtonClicked = false
    private var isOppositeButtonClicked = false
    private var agreeNum = 0.0
    private var oppositeNum = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDebatedetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//         SwipeRefreshLayout 초기화
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)

//         Swipe to Refresh 리스너 설정
        swipeRefreshLayout.setOnRefreshListener {
            // Swipe to Refresh 동작 시, 데이터 다시 불러오는 작업 수행
            loadComments(viewModel.debateId ?: "")
        }


        val spinner = binding.homeSpinner
        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_options, // 이것은 res/values/strings.xml에 정의된 배열 리소스여야 합니다.
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 선택된 옵션에 따른 정렬 작업 수행
                when (position) {
                    0 -> {
                        // "오래된순"이 선택된 경우
                        debatedetailList.sortBy { it.date }
                        adapter.notifyDataSetChanged()

                    }

                    1 -> {

                        // "최신순"이 선택된 경우
                        debatedetailList.sortByDescending { it.date }
                        adapter.notifyDataSetChanged()

                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무 항목도 선택되지 않았을 때의 동작 (optional)
            }

        }




        binding.btnComment.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                agreeClicked = false
                isAgreeButtonClicked = false
                oppositeClicked = false
                isOppositeButtonClicked = false
                showAddCommentDialog()
                Log.d("#hyunsik", "plus")


            } else {
                Toast.makeText(requireContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show()
                navigateToLoginActivity()
            }
        }


        val agreeImageView = binding.icAgree
        val tvAgree = binding.tvAgree




        checkAgreeVoteStatus()


        agreeImageView.setOnClickListener {
            if (oppositeClicked) {
            } else {
                if (agreeClicked) {
                    // 이미 클릭되었을 때
                    agreeImageView.setImageResource(R.drawable.ic_agreex)
                    tvAgree.text = (tvAgree.text.toString().toInt() - 1).toString()
                    removeAgreeVote() // 투표 제거
                    --agreeNum
                    getPerNum()
                } else {
                    // 클릭되지 않았을 때
                    agreeImageView.setImageResource(R.drawable.ic_agree)
                    tvAgree.text = (tvAgree.text.toString().toInt() + 1).toString()
                    addAgreeVote() // 투표 추가
                    ++agreeNum
                    getPerNum()
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
                    --oppositeNum
                    getPerNum()
                } else {
                    oppositeImageView.setImageResource(R.drawable.ic_opposite)
                    tvOpposite.text = (tvOpposite.text.toString().toInt() + 1).toString()
                    addOppositeVote()
                    ++oppositeNum
                    getPerNum()
                }
                oppositeClicked = !oppositeClicked
            }
        }


        binding.imgBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val title = viewModel.title
        val originalcontext = viewModel.originalcontext
        val agreecontext = viewModel.agreecontext
        val oppositecontext = viewModel.oppositecontext
        val name = viewModel.name


        val fullAgreeText = "Agree : ${agreecontext}"

// SpannableString을 생성합니다.
        val spannableString = SpannableString(fullAgreeText)

// "Agree" 문자열이 전체 텍스트에서 시작하는 인덱스를 찾습니다.
        val startIndex = fullAgreeText.indexOf("Agree")

// "Agree" 부분에 적용할 파란색을 정의합니다.
        val colorBlue = resources.getColor(R.color.agree_blue) // 원하는 파란색 리소스로 변경하세요

// "Agree"를 파란색으로 변경합니다.
        spannableString.setSpan(ForegroundColorSpan(colorBlue), startIndex, startIndex + "Agree".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val fullDisagreeText = "Disagree : ${oppositecontext}"

// SpannableString을 생성합니다.
        val spannableString1 = SpannableString(fullDisagreeText)

// "Disagree" 문자열이 전체 텍스트에서 시작하는 인덱스를 찾습니다.
        val startIndex1 = fullDisagreeText.indexOf("Disagree")

// "Disagree" 부분에 적용할 빨간색을 정의합니다.
        val colorRed = resources.getColor(R.color.disagree_red) // 원하는 빨간색 리소스로 변경하세요

// "Disagree"를 빨간색으로 변경합니다.
        spannableString1.setSpan(ForegroundColorSpan(colorRed), startIndex1, startIndex1 + "Disagree".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

// TextView에 SpannableString을 설정하여 "Disagree"를 빨간색으로 표시합니다

        if (title != null) {
            binding.tvTitle.text = title
            binding.tvContext.text = originalcontext
            binding.tvAgreecontext.text = spannableString
            binding.tvOppositecontext.text = spannableString1
            binding.tvName.text = "작성자 : ${name}"
        }

        adapter = DebateDetailListAdapter(debatedetailList)
        binding.debatedetailList.layoutManager = LinearLayoutManager(context)
        binding.debatedetailList.adapter = adapter

        adapter.itemClick = object : DebateDetailListAdapter.ItemClick {


            override fun onDeleteCommentClick(position: Int) {
                val commentToDelete = debatedetailList[position]
                val debateId = viewModel.debateId
                val user = FirebaseAuth.getInstance().currentUser
                val userUID3 = viewModel.userUID

                if (user != null && debateId != null) {
                    val userUID = user.uid
                    val commentUserUID = commentToDelete.userUID


                    if (userUID == commentUserUID) {

                        Log.d("hyunsik", "userUID=$userUID")
                        Log.d("hyunsik", "commentUserUID=$commentUserUID")

                        val firestore = FirebaseFirestore.getInstance()
                        val commentRef = firestore.collection("User")
                            .document(userUID3.toString())
                            .collection("Debates")
                            .document(debateId)
                            .collection("Comments")
                            .document(commentToDelete.id)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("hyunsik", "deletedebatedetailList=$debatedetailList")
                                debatedetailList.removeAt(position)
                                Log.d("hyunsik", "removedebatedetailList=$debatedetailList")

                                adapter.notifyItemRemoved(position)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "삭제 실패!!.", Toast.LENGTH_SHORT)
                                    .show()

                            }
                    } else {
                        Log.d("hyunsik", "userUID=$userUID")
                        Log.d("hyunsik", "commentUserUID=$commentUserUID")

                        Toast.makeText(
                            requireContext(),
                            "본인이 작성한 댓글만 삭제 가능합니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {

                    Toast.makeText(requireContext(), "삭제 할 수 없습니다. 로그인이 필요합니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }


        val debateId = viewModel.debateId
        Log.d("hyunsik", "debateId = $debateId")
        if (debateId != null) {
            loadComments(debateId)
            Log.d("hyunsik", "load = ${loadComments(debateId)}")
        }


    }


    private fun loadComments(debateId: String) {
        val firestore = FirebaseFirestore.getInstance()
        val userUID = viewModel.userUID

        if (userUID != null) {
            firestore.collection("User")
                .document(userUID)
                .collection("Debates")
                .document(debateId)
                .collection("Comments")
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    debatedetailList.clear()
                    for (document in documents) {
                        val text = document.getString("text")
                        val user = document.getString("user")
                        val date = document.getString("date")
                        val commentId = document.id
                        val userUID2 = document.getString("userUID")

                        val commentType =
                            document.getLong("commentType")?.toInt() ?: Comment.TYPE_AGREE

                        if (text != null && user != null && date != null) {
                            debatedetailList.add(
                                DebateDetailItem(
                                    commentType,
                                    commentId,
                                    text,
                                    user,
                                    date,
                                    userUID2.toString()
                                )
                            )
                        }
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("hyunsik", "loaddebatedetailList=$debatedetailList")
                }
                .addOnFailureListener { e ->
                    Log.d("hyunsik", "error")
                }
        }

        val swipeRefreshLayout = view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        swipeRefreshLayout?.isRefreshing = false

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
                    //찬성 숫자
                    agreeNum = agreeCount.toDouble()
                    getPerNum()
                    Log.d("debate", "agree :$agreeNum")
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
                    //반대표 숫자
                    oppositeNum = oppositeCount.toDouble()
                    getPerNum()
                    Log.d("debate", "opposite : $oppositeNum")

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

    private fun showAddCommentDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.RoundedCornersAlertDialog)
        val dialogView = layoutInflater.inflate(R.layout.dialog_debatedetail, null)
        val editTextComment = dialogView.findViewById<EditText>(R.id.tv_comment)
        val btnAgree = dialogView.findViewById<MaterialButton>(R.id.btn_agree)
        val btnOpposite = dialogView.findViewById<MaterialButton>(R.id.btn_opposite)

        builder.setView(dialogView)

        builder.setTitle("댓글 추가하기")
        builder.setIcon(R.drawable.ic_people)

        btnAgree.setOnClickListener {
            if (oppositeClicked) {
                // 다른 버튼이 클릭된 경우 아무것도 하지 않음
            } else {
                if (isAgreeButtonClicked) {
                    // 이미 클릭되었을 때
                    btnAgree.strokeColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.lightGray // 다시 원래 색상인 red로 변경
                        )
                    )
                } else {
                    // 클릭되지 않았을 때
                    btnAgree.strokeColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.blue // 클릭 시 blue로 변경
                        )
                    )
                }

                isAgreeButtonClicked = !isAgreeButtonClicked // 클릭 상태 토글
                agreeClicked = isAgreeButtonClicked // agreeClicked 변수 업데이트
            }
        }

        btnOpposite.setOnClickListener {
            if (agreeClicked) {
                // 다른 버튼이 클릭된 경우 아무것도 하지 않음
            } else {
                if (isOppositeButtonClicked) {
                    // 이미 클릭되었을 때
                    btnOpposite.strokeColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.lightGray // 다시 원래 색상인 red로 변경
                        )
                    )
                } else {
                    // 클릭되지 않았을 때
                    btnOpposite.strokeColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red // 클릭 시 blue로 변경
                        )
                    )
                }

                isOppositeButtonClicked = !isOppositeButtonClicked // 클릭 상태 토글
                oppositeClicked = isOppositeButtonClicked // agreeClicked 변수 업데이트
            }
        }

        builder.setPositiveButton("확인") { _, _ ->

        }
        builder.setNegativeButton("취소", null)
        val dialog = builder.create()


        dialog.show()
        val possitiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        possitiveButton.setOnClickListener {
            val commentText = editTextComment.text.toString().trim()
            if (commentText.isEmpty()) {
                Toast.makeText(context, "댓글 내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else if (!isAgreeButtonClicked && !isOppositeButtonClicked) {
                Toast.makeText(context, "찬성 혹은 반대를 선택해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                val user = auth.currentUser
                user?.let { currentUser ->
                    val userUID = currentUser.uid
                    val userUID2 = viewModel.userUID
                    val debateId = viewModel.debateId
                    val currentDate = Date()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val formattedDate = dateFormat.format(currentDate)
                    val commentType =
                        if (isAgreeButtonClicked) Comment.TYPE_AGREE else Comment.TYPE_OPPOSE

                    if (debateId != null) {
                        val commentCollection = firestore.collection("User")
                            .document(userUID2.toString())
                            .collection("Debates")
                            .document(debateId)
                            .collection("Comments")

                        // 사용자의 이름을 가져오는 부분 추가
                        val userDocRef = firestore.collection("User").document(userUID)
                        userDocRef.get()
                            .addOnSuccessListener { userDoc ->
                                if (userDoc.exists()) {
                                    val userName = userDoc.getString("name")
                                    val newComment = DebateDetailItem(
                                        commentType,
                                        text = commentText,
                                        user = userName.toString(), // 사용자의 이름으로 설정
                                        date = formattedDate,
                                        userUID = userUID
                                    )
                                    val newRef = commentCollection.document()
                                    newComment.id = newRef.id
                                    newRef.set(newComment)
                                        .addOnSuccessListener {
                                            debatedetailList.add(newComment)
                                            adapter.notifyItemInserted(debatedetailList.size - 1)
                                        }
                                        .addOnFailureListener { e ->
                                            // 실패 처리
                                        }
                                }
                            }
                            .addOnFailureListener { e ->
                                // 실패 처리
                            }
                    }
                }
                dialog.dismiss()
            }

        }


    }


    private fun navigateToLoginActivity() {
        // LoginActivity로 이동하는 Intent를 생성
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Progress bar 퍼센트 넣기
    private fun getPerNum() {
        var perNum = 0.5
        Log.d("debate", "$agreeNum , $oppositeNum")
        if (agreeNum == 0.0) {
            if (oppositeNum == 0.0) {
                binding.debateProgressBar.progress = 50
            } else {
                binding.debateProgressBar.progress = 0
            }
        } else if (oppositeNum == 0.0) {
            binding.debateProgressBar.progress = 100
        } else {
            perNum = agreeNum / (agreeNum + oppositeNum)
            Log.d("perNum", "agree = $agreeNum ,  opposite = $oppositeNum perNum =  $perNum")
            binding.debateProgressBar.progress = (perNum * 100).toInt()
        }
    }
}