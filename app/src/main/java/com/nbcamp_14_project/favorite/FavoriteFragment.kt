package com.nbcamp_14_project.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.FragmentFavoriteBinding
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.detail.DetailViewModel
import com.nbcamp_14_project.mainpage.MainActivity
import com.nbcamp_14_project.setting.SettingActivity
import com.nbcamp_14_project.ui.login.LoginActivity
import com.nbcamp_14_project.ui.login.LoginViewModel

class FavoriteFragment : Fragment() {
    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoriteListAdapter
    private val viewModel: FavoriteViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val firestore = FirebaseFirestore.getInstance()
    private var isLogin = false
    private var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // 즐겨찾기 목록 업데이트
        getFavoriteListFromFireStore()
        Log.e("onResum", "#hyunsik")

        // 로그인 상태에 따른 화면 처리
        val loginBox = binding.root.findViewById<ConstraintLayout>(R.id.login_box)
        val profileBox = binding.root.findViewById<ConstraintLayout>(R.id.profile_box)
        val logoutButton = binding.root.findViewById<Button>(R.id.btn_logout)
        val textview2 = binding.root.findViewById<TextView>(R.id.textView2)

        binding.btnLogout.setOnClickListener {
            // 로그아웃 버튼 클릭 시 Firebase에서 로그아웃
            Firebase.auth.signOut()

            if (FirebaseAuth.getInstance().currentUser == null) {
                loginBox.visibility = View.VISIBLE
                profileBox.visibility = View.INVISIBLE
                textview2.visibility = View.VISIBLE

                viewModel.setFavoriteList(emptyList())
            }
        }

        if (FirebaseAuth.getInstance().currentUser != null) {
            // 로그인 상태일 때
            loginBox.visibility = View.INVISIBLE
            profileBox.visibility = View.VISIBLE
            logoutButton.visibility = View.VISIBLE
            binding.textView2.visibility = View.GONE

            val collectionRef = firestore.collection("User")
                .document(FirebaseAuth.getInstance().currentUser?.uid ?: return)
            collectionRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        val nameField = document.getString("name")
                        binding.tvNick.text = "이름 : $nameField"
                    } else {
                        Log.d("data", "no data")
                    }
                } else {
                    Log.d("data", "no data")
                }
            }
        } else {
            // 로그아웃 상태일 때
            loginBox.visibility = View.VISIBLE
            profileBox.visibility = View.INVISIBLE
            logoutButton.visibility = View.INVISIBLE
            binding.textView2.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 어댑터 초기화
        adapter = FavoriteListAdapter { item ->
            val detailInfo = item
            detailViewModel.setDetailInfo(detailInfo)
            val mainActivity = (activity as MainActivity)
            mainActivity.runDetailFragment()
        }

        // RecyclerView 설정
        binding.favoriteList.layoutManager = LinearLayoutManager(context)
        binding.favoriteList.adapter = adapter

        // 즐겨찾기 목록 갱신
        viewModel.favoriteList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.tvLogin.setOnClickListener {
            openLoginActivity(view)
        }

        //setting 페이지로 이동
        binding.settingBtn.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }
    }

    // Firebase에서 즐겨찾기 목록을 가져오는 함수
    private fun getFavoriteListFromFireStore() {
        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        val favoriteCollection = db.collection("User").document(userUID).collection("favorites")

        favoriteCollection.orderBy("created", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { querySnapshot ->
                val favoriteList = mutableListOf<DetailInfo>()

                for (document in querySnapshot) {
                    val title = document.getString("title")
                    val description = document.getString("description")
                    val thumbnail = document.getString("thumbnail")
                    val author = document.getString("author")
                    val originalLink = document.getString("originalLink")
                    val pubDate = document.getString("pubDate")

                    if (title != null && description != null && originalLink != null && pubDate != null) {
                        val detailInfo =
                            DetailInfo(
                                title,
                                description,
                                thumbnail,
                                author,
                                originalLink,
                                pubDate,
                                isLike = true
                            )
                        favoriteList.add(detailInfo)
                    }
                }

                viewModel.setFavoriteList(favoriteList)
            }
            .addOnFailureListener { e ->
                // 실패 시 처리
            }
    }

    // 로그인 화면으로 이동하는 함수
    fun openLoginActivity(view: View) {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }
}
