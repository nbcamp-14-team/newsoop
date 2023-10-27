package com.nbcamp_14_project.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.nbcamp_14_project.favorite.FavoriteFragment
import com.nbcamp_14_project.search.SearchFragment

class HomeViewPagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    val list = listOf(HomeFragmentTabs(HomeFragment("정치"),"정치"),
        HomeFragmentTabs(HomeFragment("경제"),"경제"),
        HomeFragmentTabs(HomeFragment("사회"),"사회"),
        HomeFragmentTabs(HomeFragment("생활"),"생활"),
        HomeFragmentTabs(HomeFragment("문화"),"문화"),
        HomeFragmentTabs(HomeFragment("IT"),"IT"),
        HomeFragmentTabs(HomeFragment("과학"),"과학"),
        HomeFragmentTabs(HomeFragment("세계"),"세계"),
    )

    override fun getItemCount(): Int {
        return list.size
    }
    fun getTitle(position: Int):String{
        return list[position].titleRes
    }

    override fun createFragment(position: Int): Fragment {
        return list[position].fragment
    }


}