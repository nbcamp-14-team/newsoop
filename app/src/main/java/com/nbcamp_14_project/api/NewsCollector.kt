package com.nbcamp_14_project.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsCollector {
    companion object{
        const val NEWS_URL = "https://openapi.naver.com/"
        const val ID = "XFBo9h4LO1TqscFAuCKO"
        const val SECRET = "hi8mfNOW0V"
    }
    @GET("v1/search/news.json")
    fun getNews(
        @Header("X-Naver-Client-Id") id:String?,//앱 ID
        @Header("X-Naver-Client-Secret") secret:String?, // 앱 비밀번호
        @Query("query") query: String?,//검색어
        @Query("display") display: Int? = null,//출력개수
        @Query("start") start:Int? = null,//출력 시작점
    ): Call<NewsDTO>
}