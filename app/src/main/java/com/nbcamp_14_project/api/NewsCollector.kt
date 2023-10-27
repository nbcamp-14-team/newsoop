package com.nbcamp_14_project.api

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsCollector {
    companion object {
        const val NEWS_URL = "https://openapi.naver.com/"
    }

    /**
     * api를 통해서 뉴스를 가져오는 명령어입니다.
     */
    @GET("v1/search/news.json")
    suspend fun getNews(
        @Query("query") query: String?,//검색어
        @Query("display") display: Int? = null,//출력개수
        @Query("start") start: Int? = null,//출력 시작점
        @Query("sort") sort: String? = null,//검색 결과 정렬 방법
    ): NewsDTO
}