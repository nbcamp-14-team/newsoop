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

                // Firestore에 데이터 추가
                val firestore = FirebaseFirestore.getInstance()
                var newDebateItem = DebateItem("", debateTitle, name) // ID는 비어있는 상태로 생성
                firestore.collection("Debates")
                    .add(newDebateItem)
                    .addOnSuccessListener { documentReference ->
                        // 추가 성공
                        val newID = documentReference.id // Firestore에서 생성된 ID를 얻음
                        newDebateItem = DebateItem(newID, debateTitle, name) // ID를 설정
                        debateList.add(newDebateItem)
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { e ->
                        // 추가 실패
                        // 실패 시에 사용자에게 메시지를 표시할 수 있습니다.
                    }
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
                val itemToDelete = debateList[position]
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("Debates").document(itemToDelete.id)
                    .delete()
                    .addOnSuccessListener {
                        // Firestore에서 삭제 성공
                        debateList.removeAt(position)
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { e ->
                        // 삭제 실패
                        // 실패 시에 사용자에게 메시지를 표시할 수 있습니다.
                    }
            }
        }
    }

    // Fragment가 파괴될 때 binding을 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        // Firestore에서 데이터를 가져와서 debateList를 업데이트
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("Debates")
            .get()
            .addOnSuccessListener { documents ->
                debateList.clear() // 기존 목록을 비우고 다시 데이터를 채웁니다.
                for (document in documents) {
                    val id = document.id
                    val title = document.getString("title")
                    val name = document.getString("name")
                    if (title != null && name != null) {
                        debateList.add(DebateItem(id, title, name))
                    }
                }
                adapter.notifyDataSetChanged() // RecyclerView 갱신
            }
            .addOnFailureListener { e ->
                // 실패 시에 사용자에게 메시지를 표시할 수 있습니다.
            }
    }
}
