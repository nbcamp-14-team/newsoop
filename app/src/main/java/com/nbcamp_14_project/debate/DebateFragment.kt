package com.nbcamp_14_project.debate

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.FragmentDebateBinding

class DebateFragment : Fragment() {
    private var _binding: FragmentDebateBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DebateListAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()

    // 데이터 목록 생성 (예시 데이터)를 클래스 변수로 선언
    private val debateList = ArrayList<DebateItem>()

    // ViewModel을 선언
    private val viewModel: DebateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDebateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 2. 커스텀 다이얼로그
        binding.btnText.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("토론 추가하기")
            builder.setIcon(R.drawable.ic_people)

            val v1 = layoutInflater.inflate(R.layout.dialog_debate, null)
            builder.setView(v1)

            val tvdebate: EditText = v1.findViewById(R.id.tv_debate)
            val tvname: EditText = v1.findViewById(R.id.tv_name)

            // 확인 버튼을 눌렀을 때 실행할 코드
            val confirmListener = DialogInterface.OnClickListener { _, _ ->
                val debateTitle = tvdebate.text.toString()
                val name = tvname.text.toString()

                // 데이터 모델을 만들거나 목록에 추가
                val newDebateItem = DebateItem(debateTitle, name)
                debateList.add(newDebateItem)

                // RecyclerView 어댑터에게 데이터 변경을 알림
                adapter.notifyDataSetChanged()
            }

            builder.setPositiveButton("확인", confirmListener)
            builder.setNegativeButton("취소", null)

            builder.show()
        }

        // 이제 목록을 어댑터에 연결
        adapter = DebateListAdapter(debateList)
        binding.debateList.layoutManager = LinearLayoutManager(context)
        binding.debateList.adapter = adapter

        adapter.itemClick = object : DebateListAdapter.ItemClick {
            override fun onClick(view: View, position: Int, title: String) {
                // 아이템 클릭 시 ViewModel에 title 값을 설정
                viewModel.title = title

                val debateDetailFragment = DebateDetailFragment()
                val transaction = parentFragmentManager.beginTransaction()
                transaction.remove(this@DebateFragment) // 현재 Fragment 제거
                transaction.add(R.id.detailFragmentContainer, debateDetailFragment) // 새로운 Fragment 추가
                transaction.addToBackStack(null)
                transaction.commit()
            }
            override fun onDeleteClick(position: Int) {
                debateList.removeAt(position)
                adapter.notifyDataSetChanged()
            }
        }


    }

    // Fragment가 파괴될 때 binding을 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


