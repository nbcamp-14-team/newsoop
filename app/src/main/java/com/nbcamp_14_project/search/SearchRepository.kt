package com.nbcamp_14_project.search

import com.nbcamp_14_project.api.NewsCollector
import com.nbcamp_14_project.domain.SearchEntity
import com.nbcamp_14_project.domain.toSearchEntity
import com.nbcamp_14_project.home.HomeModel
import com.nbcamp_14_project.home.MainFragmentRepository
import java.util.concurrent.atomic.AtomicInteger

class SearchFragmentRepositoryImpl(
    private val idGenerate: AtomicInteger,
    private val remoteDatasource: NewsCollector
) : MainFragmentRepository {
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

    override fun addHeadLineItem(item: HomeModel?): List<HomeModel> {
        if (item == null) {
            return list
        }
        list.add(
            item.copy(
                id = idGenerate.getAndIncrement()
            )
        )

        return ArrayList<HomeModel>(list)
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

    override fun modifyHeadLineItem(item: HomeModel?): List<HomeModel> {
        fun findIndex(item: HomeModel?): Int {
            if (item == null) return 0
            val findItem = list.find {
                it.id == item.id
            }
            return list.indexOf(findItem)
        }

        val findPosition = findIndex(item)
        if (findPosition < 0) {
            return list
        }
        if (item == null) return list
        list[findPosition] = item
        return ArrayList<HomeModel>(list)

    }

    override fun modifyNewsItem(item: HomeModel?): List<HomeModel> {
        fun findIndex(item: HomeModel?): Int {
            if (item == null) return 0
            val findItem = newsList.find {
                it.id == item.id
            }
            return newsList.indexOf(findItem)
        }

        val findPosition = findIndex(item)
        if (findPosition < 0) {
            return newsList
        }
        if (item == null) return newsList
        newsList[findPosition] = item
        return ArrayList<HomeModel>(newsList)

    }

    override fun removeHeadLineItem(item: HomeModel?): List<HomeModel> {
        fun findIndex(item: HomeModel?): Int {
            if (item == null) return 0
            val findItem = list.find {
                it.id == item.id
            }
            return list.indexOf(findItem)
        }

        val findPosition = findIndex(item)
        if (findPosition < 0) {
            return list
        }
        list.removeAt(findPosition)
        return ArrayList<HomeModel>(list)
    }

    override fun removeNewsItem(item: HomeModel?): List<HomeModel> {
        fun findIndex(item: HomeModel?): Int {
            if (item == null) return 0
            val findItem = newsList.find {
                it.id == item.id
            }
            return newsList.indexOf(findItem)
        }

        val findPosition = findIndex(item)
        if (findPosition < 0) {
            return newsList
        }
        newsList.removeAt(findPosition)
        return ArrayList<HomeModel>(newsList)
    }

    override fun clearHeadLineItems(): List<HomeModel> {
        TODO("Not yet implemented")
    }

    override fun clearNewsItems(): List<HomeModel> {
        TODO("Not yet implemented")
    }
}