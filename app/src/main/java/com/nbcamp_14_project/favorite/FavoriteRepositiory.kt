package com.nbcamp_14_project.favorite

import com.nbcamp_14_project.api.NewsCollector
import com.nbcamp_14_project.domain.SearchEntity
import com.nbcamp_14_project.domain.toSearchEntity
import com.nbcamp_14_project.home.HomeModel
import com.nbcamp_14_project.home.MainFragmentRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

class FavoriteRepository(
    private val idGenerate: AtomicInteger,
    private val remoteDatasource: NewsCollector,
) {
    private val list = mutableListOf<HomeModel>()
   suspend fun getNews(query: String, display: Int?, start: Int?,sort:String?): SearchEntity =
        remoteDatasource.getNews(
            query,
            display,
            start,
            sort
        ).toSearchEntity()

    fun getList(): List<HomeModel> {
        return list
    }

    fun addNewsItem(item: HomeModel?): List<HomeModel> {
        if (item == null) {
            return list
        }
        list.add(item.copy(id = idGenerate.getAndIncrement()))
        return list
    }

    fun clearList(): List<HomeModel>{
        list.clear()
        return list
    }
}