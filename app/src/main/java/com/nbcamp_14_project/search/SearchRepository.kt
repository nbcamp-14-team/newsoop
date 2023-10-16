package com.nbcamp_14_project.search

import android.util.Log
import com.nbcamp_14_project.api.NewsDTO
import com.nbcamp_14_project.api.RetrofitInstance
import com.nbcamp_14_project.home.HomeModel
import retrofit2.Call
import retrofit2.Response

interface SearchRepository {
    suspend fun getNews(
        query: String,
        display: Int? = null,
        start: Int? = null
    ): NewsDTO

    fun getList(query: String): List<HomeModel>

    //라이브데이터에 아이템 추가하는 기능
    fun addItem(item: HomeModel?): List<HomeModel>

    //라이브데이터에서 아이템 삭제하는 기능
    fun removeItem(item: HomeModel?): List<HomeModel>
    fun modifyItem(item: HomeModel?): List<HomeModel>
}

class SearchRepositoryImpl : SearchRepository {
    private val list = mutableListOf<HomeModel>()
    override suspend fun getNews(query: String, display: Int?, start: Int?): NewsDTO {
        TODO("Not yet implemented")
    }

    override fun getList(query: String): List<HomeModel> {
        val service = RetrofitInstance.search
        service.getNaverNewsSearch(query = query).enqueue(object : retrofit2.Callback<NewsDTO> {
            override fun onResponse(call: Call<NewsDTO>, response: Response<NewsDTO>) {
                if (response.isSuccessful) {
                    response.body()?.items?.forEach { item ->
                        var title = item.title ?: ""
                        title = title.replace("<b>", "")
                        title = title.replace("</b>", "")
                        title = title.replace("&quot;", "\"")
                        val description = item.description ?: ""
                        list.add(HomeModel(null, title, description))
                    }
                }
            }

            override fun onFailure(call: Call<NewsDTO>, t: Throwable) {
                Log.d("TAG", t.message.toString())
            }
        })
        return list
    }

    override fun addItem(item: HomeModel?): List<HomeModel> {
        TODO("Not yet implemented")
    }

    override fun removeItem(item: HomeModel?): List<HomeModel> {
        TODO("Not yet implemented")
    }

    override fun modifyItem(item: HomeModel?): List<HomeModel> {
        TODO("Not yet implemented")
    }
}