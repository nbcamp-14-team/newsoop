package com.nbcamp_14_project.search

import com.nbcamp_14_project.api.NewsCollector
import com.nbcamp_14_project.domain.SearchEntity
import com.nbcamp_14_project.domain.toSearchEntity
import com.nbcamp_14_project.home.HomeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger


interface SearchRepository {
    suspend fun getNews(
        query: String,
        display: Int? = null,
        start: Int? = null
    ): SearchEntity

    fun getList(): List<HomeModel>
    fun addNewsItem(item: HomeModel?): List<HomeModel>
    fun clearList(): List<HomeModel>
    fun getRecentSearchList(): List<String>
    fun addRecentSearchList(searchWord: String): List<String>
    fun removeRecentSearchItem(searchWord: String): List<String>
}

class SearchRepositoryImpl(
    private val idGenerate: AtomicInteger,
    private val remoteDatasource: NewsCollector
) : SearchRepository {
    private val searchList = mutableListOf<HomeModel>()
    private val recentSearchList = mutableListOf<String>()

    //시간이 오래 걸림 ->
    override suspend fun getNews(query: String, display: Int?, start: Int?): SearchEntity =
        withContext(Dispatchers.IO) {
            remoteDatasource.getNews(
                query,
                display,
                start
            ).toSearchEntity()
        }


    override fun getList(): List<HomeModel> {
        return searchList
    }

    override fun addNewsItem(item: HomeModel?): List<HomeModel> {
        if (item == null) {
            return searchList
        }
        searchList.add(item.copy(id = idGenerate.getAndIncrement()))
        return searchList
    }

    override fun clearList(): List<HomeModel> {
        searchList.clear()
        return ArrayList<HomeModel>(searchList)
    }

    override fun getRecentSearchList(): List<String> {
        return recentSearchList
    }

    override fun addRecentSearchList(searchWord: String): List<String> {
        if (searchWord == null || searchWord == "") {
            return recentSearchList
        }
        recentSearchList.add(searchWord)
        return recentSearchList
    }

    override fun removeRecentSearchItem(searchWord: String): List<String> {
        recentSearchList.remove(searchWord)
        return recentSearchList
    }
}