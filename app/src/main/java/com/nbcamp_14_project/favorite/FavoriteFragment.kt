package com.nbcamp_14_project.favorite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.nbcamp_14_project.R
import com.nbcamp_14_project.data.model.User
import com.nbcamp_14_project.databinding.FragmentFavoriteBinding
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.detail.DetailViewModel
import com.nbcamp_14_project.home.HomeViewPagerViewModel
import com.nbcamp_14_project.home.HomeViewPagerViewModelFactory
import com.nbcamp_14_project.mainpage.MainActivity
import com.nbcamp_14_project.setting.SettingActivity
import com.nbcamp_14_project.ui.login.CategoryFragment
import com.nbcamp_14_project.ui.login.LoginActivity
import com.nbcamp_14_project.ui.login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
    private val homeViewPagerViewModel: HomeViewPagerViewModel by activityViewModels()
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
    private val checkLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val checkLogin = result.data?.getStringExtra(LoginActivity.CHECK_LOGIN)
            if(checkLogin == "Login"){
                homeViewPagerViewModel.addListAtFirst("추천","추천")
            }
        }
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
                homeViewPagerViewModel.removeListAtFirst()
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
                        val category = document.getString("category")
                        val secondcategory = document.getString("secondCategory")
                        val thirdcategory = document.getString("thirdCategory")
                        binding.tvNick.text = "이름 : $nameField"
                        binding.tvCategory.text = "카테고리 : $category, $secondcategory, $thirdcategory"
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

        // TODO : RecyclerView 설정 - 가로 방향
        binding.favoriteList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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


        binding.ivFixCategory.setOnClickListener {
            showCategory()
            //주변 그림자
            loginViewModel.isCategoryBooleanValue.observe(requireActivity(), Observer { isTrue ->
                // 불린 값이 변경될 때 수행할 동작을 여기에 추가
                if (isTrue) {
                    binding.viewEmpty.visibility = View.VISIBLE
                } else {
                    binding.viewEmpty.visibility = View.INVISIBLE
                }
                updateCategory()
            })
        }
        if (binding.viewEmpty.visibility == View.INVISIBLE){
            updateCategory()
        }

    }

    //카테고리 보여주기
    private fun showCategory() {
        val detailFragment = CategoryFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frag_category, detailFragment)
        transaction.setReorderingAllowed(true)
        transaction.addToBackStack(null)
        transaction.commit()
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
        checkLoginLauncher.launch(intent)
    }

    fun updateCategory() {
        val curUser = auth.currentUser
        val user = User()
        val fbUser = firestore.collection("User").document(curUser?.uid!!)
        val updateData = hashMapOf(
            "category" to loginViewModel.category.value,
            "secondCategory" to loginViewModel.secondCategory.value,
            "thirdCategory" to loginViewModel.thirdCategory.value
        )
        Log.d("userContent", "${loginViewModel.category.value}")
        fbUser.update(updateData as Map<String, Any>).addOnSuccessListener {
            Log.d("updatingData", "success")
        }.addOnFailureListener { e ->
            // 업데이트 실패 시
        }


    }

}
