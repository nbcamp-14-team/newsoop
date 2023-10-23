package com.nbcamp_14_project.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbcamp_14_project.detail.DetailInfo

class FavoriteViewModel : ViewModel() {

    private val _favoriteList: MutableLiveData<List<DetailInfo>> = MutableLiveData()
    val favoriteList: LiveData<List<DetailInfo>> get() = _favoriteList

    fun addFavoriteItem(item: DetailInfo) {
        val currentList = _favoriteList.value?.toMutableList() ?: mutableListOf()
        currentList.add(0, item)
        _favoriteList.value = currentList
    }

    fun removeFavoriteItem(item: DetailInfo) {
        val currentList = _favoriteList.value?.toMutableList() ?: return
        if (currentList.contains(item)) {
            currentList.remove(item)
            _favoriteList.value = currentList
        }


    }

    fun setFavoriteList(list: List<DetailInfo>) {
        _favoriteList.value = list
    }






}

