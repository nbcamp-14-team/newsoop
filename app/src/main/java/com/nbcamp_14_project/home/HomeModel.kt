package com.nbcamp_14_project.home

data class HomeModel (
    val id : Int? = -1,
    val title : String?, // 제목
    val description : String?, // 본문 요약
    val thumbnail: String? = null,// 기사 썸네일 이미지
    val author: String? = null, //기자 이름
    val isFollowing:Boolean = false,
    val link:String? = null,//원문 링크
    val pubDate:String? =null,//발간일자
)


