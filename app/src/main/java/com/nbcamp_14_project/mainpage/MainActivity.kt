package com.nbcamp_14_project.mainpage

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nbcamp_14_project.R
import com.nbcamp_14_project.detail.DetailFragment
import com.nbcamp_14_project.databinding.ActivityMainBinding
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.ui.login.LoginActivity
import kotlinx.coroutines.selects.select

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

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

    fun runDetailFragment(){
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

        // Firebase Authentication 인스턴스 가져오기
        auth = Firebase.auth

        // 앱이 시작될 때 자동 로그아웃 실행
//        autoLogoutUser()

        with(binding) {


            //Viewpager2 adapter 설정
            viewpager.adapter = viewPagerAdapter
            viewpager.run {
                isUserInputEnabled = false
            }
            viewpager.offscreenPageLimit = viewPagerAdapter.itemCount//생명주기 관련 코드
            //TabLayout 연동
            val tabTitle = listOf("홈", "검색","토론방" ,"신문사별","즐겨찾기")
            val tabIcons = listOf<Int>(
                R.drawable.ic_home,
                R.drawable.ic_search,
                R.drawable.ic_debate,
                R.drawable.ic_news,
                R.drawable.ic_like,
            )


            TabLayoutMediator(tabLayout, viewpager) { tab, position ->
                tab.setText(tabTitle[position])
                tab.setIcon(
                    tabIcons[position]
                )
            }.attach()

            //TabLayout 아이콘 설정
//            tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
//                override fun onTabReselected(tab: TabLayout.Tab?) {
//
//                }
//                override fun onTabSelected(tab: TabLayout.Tab?) {
//                    when(tab?.position){
//                        0 ->tab.setIcon(selectedTabIcons[tab.position])
//                        1 ->tab.setIcon(selectedTabIcons[tab.position])
//                        2 ->tab.setIcon(selectedTabIcons[tab.position])
//                        3 ->tab.setIcon(selectedTabIcons[tab.position])
//                        4 ->tab.setIcon(selectedTabIcons[tab.position])
//                    }
//                }
//
//                override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//                }
//            })

        }

    }

    private fun autoLogoutUser() {
        // Firebase Authentication의 현재 사용자 확인
        val user = auth.currentUser

        if (user != null) {
            // 사용자가 로그인되어 있는 경우, 로그아웃 실행
            auth.signOut()


        }
    }


}