package com.nbcamp_14_project.debate

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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


        adapter.itemClick = object : DebateListAdapter.ItemClick {
            override fun onClick(view: View, position: Int, title: String) {
                val debateItem = debateList[position] // 클릭한 아이템을 가져옴

                // ViewModel에 Firebase 문서 ID와 다른 필요한 데이터를 할당
                viewModel.debateId = debateItem.id // Firestore 문서 ID
                viewModel.userUID = debateItem.userUID // Firestore 문서 ID
                viewModel.title = title // 제목
                // 사용자 ID

                val debateDetailFragment = DebateDetailFragment()
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    R.id.detailFragmentContainer,
                    debateDetailFragment
                )
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
                    Toast.makeText(requireContext(), "삭제 할 수 없습니다. 로그인이 필요합니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }

        binding.btnText.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                showAddDebateDialog()
                Log.d("#hyunsik", "plus")
            } else {
                Toast.makeText(requireContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show()

                navigateToLoginActivity()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        loadDebates()
        Log.d("hyunsik", "load = ${loadDebates()}")
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


                val newDebateItem = DebateItem("", debateTitle, name, userUID)
                firestore.collection("User").document(userUID)
                    .collection("Debates")
                    .add(newDebateItem)
                    .addOnSuccessListener { documentReference ->
                        val newID = documentReference.id
                        newDebateItem.id = newID
                        debateList.add(newDebateItem)
//                        viewModel.debateId = newID
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { e ->

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
                Log.d("hyunsik", "documents = $documents")
                debateList.clear()
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

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
