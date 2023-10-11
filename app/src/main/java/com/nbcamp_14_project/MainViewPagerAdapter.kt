package com.nbcamp_14_project

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(fragmentActivity:FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    val list = listOf(MainFragment(),SearchFragment(),FavoriteFragment())

    override fun getItemCount(): Int {
        return list.size
    }
    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}