package com.nbcamp_14_project.detail

import androidx.lifecycle.LiveData

class DetailRepository {
    private var detailItem: DetailInfo? = null

    fun setDetailInfo(detailInfo: DetailInfo) {
        detailItem = detailInfo
    }
    fun getDetailInfo():DetailInfo?{
        return detailItem
    }
}