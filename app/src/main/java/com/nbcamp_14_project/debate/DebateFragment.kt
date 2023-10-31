package com.nbcamp_14_project.debate

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.FragmentDebateBinding
import com.nbcamp_14_project.ui.login.LoginActivity

class DebateFragment : Fragment() {
    private var _binding: FragmentDebateBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DebateListAdapter
    private val debateList = ArrayList<DebateItem>()
    val viewModel: DebateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDebateBinding.inflate(inflater, container, false)
        val firestore = FirebaseFirestore.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DebateListAdapter(debateList)
        binding.debateList.layoutManager = LinearLayoutManager(context)
        binding.debateList.adapter = adapter

        // 아이템 클릭 및 삭제 버튼 클릭 리스너 설정
        adapter.itemClick = object : DebateListAdapter.ItemClick {
            override fun onClick(view: View, position: Int, title: String) {
                // 아이템 클릭 시 ViewModel에 title 값을 설정
                viewModel.title = title

                val debateDetailFragment = DebateDetailFragment()
                val transaction = parentFragmentManager.beginTransaction()
                transaction.remove(this@DebateFragment) // 현재 Fragment 제거
                transaction.add(
                    R.id.detailFragmentContainer,
                    debateDetailFragment
                ) // 새로운 Fragment 추가
                transaction.addToBackStack(null)
                transaction.commit()
            }

            override fun onDeleteClick(position: Int) {
                val itemToDelete = debateList[position]
                val user = FirebaseAuth.getInstance().currentUser

                if (user != null) {
                    val userUID = user.uid

                    // 본인이 추가한 아이템만 삭제할 수 있도록 체크
                    if (userUID == itemToDelete.userUID) {
                        // 해당 사용자의 "Debates" 컬렉션에서 데이터 삭제
                        val firestore = FirebaseFirestore.getInstance()
                        firestore.collection("User").document(itemToDelete.userUID)
                            .collection("Debates")
                            .document(itemToDelete.id)
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
                    } else {
                        // 본인이 추가한 아이템이 아닌 경우
                        Toast.makeText(requireContext(), "삭제 할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 사용자가 로그아웃된 경우
                    Toast.makeText(requireContext(), "삭제 할 수 없습니다. 로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.btnText.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // 사용자가 로그인되어 있음
                // 토론 추가 다이얼로그 띄우는 코드 여기에 추가
                showAddDebateDialog()
            } else {
                // 사용자가 로그인되어 있지 않음
                // "로그인 해주세요" 메시지 표시
                Toast.makeText(requireContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show()
                // LoginActivity로 이동
                navigateToLoginActivity()
            }
        }

        loadDebates() // 사용자의 debates를 불러오는 함수 호출
    }

    private fun showAddDebateDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("토론 추가하기")
        builder.setIcon(R.drawable.ic_people)

        val v1 = layoutInflater.inflate(R.layout.dialog_debate, null)
        builder.setView(v1)

        val tvdebate: EditText = v1.findViewById(R.id.tv_debate)
        val tvname: EditText = v1.findViewById(R.id.tv_name)

        val confirmListener = DialogInterface.OnClickListener { _, _ ->
            val debateTitle = tvdebate.text.toString()
            val name = tvname.text.toString()

            val firestore = FirebaseFirestore.getInstance()
            val user = FirebaseAuth.getInstance().currentUser
            user?.let { currentUser ->
                val userUID = currentUser.uid

                // 새로운 토론 아이템 생성 및 추가
                val newDebateItem = DebateItem("", debateTitle, name, userUID)
                firestore.collection("User").document(userUID)
                    .collection("Debates")
                    .add(newDebateItem)
                    .addOnSuccessListener { documentReference ->
                        // 추가 성공
                        val newID = documentReference.id
                        newDebateItem.id = newID
                        debateList.add(newDebateItem)
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { e ->
                        // 추가 실패
                        // 실패 시에 사용자에게 메시지를 표시할 수 있습니다.
                    }
            }
        }

        builder.setPositiveButton("확인", confirmListener)
        builder.setNegativeButton("취소", null)

        builder.show()
    }

    private fun navigateToLoginActivity() {
        // LoginActivity로 이동하는 Intent를 생성
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    private fun loadDebates() {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collectionGroup("Debates")
            .get()
            .addOnSuccessListener { documents ->
                debateList.clear() // 기존 목록을 비우고 다시 데이터를 채웁니다.
                for (document in documents) {
                    val id = document.id
                    val title = document.getString("title")
                    val name = document.getString("name")
                    val userUID = document.getString("userUID")
                    if (title != null && name != null && userUID != null) {
                        debateList.add(DebateItem(id, title, name, userUID))
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // 실패 시에 사용자에게 메시지를 표시할 수 있습니다.
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
