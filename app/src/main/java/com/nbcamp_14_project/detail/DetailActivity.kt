package com.nbcamp_14_project.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import coil.load
import com.nbcamp_14_project.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailInfo = intent.getSerializableExtra("detail_model_key") as? DetailInfo
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


        }
    }

}