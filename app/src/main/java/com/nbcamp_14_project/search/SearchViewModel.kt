package com.nbcamp_14_project.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private lateinit var newsList: ArrayList<SearchModel>
    private var _newsListResult: MutableLiveData<List<SearchModel>> =
        MutableLiveData<List<SearchModel>>()
    val newsListResult: LiveData<List<SearchModel>>
        get() = _newsListResult

    fun getNewsList(query: String, start: Int) {
        newsList = arrayListOf()
        newsList = SearchRepositoryImpl().getList(query, start) as ArrayList<SearchModel>
        _newsListResult.value = newsList
    }
}