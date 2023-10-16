package com.nbcamp_14_project.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbcamp_14_project.home.HomeModel

class SearchViewModel : ViewModel() {
    private lateinit var newsList: ArrayList<HomeModel>
    private var _newsListResult: MutableLiveData<List<HomeModel>> =
        MutableLiveData<List<HomeModel>>()
    val newsListResult: LiveData<List<HomeModel>>
        get() = _newsListResult

    fun getNewsList(query: String) {
        newsList = arrayListOf()
        newsList = SearchRepositoryImpl().getList(query) as ArrayList<HomeModel>
        _newsListResult.value = newsList
    }
}