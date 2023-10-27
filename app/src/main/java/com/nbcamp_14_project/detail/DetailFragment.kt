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
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.R
import com.nbcamp_14_project.Utils

import com.nbcamp_14_project.databinding.FragmentDetailBinding
import com.nbcamp_14_project.databinding.FragmentFavoriteBinding
import com.nbcamp_14_project.databinding.FragmentSearchBinding
import com.nbcamp_14_project.favorite.FavoriteListAdapter
import com.nbcamp_14_project.favorite.FavoriteViewModel
import com.nbcamp_14_project.search.SearchListAdapter
import com.nbcamp_14_project.ui.login.LoginActivity
import com.nbcamp_14_project.ui.login.LoginViewModel
import java.util.Date
import java.util.Locale


class DetailFragment : Fragment(R.layout.fragment_detail) {

    companion object {

        fun newInstance() = DetailFragment()

    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by activityViewModels()
    private var isAnimating = false
    private lateinit var textToSpeech: TextToSpeech
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        _binding = FragmentDetailBinding.bind(view)


        val detailInfo = viewModel.detailInfo.value


        val isFavorite = favoriteViewModel.favoriteList.value?.contains(detailInfo) == true
        if (isFavorite) {
            binding.imgLike.setImageResource(R.drawable.ic_check)
        } else {
            binding.imgLike.setImageResource(R.drawable.ic_like)
        }


        binding.imgLike.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // 사용자가 로그인한 경우
                if (detailInfo != null) {
                    val isFavorite = favoriteViewModel.favoriteList.value?.contains(detailInfo) == true
                    if (isFavorite) {
                        favoriteViewModel.removeFavoriteItem(detailInfo)
                        binding.imgLike.setImageResource(R.drawable.ic_like)
                        removeFavoriteFromFireStore(detailInfo)  // Firestore에서도 제거
                    } else {
                        favoriteViewModel.addFavoriteItem(detailInfo)
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


        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.KOREA)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // 언어 지원되지 않는 경우 처리
                }
            }
        }


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
            binding.tvName.text = info.author
        }

        initView()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initView() = with(binding) {


//        val data =viewModel.detailInfo.value
//        binding.tvTitle.text = viewModel.detailInfo.value?.title
//        binding.tvDate.text = viewModel.detailInfo.value?.title
//        binding.imgThumbnail.load(data?.thumbnail)
//        binding.tvDescription.text = viewModel.detailInfo.value?.title
//        binding.tvName.text = viewModel.detailInfo.value?.author


        btnText.setOnClickListener {

            val url = viewModel.detailInfo.value?.originalLink


            if (url != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
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

    private fun initViewModel() {
        with(viewModel) {


        }

    }

    private fun addFavoriteToFireStore(detailInfo: DetailInfo) {
//        val db = FirebaseFirestore.getInstance()
//        val favoriteRef = db.collection("favorites")
//        val favoriteData = hashMapOf(
//            "title" to detailInfo.title,
//            "thumbnail" to detailInfo.thumbnail,
//            "description" to detailInfo.description,
//            "author" to detailInfo.author,
//            "originalLink" to detailInfo.originalLink,
//            "pubDate" to detailInfo.pubDate
//        )
//        favoriteRef.add(favoriteData)

        // 1. 사용자가 로그인한 후 사용자 UID 가져오기
        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user?.uid

        if (userUID != null) {
            // 2. Firestore에서 해당 사용자의 favorite 컬렉션 참조
            val db = FirebaseFirestore.getInstance()
            val favoriteCollection = db.collection("User").document(userUID).collection("favorites")
            val favoriteData = hashMapOf(
                "title" to detailInfo.title,
                "thumbnail" to detailInfo.thumbnail,
                "description" to detailInfo.description,
                "author" to detailInfo.author,
                "originalLink" to detailInfo.originalLink,
                "pubDate" to detailInfo.pubDate,
                "created" to Date()
            )

            favoriteCollection.add(favoriteData)
        } else {
//            Toast.makeText(requireContext(), "로그인을 해주세요".toString() )show()
        }

    }

    private fun removeFavoriteFromFireStore(detailInfo: DetailInfo) {
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

    }


    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }


}



