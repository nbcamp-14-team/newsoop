package com.nbcamp_14_project.home

class HomeViewPagerRepository {
    private val list = mutableListOf<HomeFragmentTabs>(
        HomeFragmentTabs(HomeFragment("정치 사회"),"정치/사회"),
        HomeFragmentTabs(HomeFragment("경제"),"경제"),
        HomeFragmentTabs(HomeFragment("생활 문화"),"생활/문화"),
        HomeFragmentTabs(HomeFragment("IT 과학"),"IT/과학"),
        HomeFragmentTabs(HomeFragment("세계"),"세계"),
    )
    fun addListAtFront(query:String,title:String): MutableList<HomeFragmentTabs> {
        list.add(0, HomeFragmentTabs(HomeFragment(query),title))
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