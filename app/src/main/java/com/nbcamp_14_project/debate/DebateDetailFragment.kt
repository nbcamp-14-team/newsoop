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

        val title = viewModel.title

        if (title != null) {
            binding.tvTitle.text = title
        }

        // 데이터 목록 생성 (예시 데이터)
        val debatedetailList = listOf(
            DebateDetailItem("1동의합니다", "정현식", "2023-10-31"),
            DebateDetailItem("2동의합니다", "정현식", "2023-10-31"),

        )

        adapter = DebateDetailListAdapter(debatedetailList)
        binding.debatedetailList.layoutManager = LinearLayoutManager(context)
        binding.debatedetailList.adapter = adapter
    }

    // onDestroyView 메서드 내에서 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
