package com.nbcamp_14_project.mainpage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nbcamp_14_project.FavoriteFragment
import com.nbcamp_14_project.SearchFragment
import com.nbcamp_14_project.Main.MainFragment

class MainViewPagerAdapter(fragmentActivity:FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    val list = listOf(MainFragment(), SearchFragment(), FavoriteFragment())

    override fun getItemCount(): Int {
        return list.size
    }
    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}