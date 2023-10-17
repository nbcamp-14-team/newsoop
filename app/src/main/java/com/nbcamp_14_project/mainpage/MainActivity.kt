package com.nbcamp_14_project.mainpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.nbcamp_14_project.detail.DetailActivity
import com.nbcamp_14_project.databinding.ActivityMainBinding
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    init {
        instance = this
    }

    companion object {
        private var instance: MainActivity? = null
        fun newInstance(): MainActivity? {
            return instance
        }
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) = with(binding) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //Viewpager2 adapter 설정
        viewpager.adapter = viewPagerAdapter
        viewpager.run {
            isUserInputEnabled = false
        }
        viewpager.offscreenPageLimit = viewPagerAdapter.itemCount//생명주기 관련 코드
        //TabLayout 연동
        val tabTitle = listOf("홈", "검색", "즐겨찾기")
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.setText(tabTitle[position])
        }.attach()
        btnGotoDetail.setOnClickListener {
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            val detailInfo = DetailInfo(
                title = "황성희 튜터님 부자 되세요!",
                description = "10월 마지막 주 로또 1등!",
                thumbnail = "https://pds.joongang.co.kr/news/component/htmlphoto_mmdata/202308/03/9f2025fe-1819-42a3-b5c1-13032da70bc8.jpg",
                originalLink = "https://www.joongang.co.kr/article/25182197#home",
                pubDate = "2023-10-17",
                author = "조광희"
            )

            intent.putExtra("detail_model_key", detailInfo)
            startActivity(intent)
        }
        btnGotoLogin.setOnClickListener {
            val test = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(test)
        }

    }


}