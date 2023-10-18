package com.nbcamp_14_project.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import coil.load
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private var isAnimating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("detail_model_key", DetailInfo::class.java)
        } else {
            intent.getParcelableExtra("detail_model_key")
        }
        if (detailInfo != null) {
            viewModel.setDetailInfo(detailInfo)
        } else {
            Toast.makeText(applicationContext, "디테일 정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }

        viewModel.detailInfo.observe(this) { info ->
            binding.tvTitle.text = info.title
            binding.tvDate.text = info.pubDate
            binding.imgThumbnail.load(info.thumbnail)
            binding.tvDescription.text = info.description
            binding.tvName.text = info.author
        }


        val btnText = binding.btnText


        btnText.setOnClickListener {

            val url = viewModel.detailInfo.value?.originalLink


            if (url != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {

                Toast.makeText(this, "링크가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        initView()
    }

    private fun initView() = with(binding) {

        binding.imgBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_down);
        }
    }


}
