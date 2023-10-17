package com.nbcamp_14_project.search

import android.util.Log
import com.nbcamp_14_project.api.NewsDTO
import com.nbcamp_14_project.api.RetrofitInstance
import retrofit2.Call
import retrofit2.Response

interface SearchRepository {
    suspend fun getNews(
        query: String,
        display: Int? = null,
        start: Int? = null
    ): NewsDTO

    fun getList(query: String, start: Int): List<SearchModel>
    fun addItem(item: SearchModel?): List<SearchModel>
    fun removeItem(item: SearchModel?): List<SearchModel>
    fun modifyItem(item: SearchModel?): List<SearchModel>
}

class SearchRepositoryImpl : SearchRepository {
    private val list = mutableListOf<SearchModel>()
    override suspend fun getNews(query: String, display: Int?, start: Int?): NewsDTO {
        TODO("Not yet implemented")
    }

    override fun getList(query: String, start: Int): List<SearchModel> {
        val service = RetrofitInstance.search
        service.getNaverNewsSearch(query = query, start = start)
            .enqueue(object : retrofit2.Callback<NewsDTO> {
                override fun onResponse(call: Call<NewsDTO>, response: Response<NewsDTO>) {
                    Log.d("TAG", "query : {$query} , start : $start")
                    if (response.isSuccessful) {
                        response.body()?.items?.forEach { item ->
                            var title = item.title ?: ""
                            title = title.replace("<b>", "")
                            title = title.replace("</b>", "")
                            title = title.replace("&quot;", "\"")
                            val data = item.pubDate ?: ""
                            list.add(SearchModel(title, data))
                        }
                    }
                }

                override fun onFailure(call: Call<NewsDTO>, t: Throwable) {
                    Log.d("TAG", t.message.toString())
                }
            })
        return list
    }

    override fun addItem(item: SearchModel?): List<SearchModel> {
        TODO("Not yet implemented")
    }

    override fun removeItem(item: SearchModel?): List<SearchModel> {
        TODO("Not yet implemented")
    }

    override fun modifyItem(item: SearchModel?): List<SearchModel> {
        TODO("Not yet implemented")
    }

}