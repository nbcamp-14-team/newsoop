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
import com.nbcamp_14_project.R

import com.nbcamp_14_project.databinding.FragmentDetailBinding
import com.nbcamp_14_project.databinding.FragmentFavoriteBinding
import com.nbcamp_14_project.databinding.FragmentSearchBinding
import com.nbcamp_14_project.favorite.FavoriteListAdapter
import com.nbcamp_14_project.favorite.FavoriteViewModel
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        _binding = FragmentDetailBinding.bind(view)
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
            binding.tvDate.text = info.pubDate
            binding.imgThumbnail.load(info.thumbnail)
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

        imgLike.setOnClickListener {
            val detailInfo = viewModel.detailInfo.value
            if (detailInfo != null) {
                favoriteViewModel.addFavoriteItem(detailInfo)
            } else {

            }
        }




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

    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }


}



