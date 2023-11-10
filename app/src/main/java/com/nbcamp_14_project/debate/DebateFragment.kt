// DebateFragment.kt
package com.nbcamp_14_project.debate

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.FragmentDebateBinding
import com.nbcamp_14_project.ui.login.LoginActivity
import java.util.Date

// Fragment를 상속받은 DebateFragment 클래스 정의
class DebateFragment : Fragment() {
    // 뷰 바인딩을 위한 바인딩 객체 선언
    private var _binding: com.nbcamp_14_project.databinding.FragmentDebateBinding? = null
    private val binding get() = _binding!!

    // 토론 목록을 저장하는 어레이 리스트와 어댑터, ViewModel 선언
    private lateinit var adapter: DebateListAdapter
    private val debateList = ArrayList<DebateItem>()
    val viewModel: DebateViewModel by activityViewModels()

    // onCreateView 함수 오버라이드 - Fragment의 뷰 생성
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩 초기화
        _binding = FragmentDebateBinding.inflate(inflater, container, false)
        // Firestore 인스턴스 초기화
        val firestore = FirebaseFirestore.getInstance()

        // 뷰 바인딩의 root를 반환
        return binding.root
    }

    // onViewCreated 함수 오버라이드 - 뷰가 생성된 후 호출되는 함수
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 검색 버튼 클릭 시의 동작 설정
        binding.searchBtn.setOnClickListener {
            val searchText = binding.searchInput.text.toString()

            // 검색어와 일치하는 항목을 저장할 목록을 만듭니다.
            val searchResults = ArrayList<DebateItem>()

            // 검색어와 tv_title을 비교하여 일치하는 항목을 찾습니다.
            for (debateItem in debateList) {
                if (debateItem.title.contains(searchText, ignoreCase = true)) {
                    searchResults.add(debateItem)
                }
            }

            // 어댑터에 검색 결과를 설정하여 RecyclerView를 업데이트합니다.
            adapter.setDebateList(searchResults)
        }

        // 어댑터 초기화 및 리사이클러뷰 설정
        adapter = DebateListAdapter()
        binding.debateList.layoutManager = LinearLayoutManager(context)
        binding.debateList.adapter = adapter

        // 아이템 클릭 이벤트 설정
        adapter.itemClick = object : DebateListAdapter.ItemClick {
            override fun onClick(view: View, position: Int, title: String) {
                // 클릭한 아이템의 정보를 ViewModel에 저장
                val debateItem = debateList[position]
                viewModel.debateId = debateItem.id
                viewModel.userUID = debateItem.userUID
                viewModel.title = title
                viewModel.name = debateItem.name
                viewModel.originalcontext = debateItem.originalcontext
                viewModel.agreecontext = debateItem.agreecontext
                viewModel.oppositecontext = debateItem.oppositecontext

                // 토론 상세 화면으로 이동
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
                // 삭제 버튼 클릭 시 동작
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
                                // Firestore에서 삭제 성공 시 목록에서도 제거
                                debateList.removeAt(position)
                                adapter.setDebateList(debateList)
                            }
                            .addOnFailureListener { e ->
                                // 삭제 실패
                                // 실패 시에 사용자에게 메시지를 표시할 수 있습니다.
                            }
                    } else {
                        // 본인이 추가한 아이템이 아닌 경우
                        showSnackbar("삭제 할 수 없습니다.")
                    }
                } else {
                    // 사용자가 로그아웃된 경우
                    showSnackbar("삭제 할 수 없습니다. 로그인이 필요합니다.")
                }
            }
        }

        // "토론 추가" 버튼 클릭 시의 동작 설정
        binding.btnText.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // 로그인된 경우 토론 추가 다이얼로그 표시
                showAddDebateDialog()
            } else {
                // 로그인되지 않은 경우 로그인 화면으로 이동
                showSnackbar("로그인을 해주세요.")
                navigateToLoginActivity()
            }
        }
    }

    // onResume 함수 오버라이드 - Fragment가 화면에 표시될 때 호출되는 함수
    override fun onResume() {
        super.onResume()
        // 토론 목록을 새로고침
        loadDebates()
    }

    // 토론 추가 다이얼로그 표시 함수
    private fun showAddDebateDialog() {
        // 다이얼로그 빌더 생성
        val builder = AlertDialog.Builder(requireContext(), R.style.RoundedCornersAlertDialog)
        builder.setTitle("토론 추가하기")
        builder.setIcon(R.drawable.ic_people)

        // 다이얼로그 레이아웃 설정
        val v1 = layoutInflater.inflate(R.layout.dialog_debate, null)
        builder.setView(v1)

        // 다이얼로그 내의 에디트텍스트 초기화
        val tvdebate: EditText = v1.findViewById(R.id.tv_debate)
        val tvContext: EditText = v1.findViewById(R.id.tv_context)
        val tvAgree: EditText = v1.findViewById(R.id.tv_agree)
        val tvOpposite: EditText = v1.findViewById(R.id.tv_opposite)

        // 확인 버튼 클릭 시의 동작 설정
        builder.setPositiveButton("확인") { _, _ -> }

        // 취소 버튼 클릭 시의 동작 설정
        builder.setNegativeButton("취소", null)
        val dialog = builder.create()

        // 다이얼로그 표시
        dialog.show()

        // 확인 버튼 클릭 리스너 설정
        val possitiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        possitiveButton.setOnClickListener {
            // 입력된 데이터 가져오기
            val debateTitle = tvdebate.text.toString()
            val originalcontext = tvContext.text.toString()
            val agreecontext = tvAgree.text.toString()
            val oppositecontext = tvOpposite.text.toString()

            // 입력 데이터 유효성 검사
            if (debateTitle.isEmpty()) {
                // 토론 주제가 입력되지 않은 경우 오류 메시지 표시
                showSnackbar("토론할 주제를 입력해 주세요.")
            } else if (originalcontext.isEmpty() || agreecontext.isEmpty() || oppositecontext.isEmpty()) {
                // 토론 내용, 찬성 의견, 반대 의견 중 하나라도 비어있는 경우 오류 메시지 표시
                showSnackbar("빈칸의 내용을 모두 입력해 주세요.")
            } else {
                // Firebase에 토론 아이템 추가
                val firestore = FirebaseFirestore.getInstance()
                val user = FirebaseAuth.getInstance().currentUser
                user?.let { currentUser ->
                    val userUID = currentUser.uid

                    // 로그인한 사용자의 이름을 가져오는 부분
                    firestore.collection("User").document(userUID)
                        .get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val name = documentSnapshot.getString("name")

                                if (name != null) {
                                    // 사용자 이름을 가져왔을 경우에만 토론 아이템을 추가합니다.
                                    val commentCollection =
                                        firestore.collection("User").document(userUID)
                                            .collection("Debates")
                                    val timestamp = Date()
                                    val newDebateItem =
                                        DebateItem(
                                            title = debateTitle,
                                            originalcontext = originalcontext,
                                            agreecontext = agreecontext,
                                            oppositecontext = oppositecontext,
                                            name = name,
                                            userUID = userUID,
                                            timestamp = timestamp
                                        )
                                    val newRef = commentCollection.document()
                                    newDebateItem.id = newRef.id
                                    newRef.set(newDebateItem)
                                        .addOnSuccessListener { documentReference ->
                                            // Firestore에서 추가 성공 시 목록에도 추가
                                            debateList.add(newDebateItem)
                                            adapter.setDebateList(debateList)
                                        }
                                        .addOnFailureListener { e ->

                                        }
                                } else {
                                    // 사용자 이름이 없을 경우에 대처할 내용을 추가할 수 있습니다.
                                    // 예를 들어, 에러 메시지 표시 등
                                }
                            } else {
                                // 사용자 문서가 없을 경우에 대처할 내용을 추가할 수 있습니다.
                            }
                        }
                        .addOnFailureListener { e ->

                        }
                }
                dialog.dismiss()
            }
        }
    }

    // LoginActivity로 이동하는 함수
    private fun navigateToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    // 토론 목록을 불러오는 함수
    private fun loadDebates() {
        val firestore = FirebaseFirestore.getInstance()

        // "Debates" 컬렉션에서 모든 문서 가져오기
        firestore.collectionGroup("Debates")
            .get()
            .addOnSuccessListener { documents ->
                debateList.clear()
                for (document in documents) {
                    // 각 문서의 데이터 가져오기
                    val id = document.id
                    val title = document.getString("title")
                    val originalcontext = document.getString("originalcontext")
                    val agree = document.getString("agreecontext")
                    val opposite = document.getString("oppositecontext")
                    val name = document.getString("name")
                    val userUID = document.getString("userUID")
                    val timestamp = Date()

                    // 가져온 데이터를 이용하여 DebateItem 객체 생성 및 목록에 추가
                    if (title != null && name != null && userUID != null) {
                        debateList.add(
                            DebateItem(
                                id,
                                title,
                                originalcontext.toString(),
                                agree.toString(),
                                opposite.toString(),
                                name,
                                userUID,
                                timestamp
                            )
                        )
                    }
                }

                // 어댑터에 토론 목록 설정 및 업데이트
                adapter.setDebateList(debateList)
            }
            .addOnFailureListener { e ->

            }
    }

    // Snackbar 표시 함수
    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    // Fragment 종료 시 뷰 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
