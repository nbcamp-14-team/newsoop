package com.nbcamp_14_project.detail


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel : ViewModel() {

    private val _detailInfo: MutableLiveData<DetailInfo> = MutableLiveData()
    val detailInfo: LiveData<DetailInfo> get() = _detailInfo
    fun setDetailInfo(detailInfo: DetailInfo) {
        _detailInfo.value = detailInfo
    }


}

