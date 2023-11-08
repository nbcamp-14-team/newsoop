package com.nbcamp_14_project.favorite

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.nbcamp_14_project.R
import com.nbcamp_14_project.data.model.User
import com.nbcamp_14_project.databinding.FragmentFavoriteBinding
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.detail.DetailViewModel
import com.nbcamp_14_project.home.HomeModel
import com.nbcamp_14_project.home.HomeViewModel
import com.nbcamp_14_project.home.HomeViewPagerViewModel
import com.nbcamp_14_project.home.toDetailInfo
import com.nbcamp_14_project.mainpage.MainActivity
import com.nbcamp_14_project.setting.SettingActivity
import com.nbcamp_14_project.ui.login.CategoryFragment
import com.nbcamp_14_project.ui.login.LoginActivity
import com.nbcamp_14_project.ui.login.LoginViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class FavoriteFragment : Fragment() {
    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val authorData: ArrayList<HomeModel> = ArrayList()
    private lateinit var adapter: FavoriteListAdapter
    private val viewModel: FavoriteViewModel by lazy {
        ViewModelProvider(
            requireActivity(), FavoriteViewModel.FavoriteViewModelFactory()
        )[FavoriteViewModel::class.java]
    }
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val homeViewPagerViewModel: HomeViewPagerViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val firestore = FirebaseFirestore.getInstance()
    private var isLogin = false
    private var auth = FirebaseAuth.getInstance()
    private val authorNameList = mutableListOf<String>()
    private val queryAuthorList = mutableListOf<String>()
    private lateinit var query: String

    private val followingAdapter by lazy {
        FollowingListAdapter(//recyclerView에서 클릭이 일어났을 때, Detail에 데이터값을 보냄
            onItemClick = { item ->
                val detailInfo =
                    item
                detailViewModel.setDetailInfo(item.toDetailInfo())//뷰모델로 전송
                val mainActivity = (activity as MainActivity)
                mainActivity.runDetailFragment()//DetailFragment 실행
            }
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    private val checkLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val checkLogin = result.data?.getStringExtra(LoginActivity.CHECK_LOGIN)
                if (checkLogin == "Login") {
                    homeViewPagerViewModel.addListAtFirst("추천", "추천")
                }
            }
        }

    private val user = FirebaseAuth.getInstance().currentUser
    private val userUID = user?.uid
    private var selectedImageUri: Uri? = null
    private val pickImageActivityResult =//갤러리에서 선택한 사진 적용
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            with(binding) {
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        selectedImageUri = data.data
                        if (selectedImageUri != null) {
                            binding.imgProfile.load(selectedImageUri) {
                                transformations(CircleCropTransformation())
                                if (userUID != null) {
                                    deleteFirebaseImage(userUID)
                                    setFirebaseImage()
                                }
                            }
                        } else {
                            Toast.makeText(activity, "사진을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    private val permissionList = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val permissionImage = android.Manifest.permission.READ_MEDIA_IMAGES
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                Toast.makeText(requireContext(), "권한 허가", Toast.LENGTH_SHORT).show()
            }

            false -> {
                Toast.makeText(requireContext(), "권한 거부 하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 즐겨찾기 목록 업데이트
        getFollowingAuthorListFromFireStore()
        getFavoriteListFromFireStore()
        Log.d("authorList", "$authorNameList")
        Log.d("authorquery", "$queryAuthorList")
        Log.d("viewmodel", "${viewModel.authorList.value}")
        Log.e("onResume", "#hyunsik")


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
                        val category = document.getString("category") ?: ""
                        val secondcategory = document.getString("secondCategory") ?: ""
                        val thirdcategory = document.getString("thirdCategory") ?: ""
                        binding.tvNick.text = "이름 : $nameField"
                        binding.tvFirstCategory.text = "선호 카테고리: $category"
                        binding.tvSecondCategory.text = " $secondcategory"
                        binding.tvThirdCategory.text = " $thirdcategory"


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

        // firebase에서 이미지 가져오기
        if (userUID != null) {
            val storage = FirebaseStorage.getInstance()
            var imgFileName = "IMAGE_$userUID.jpg"
            storage.reference.child("profiles")
                .child(imgFileName).downloadUrl.addOnSuccessListener {
                    selectedImageUri = it
                    binding.imgProfile.load(selectedImageUri) {
                        transformations(CircleCropTransformation())
                    }
                    Log.d("img", "이미지 가져오기 성공")
                }.addOnFailureListener {
                    Log.d("img", it.message.toString())
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFollowingAuthorListFromFireStore()
        getFavoriteListFromFireStore()
        initViewModel()


        Log.d("nameList", "${authorNameList}")
        loginViewModel.category.observe(requireActivity()) { text ->
            binding.tvFirstCategory.text = "선호 카테고리: $text"
        }

        loginViewModel.secondCategory.observe(requireActivity()) { text ->
            if (text.isNotEmpty()) {
                binding.tvSecondCategory.text = ", $text"
            } else {
                binding.tvSecondCategory.text = null
            }
        }

        loginViewModel.thirdCategory.observe(requireActivity()) { text ->
            if (text.isNotEmpty()) {
                binding.tvThirdCategory.text = ", $text"
            } else {
                binding.tvThirdCategory.text = null
            }
        }


        // RecyclerView 어댑터 초기화
        adapter = FavoriteListAdapter { item ->
            val detailInfo = item
            detailViewModel.setDetailInfo(detailInfo)
            val mainActivity = (activity as MainActivity)
            mainActivity.runDetailFragment()
        }

        //RecyclerView 설정 - 가로 방향
        val snapHelperForFavorite = PagerSnapHelper()//아이템 한 개씩 움직이게 도와주는 헬퍼
        snapHelperForFavorite.attachToRecyclerView(binding.favoriteList)// 리사이클러뷰 지정
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
        val snapHelperForFollowing = PagerSnapHelper()
        snapHelperForFollowing.attachToRecyclerView(binding.favoriteFollowList)// 리사이클러뷰 지정
        binding.favoriteFollowList.layoutManager =
            LinearLayoutManagerWrapper(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.favoriteFollowList.itemAnimator = null
        binding.favoriteFollowList.adapter = followingAdapter


        //setting 페이지로 이동
        binding.settingBtn.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        //프로필 이미지 올리기
        binding.imgProfile.setOnClickListener {
            Log.d("img", "start")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                when {
                    // 갤러리 접근 권한이 있는 경우
                    ContextCompat.checkSelfPermission(
                        requireActivity(),
                        permissionImage
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        Log.d("img", "getImage")
                        navigateGallery()
                    }
                    // 갤러리 접근 권한이 없는 경우
                    shouldShowRequestPermissionRationale(permissionImage)
                    -> {
                        Log.d("img", "showPermission")
                        showPermissionContextPopup(permissionImage)
                    }
                    // 권한 요청(requestPermissions) -> 갤러리 접근(onRequestPermissionResult)
                    else -> showPermissionContextPopup(permissionImage)
                }
            } else {
                when {
                    // 갤러리 접근 권한이 있는 경우
                    ContextCompat.checkSelfPermission(
                        requireActivity(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        Log.d("img", "getImage")
                        navigateGallery()
                    }
                    // 갤러리 접근 권한이 없는 경우
                    shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    -> {
                        Log.d("img", "showPermission")
                        showPermissionContextPopup(permissionList)
                    }
                    // 권한 요청(requestPermissions) -> 갤러리 접근(onRequestPermissionResult)
                    else -> showPermissionContextPopup(permissionList)
                }
            }

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
            if (binding.viewEmpty.visibility == View.INVISIBLE) {
                updateCategory()
            }
        }
    }

    private fun initViewModel() {
        with(viewModel) {

            list.observe(viewLifecycleOwner) {
                followingAdapter.submitList(it.toList())
            }
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

    private fun getFollowingAuthorListFromFireStore() {
        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user?.uid
        if (userUID != null) {
            val collectionRef = firestore.collection("User").document(userUID).collection("author")

            collectionRef.get().addOnSuccessListener { querySnapshot ->
                for (documentSnapshot in querySnapshot.documents) {
                    val fieldValue = documentSnapshot.getString("author")
                    if (fieldValue != null) {
                        //authorNameList.clear()
                        if (!authorNameList.contains(fieldValue)) {
                            authorNameList.add(fieldValue)
                        }
                    }
                }
                if (authorNameList.isEmpty()) return@addOnSuccessListener
                val randomAuthorName = authorNameList.random()
                viewModel.detailNews("$randomAuthorName 기자")
            }.addOnFailureListener { exception ->
                // 접근에 실패했을 때 수행할 작업
                authorNameList.clear()
            }
        }
    }

    // 로그인 화면으로 이동하는 함수
    private fun openLoginActivity(view: View) {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        checkLoginLauncher.launch(intent)
    }

    private fun updateCategory() {
        val curUser = auth.currentUser
        val user = User()
        val fbUser = firestore.collection("User").document(curUser?.uid ?: return)
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


    // 갤러리에서 사진 보는 함수
    private fun navigateGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageActivityResult.launch(intent)
    }

    // 권한부여 Dialog 생성
    private fun showPermissionContextPopup(permission: String) {
        AlertDialog.Builder(activity)
            .setTitle("권한을 부여해주세요")
            .setMessage("프로필 업로드를 위해 갤러리에 접근할 권한을 허용해주세요.")
            .setPositiveButton("권한 부여") { _, _ ->
                requestPermission.launch(permission)
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

    // firebase profile image upload
    private fun setFirebaseImage() {
        Log.d("img", "get user : $userUID")
        val storage = FirebaseStorage.getInstance()
        var imgFileName = "IMAGE_$userUID.jpg"
        var storageRef = storage.reference.child("profiles").child(imgFileName)
        //이미지 사이즈 줄이기
        val resizeImage = selectedImageUri?.let { convertResizeImage(it) }
        storageRef.putFile(resizeImage!!).addOnSuccessListener {
            Log.d("img", "이미지 업로드 성공")
        }.addOnFailureListener {
            Log.d("img", "이미지 업로드 실패")
            Toast.makeText(requireContext(), "이미지 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    //firebase profile image 지우기
    private fun deleteFirebaseImage(userUID: String) {
        val storage = FirebaseStorage.getInstance()
        var imgFileName = "IMAGE_$userUID.jpg"
        var storageRef = storage.reference.child("profiles").child(imgFileName)
        storageRef.delete().addOnSuccessListener {
            Log.d("img", "이미지 삭제 성공")
        }.addOnFailureListener {
            Log.d("img", "이미지 삭제 실패")
            Toast.makeText(requireContext(), "이미지 삭제를 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    //이미지 사이즈 줄이기
    private fun convertResizeImage(imageUri: Uri): Uri {
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
        val resizedBitmap =
            Bitmap.createScaledBitmap(bitmap, 80, 80, true)

        val byteArrayOutputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

        val tempFile = File.createTempFile("resized_image", ".jpg", requireContext().cacheDir)
        val fileOutputStream = FileOutputStream(tempFile)
        fileOutputStream.write(byteArrayOutputStream.toByteArray())
        fileOutputStream.close()

        return Uri.fromFile(tempFile)
    }


}

class LinearLayoutManagerWrapper : LinearLayoutManager {
    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}