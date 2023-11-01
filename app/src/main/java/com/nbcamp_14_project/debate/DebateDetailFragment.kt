package com.nbcamp_14_project.debate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.databinding.FragmentDebatedetailBinding

class DebateDetailFragment : Fragment() {
    private var _binding: FragmentDebatedetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DebateDetailListAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val debatedetailList = ArrayList<DebateDetailItem>()
    private val viewModel: DebateViewModel by activityViewModels()

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

                    if (debateId != null) {
                        Log.d("hyunsik1", "debateId = $debateId()")
                        val commentCollection = firestore.collection("User").document(userUID)
                            .collection("Debates").document(debateId).collection("Comments")
                        val newComment = hashMapOf(
                            "text" to commentText,
                            "user" to "사용자 이름", // 사용자 이름을 가져오거나 설정해야 합니다.
                            "date" to "날짜" // 날짜를 가져오거나 설정해야 합니다.
                        )

                        commentCollection.add(newComment)
                            .addOnSuccessListener { documentReference ->
                                val newCommentId = documentReference.id
                                debatedetailList.add(DebateDetailItem(commentText, "사용자 이름", "날짜"))
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
                            if (text != null && user != null && date != null) {
                                debatedetailList.add(DebateDetailItem(text, user, date))
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
