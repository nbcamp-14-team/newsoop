package com.nbcamp_14_project.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.nbcamp_14_project.databinding.FragmentMainBinding
import com.nbcamp_14_project.databinding.FragmentViewpagerHomeBinding


class HomeViewPagerFragment:Fragment() {
    private var _binding: FragmentViewpagerHomeBinding? = null
    private val binding get() = _binding!!
    private val viewPagerAdapter by lazy{
        HomeViewPagerAdapter(requireActivity())
    }//test

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewpagerHomeBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            viewpager.adapter = viewPagerAdapter
            viewpager.run {
                isUserInputEnabled = false
            }
            viewpager.offscreenPageLimit = 3//생명주기 관련 코드
            val tabTitle = listOf("정치", "경제", "사회","생활","문화","IT","과학","세계")
            TabLayoutMediator(tabLayout, viewpager) { tab, position ->
                tab.setText(tabTitle[position])
            }.attach()
        }
    }
    //AutoScroll 적용

}