package com.nbcamp_14_project.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel(
    private val repository: DetailRepository
) : ViewModel() {
    // 1. ViewModel 클래스를 정의

    private val _detailInfo: MutableLiveData<DetailInfo> = MutableLiveData()
    // 2. _detailInfo라는 MutableLiveData를 생성 (DetailInfo 객체를 저장)
    val detailInfo: LiveData<DetailInfo> get() = _detailInfo
    // 3. detailInfo를 통해 _detailInfo의 LiveData 버전을 외부에 노출

    fun setDetailInfo(detailInfo: DetailInfo) {
        _detailInfo.value = repository.getDetailInfo()
    }
    // 4. setDetailInfo 메서드를 통해 _detailInfo에 DetailInfo 객체를 설정
}
