package com.nbcamp_14_project.mainpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nbcamp_14_project.R
import com.nbcamp_14_project.detail.DetailFragment
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

    fun test(){
        val detailFragment = DetailFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.detailFragmentContainer, detailFragment)
        transaction.setReorderingAllowed(true)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {


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

            btnGotoLogin.setOnClickListener {
                val test = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(test)
            }

            btnGotoDetail.setOnClickListener {
                Firebase.auth.signOut()
            }
        }

    }


}