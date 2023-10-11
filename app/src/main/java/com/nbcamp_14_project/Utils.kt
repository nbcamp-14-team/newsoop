package com.nbcamp_14_project

import android.util.Log
import com.nbcamp_14_project.api.NewsCollector
import com.nbcamp_14_project.api.NewsDTO
import com.nbcamp_14_project.api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Utils {
    //API 검색 함수, 파라미터 SearchWord = 검색 단어,display = 검색해올 뉴스 개수(최소 10 최대 100),start = 검색시작위치
    fun getNews(searchWord:String,display:Int? = null,start:Int? = null){
        RetrofitInstance.api.getNews(NewsCollector.ID, NewsCollector.SECRET,searchWord,display,start)?.enqueue(object:
            Callback<NewsDTO> {
            override fun onResponse(call: Call<NewsDTO>, response: Response<NewsDTO>) {
                if(response.isSuccessful){
                    Log.d("APiSuccess","Success")
                    val data = response.body()
                    Log.d("test","$data")
                    val newsList = data?.items
                    var testData = newsList!![0].description
                    Log.d("newsList","$newsList")
                }
            }

            override fun onFailure(call: Call<NewsDTO>, t: Throwable) {
                Log.d("APIfail","Fail")
            }
        })
    }
}