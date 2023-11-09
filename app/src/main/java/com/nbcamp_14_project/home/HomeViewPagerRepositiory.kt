package com.nbcamp_14_project.home

import android.os.Bundle
import android.util.Log
import java.util.concurrent.atomic.AtomicInteger

class HomeViewPagerRepository {
    private val list = mutableListOf<HomeFragmentTabs>()
    private val idGenerate =AtomicInteger()
    fun addListAtFront(query:String,title:String): MutableList<HomeFragmentTabs> {
        val homeFragment = HomeFragment()
        val bundle = Bundle()
        bundle.putString("query",query)
        homeFragment.arguments=bundle
        list.add(0, HomeFragmentTabs(homeFragment,title))
        return list
    }
    fun addFragment(query: String,title: String):MutableList<HomeFragmentTabs>{
        val homeFragment = HomeFragment()
        val bundle = Bundle()
        bundle.putString("query",query)
        homeFragment.arguments=bundle
        Log.d("idGenerate","$idGenerate")
        list.add(idGenerate.getAndIncrement(),HomeFragmentTabs(homeFragment,title))
        return list
    }
    fun removeListAtFront(): MutableList<HomeFragmentTabs> {
        list.removeAt(0)
        return list
    }
    fun getList(): MutableList<HomeFragmentTabs> {
        return list
    }
}