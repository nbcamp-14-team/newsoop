package com.nbcamp_14_project.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {


    var list = mutableListOf<HomeFragmentTabs>()


    override fun getItemCount(): Int {
        return list.size
    }
    fun getTitle(position: Int):String{
        return list[position].titleRes
    }

    override fun createFragment(position: Int): Fragment {
        return list[position].fragment
    }
    fun setData(data: List<HomeFragmentTabs>){
        list = data.toMutableList()
        notifyDataSetChanged()
    }


}