package com.nbcamp_14_project.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.home.HomeModel

class FavoriteViewModel : ViewModel() {
    // ViewModel 클래스 정의

    private val _favoriteList: MutableLiveData<List<DetailInfo>> = MutableLiveData()
    val favoriteList: LiveData<List<DetailInfo>> get() = _favoriteList



    var isLogin: Boolean = false
    // 사용자 로그인 상태를 저장하는 변수

    // 즐겨찾기 아이템 추가
    fun addFavoriteItem(item: DetailInfo) {
        val currentList = _favoriteList.value?.toMutableList() ?: mutableListOf()
        // 현재의 즐겨찾기 목록을 가져오거나, 비어있는 리스트를 생성
        currentList.add(0, item) // 새로운 아이템을 목록의 맨 앞에 추가
        _favoriteList.value = currentList // 변경된 목록을 LiveData에 설정
    }



    // 즐겨찾기 아이템 삭제
    fun removeFavoriteItem(item: DetailInfo) {
        val currentList = _favoriteList.value?.toMutableList() ?: return
        // 현재의 즐겨찾기 목록을 가져오거나, 목록이 비어있으면 함수 종료
        if (currentList.contains(item)) {
            currentList.remove(item) // 목록에서 해당 아이템 삭제
            _favoriteList.value = currentList // 변경된 목록을 LiveData에 설정
        }
    }

    // 즐겨찾기 아이템을 지정된 위치에서 삭제
    fun removeFavoriteItemToPosition(item: DetailInfo) {
        val currentList = _favoriteList.value?.toMutableList() ?: return
        // 현재의 즐겨찾기 목록을 가져오거나, 목록이 비어있으면 함수 종료
        fun findIndex(item: DetailInfo?): Int {
            if (item == null) return 0
            // 입력된 아이템이 null이면 0 반환
            val findItem = currentList.find {
                it.thumbnail == item.thumbnail
            }
            // 현재 목록에서 아이템을 고유 식별자로 찾음
            return currentList.indexOf(findItem)
            // 아이템의 위치를 반환
        }
        val findPosition = findIndex(item)
        if (findPosition < 0) {
            return // 찾은 위치가 0 미만이면 함수 종료
        }
        if (item == null) return
        // 아이템이 null이면 함수 종료
        currentList.removeAt(findPosition)
        // 아이템을 찾은 위치에서 삭제
        _favoriteList.value = currentList
        // 변경된 목록을 LiveData에 설정
    }

    // 즐겨찾기 목록을 설정
    fun setFavoriteList(list: List<DetailInfo>) {
        _favoriteList.value = list
    }
}
