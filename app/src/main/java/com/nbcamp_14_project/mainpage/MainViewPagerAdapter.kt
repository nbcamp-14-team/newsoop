package com.nbcamp_14_project.mainpage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nbcamp_14_project.debate.DebateFragment
import com.nbcamp_14_project.favorite.FavoriteFragment
import com.nbcamp_14_project.home.HomeFragment
import com.nbcamp_14_project.home.HomeViewPagerFragment
import com.nbcamp_14_project.newspaper.NewspaperFragment
import com.nbcamp_14_project.search.SearchFragment

class MainViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    val list = listOf(
        HomeViewPagerFragment(),
        SearchFragment(),
        DebateFragment(),
        NewspaperFragment(),
        FavoriteFragment(),
    )

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}