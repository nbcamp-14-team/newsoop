package com.nbcamp_14_project.search

import com.nbcamp_14_project.api.NewsCollector
import com.nbcamp_14_project.domain.SearchEntity
import com.nbcamp_14_project.domain.toSearchEntity
import com.nbcamp_14_project.home.HomeModel
import java.util.concurrent.atomic.AtomicInteger


interface SearchRepository {
    suspend fun getNews(
        query: String,
        display: Int? = null,
        start: Int? = null
    ): SearchEntity

    fun getList(): List<HomeModel>
    fun addNewsItem(item: HomeModel?): List<HomeModel>
}

class SearchFragmentRepositoryImpl(
    private val idGenerate: AtomicInteger,
    private val remoteDatasource: NewsCollector
) : SearchRepository {
    private val list = mutableListOf<HomeModel>()
    private val newsList = mutableListOf<HomeModel>()
    override suspend fun getNews(query: String, display: Int?, start: Int?): SearchEntity =
        remoteDatasource.getNews(
            query,
            display,
            start
        ).toSearchEntity()


    override fun getList(): List<HomeModel> {
        return list
    }

    override fun addNewsItem(item: HomeModel?): List<HomeModel> {
        if (item == null) {
            return newsList
        }
        newsList.add(
            item.copy(
                id = idGenerate.getAndIncrement()
            )
        )

        return ArrayList<HomeModel>(newsList)
    }
}