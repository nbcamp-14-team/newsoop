package com.nbcamp_14_project.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.home.HomeModel

class FavoriteViewModel : ViewModel() {

    private val _favoriteList: MutableLiveData<List<DetailInfo>> = MutableLiveData()
    val favoriteList: LiveData<List<DetailInfo>> get() = _favoriteList

    var isLogin: Boolean = false

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
    fun removeFavoriteItemToPosition(item: DetailInfo){
        val currentList = _favoriteList.value?.toMutableList() ?: return
        fun findIndex(item: DetailInfo?):Int{
            if(item == null) return 0
            val findItem = currentList.find{
                it.thumbnail == item.thumbnail
            }
            return currentList.indexOf(findItem)
        }
        val findPosition = findIndex(item)
        if(findPosition < 0){
            return
        }
        if(item == null) return
        currentList.removeAt(findPosition)
        _favoriteList.value = currentList

    }

    fun setFavoriteList(list: List<DetailInfo>) {
        _favoriteList.value = list
    }



}

