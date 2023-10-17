package com.nbcamp_14_project.mainpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.nbcamp_14_project.DetailActivity
import com.nbcamp_14_project.databinding.ActivityMainBinding
import com.nbcamp_14_project.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    init{
        instance = this
    }
    companion object{
        private var instance: MainActivity? = null
        fun newInstance(): MainActivity?{
            return instance
        }
    }
    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater) }
    private val viewPagerAdapter by lazy{
        MainViewPagerAdapter(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) = with(binding) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //Viewpager2 adapter 설정
        viewpager.adapter = viewPagerAdapter
        viewpager.run{
            isUserInputEnabled = false
        }
        viewpager.offscreenPageLimit = viewPagerAdapter.itemCount//생명주기 관련 코드
        //TabLayout 연동
        val tabTitle = listOf("홈","검색","즐겨찾기")
        TabLayoutMediator(tabLayout,viewpager){tab, position ->
            tab.setText(tabTitle[position])
        }.attach()
        btnGotoDetail.setOnClickListener{
            val test = Intent(this@MainActivity,DetailActivity::class.java)
            startActivity(test)
        }
        btnGotoLogin.setOnClickListener {
            val test = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(test)
        }

    }


}