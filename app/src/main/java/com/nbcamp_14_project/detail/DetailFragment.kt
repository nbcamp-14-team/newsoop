package com.nbcamp_14_project.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.R
import com.nbcamp_14_project.Utils

import com.nbcamp_14_project.databinding.FragmentDetailBinding
import com.nbcamp_14_project.databinding.FragmentFavoriteBinding
import com.nbcamp_14_project.databinding.FragmentSearchBinding
import com.nbcamp_14_project.favorite.FavoriteListAdapter
import com.nbcamp_14_project.favorite.FavoriteViewModel
import com.nbcamp_14_project.home.HomeModelFactory
import com.nbcamp_14_project.home.HomeViewModel
import com.nbcamp_14_project.newspaper.NewspaperDialog
import com.nbcamp_14_project.search.SearchListAdapter
import com.nbcamp_14_project.search.SearchViewModel
import com.nbcamp_14_project.search.SearchViewModelFactory
import com.nbcamp_14_project.ui.login.LoginActivity
import com.nbcamp_14_project.ui.login.LoginViewModel
import java.util.Date
import java.util.Locale

class DetailFragment : Fragment(R.layout.fragment_detail) {

    companion object {
        // Fragment를 생성하기 위한 companion object
        fun newInstance() = DetailFragment()
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by activityViewModels()
    private var isAnimating = false
    private val fbFireStore = FirebaseFirestore.getInstance()
    private lateinit var textToSpeech: TextToSpeech
    private val favoriteViewModel: FavoriteViewModel by activityViewModels{
        FavoriteViewModel.FavoriteViewModelFactory()
    }

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(
            requireActivity(), SearchViewModelFactory()
        )[SearchViewModel::class.java]
    }
    private val fireStore = FirebaseFirestore.getInstance()
    var isLike:Boolean? = false
    var isFollow:Boolean? = false




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailBinding.bind(view)

        // Detail 정보 가져오기
        val detailInfo = viewModel.detailInfo.value
        Log.d("detailWork","$detailInfo")
        //즐겨찾기 여부 확인
        val isFavorite = favoriteViewModel.favoriteList.value?.contains(detailInfo) == true
        if (isFavorite) {
            binding.imgLike.setImageResource(R.drawable.ic_check)
        } else {
            binding.imgLike.setImageResource(R.drawable.ic_like)
        }


        // 즐겨찾기 버튼 클릭 리스너
        binding.imgLike.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // 사용자가 로그인한 경우
                if (detailInfo != null) {
                    val isFavorite =
                        favoriteViewModel.favoriteList.value?.contains(detailInfo) == true
                    if (isFavorite || isLike == true ) {
                        favoriteViewModel.removeFavoriteItem(detailInfo)
                        searchViewModel.modifyFavoriteItemToPosition(detailInfo)
                        binding.imgLike.setImageResource(R.drawable.ic_like)
                        removeFavoriteFromFireStore(detailInfo)  // Firestore에서도 제거
                    } else {
                        favoriteViewModel.addFavoriteItem(detailInfo)
                        searchViewModel.modifyFavoriteItemToPosition(detailInfo)
                        binding.imgLike.setImageResource(R.drawable.ic_check)
                        addFavoriteToFireStore(detailInfo)  // Firestore에도 추가
                    }
                }
            } else {
                // 사용자가 로그인하지 않은 경우
                Toast.makeText(requireContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }



        // 텍스트 음성 출력 설정
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.KOREA)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // 언어 지원되지 않는 경우 처리
                }
            }
        }

        // Detail 정보 변경 시 UI 업데이트
        viewModel.detailInfo.observe(viewLifecycleOwner) { info ->
            Log.d("info", "#hyunsik")
            binding.tvTitle.text = info.title
            var date = Date(info.pubDate)// 날짜로 변환
            val value = date.time?.let { Utils.calculationTime(it) }
            binding.tvDate.text = value
            Glide.with(this)
                .load(info.thumbnail)
                .centerCrop()
                .into(binding.imgThumbnail)
            binding.tvDescription.text = info.description
            binding.tvName.text = "${info.author} 기자"
        }

        initView()
        val collectionRef = fireStore.collection("User")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: return)
            .collection("favorites")
            .document(detailInfo?.title.toString())
        collectionRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    isLike = document.getBoolean("isLike")
                    Log.d("isLike??","$isLike")
                    if (isLike == true) {
                        binding.imgLike.setImageResource(R.drawable.ic_check)
                    } else {
                        binding.imgLike.setImageResource(R.drawable.ic_like)
                    }
                }
            }
            else{
                return@addOnCompleteListener
            }
        }
        val collectionRefToAuthor = fireStore.collection("User")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: return)
            .collection("author")
            .document(detailInfo?.author.toString())
        Log.d("test","${detailInfo!!.author.toString()}")
        collectionRefToAuthor.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    isFollow = document.getBoolean("isFollow")
                    Log.d("isFollow??","$isFollow")
                    if (isFollow == true) {
                        binding.tvFollowing.text = "구독중"
                    } else {
                        binding.tvFollowing.text = "구독하기"
                    }
                }
            }
            else{
                Log.d("isFollow?","false")
                return@addOnCompleteListener
            }
        }
        binding.tvFollowing.setOnClickListener {
            if (detailInfo != null && isFollow == false) {
                storeAuthorInFireStore(detailInfo)
                binding.tvFollowing.text = "구독중"
                Log.d("isFollowbtn1","$isFollow")
                isFollow = true
            }else if(isFollow == true){
                Log.d("isFollowbtn2","$isFollow")
                removeFollowingFromFireStore(detailInfo)
                binding.tvFollowing.text = "구독하기"
                isFollow = false
            }else{
                Toast.makeText(requireContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() = with(binding) {
        // 뷰 초기화 및 이벤트 핸들러 설정

        btnText.setOnClickListener {
            val url = viewModel.detailInfo.value?.originalLink
            if (url != null) {
                startActivity(NewspaperDialog.newInstance(requireContext(), url))
            } else {
                Toast.makeText(requireContext(), "링크가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        imgShare.setOnClickListener {
            val newsurl = viewModel.detailInfo.value?.originalLink
            val sendMessage = newsurl ?: "비디오 URL을 찾을 수 없습니다."
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, sendMessage)
            val shareIntent = Intent.createChooser(intent, "비디오 공유")
            startActivity(shareIntent)
        }

        imgMic.setOnClickListener {
            val descriptionText = tvDescription.text.toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(descriptionText, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                textToSpeech.speak(descriptionText, TextToSpeech.QUEUE_FLUSH, null)
            }
        }

    }

    private fun addFavoriteToFireStore(detailInfo: DetailInfo) {
        // Firestore에 즐겨찾기 추가
        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user?.uid

        if (userUID != null) {
            val db = FirebaseFirestore.getInstance()
            val favoriteCollection =
                db.collection("User").document(userUID).collection("favorites")
            val favoriteData = hashMapOf(
                "title" to detailInfo.title,
                "thumbnail" to detailInfo.thumbnail,
                "description" to detailInfo.description,
                "author" to detailInfo.author,
                "originalLink" to detailInfo.originalLink,
                "pubDate" to detailInfo.pubDate,
                "created" to Date(),
                "isLike" to detailInfo.isLike
            )

            favoriteCollection.document(detailInfo.title.toString()).set(favoriteData)
        }
    }

    private fun storeAuthorInFireStore(detailInfo: DetailInfo) {
        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user?.uid

        if (userUID != null) {
            val db = FirebaseFirestore.getInstance()
            val favoriteCollection = db.collection("User").document(userUID).collection("author")
            val favoriteData = hashMapOf(
                "author" to detailInfo.author,
                "isFollow" to true
            )

            favoriteCollection.document(detailInfo.author.toString()).set(favoriteData)
        }
    }
    private fun removeFollowingFromFireStore(detailInfo: DetailInfo) {
        // Firestore에서 즐겨찾기 삭제
        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user?.uid ?: return

        val db = FirebaseFirestore.getInstance()
        val authorCollection = db.collection("User").document(userUID).collection("author")
        val query = authorCollection.whereEqualTo("author", detailInfo.author)

        query.get().addOnSuccessListener { documents ->
            for (document in documents) {
                document.reference.delete()
            }
        }
    }
    private fun removeFavoriteFromFireStore(detailInfo: DetailInfo) {
        // Firestore에서 즐겨찾기 삭제
        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user?.uid ?: return

        val db = FirebaseFirestore.getInstance()
        val favoriteCollection = db.collection("User").document(userUID).collection("favorites")
        val query = favoriteCollection.whereEqualTo("title", detailInfo.title)

        query.get().addOnSuccessListener { documents ->
            for (document in documents) {
                document.reference.delete()
            }
        }
    }


    fun restoreFavoriteFromFireStore() {
        // Firestore에서 즐겨찾기 복원 (구현 필요)
    }

    override fun onDestroy() {
        // 화면 종료 시 TextToSpeech 정리
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }
}
