// DebateDetailFragment.kt

package com.nbcamp_14_project.debate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
    private var auth = FirebaseAuth.getInstance()
    private val viewModel: DebateViewModel by activityViewModels()
    private val debatedetailList = ArrayList<DebateDetailItem>()

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

        // RecyclerView에 어댑터 설정
        adapter = DebateDetailListAdapter(debatedetailList)
        binding.debatedetailList.layoutManager = LinearLayoutManager(context)
        binding.debatedetailList.adapter = adapter

        // 댓글 입력 버튼 클릭 이벤트 처리
        binding.searchBtn.setOnClickListener {
            // EditText에서 입력된 댓글을 가져옵니다.
            val commentText = binding.searchInput.text.toString().trim()

            // 입력된 댓글이 비어 있지 않은 경우에만 처리합니다.
            if (commentText.isNotEmpty()) {
                // 이 댓글을 어딘가에 저장합니다. 예를 들어, 리스트에 추가할 수 있습니다.
                val newComment = DebateDetailItem(commentText, "사용자 이름", "날짜")

                // 저장된 댓글을 RecyclerView의 데이터에 추가합니다.
                debatedetailList.add(newComment)
                adapter.notifyItemInserted(debatedetailList.size - 1)

                // 입력 필드를 지워 사용자가 새 댓글을 입력할 수 있도록 합니다.
                binding.searchInput.text.clear()
            }
        }
    }

    // onDestroyView 메서드 내에서 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
