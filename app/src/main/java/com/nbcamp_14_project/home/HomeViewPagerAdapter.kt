package com.nbcamp_14_project.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.nbcamp_14_project.favorite.FavoriteFragment
import com.nbcamp_14_project.search.SearchFragment

class HomeViewPagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    val list = listOf(HomeFragment("정치"),
        HomeFragment("경제"),
        HomeFragment("사회"),
        HomeFragment("생활"),
        HomeFragment("문화"),
        HomeFragment("IT"),
        HomeFragment("과학"),
        HomeFragment("세계")
    )

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }


}