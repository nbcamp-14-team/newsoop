package com.nbcamp_14_project.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPagerAdapter(fragment: FragmentActivity):FragmentStateAdapter(fragment) {
    var list = mutableListOf<HomeFragmentTabs>()
    private val listIds = list.map{it.hashCode().toLong()}
    override fun getItemCount(): Int {
        return list.size
    }
    fun getTitle(position: Int):String{
        return list[position].titleRes
    }

    override fun createFragment(position: Int): Fragment {
        return list[position].fragment
    }

    /**
     *  notifyDataSetChanged() 사용했는데 갱신 안되서 추가한 코드
     */

    override fun getItemId(position: Int): Long {
        return list[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return listIds.contains(itemId)
    }

    /**
     * 데이터 넣는 코드
     */

    fun setData(data: List<HomeFragmentTabs>){
        list = data.toMutableList()
        notifyDataSetChanged()
    }

}