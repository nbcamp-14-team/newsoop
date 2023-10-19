package com.nbcamp_14_project.home

import android.util.Log
import com.nbcamp_14_project.domain.SearchEntity
import com.nbcamp_14_project.api.NewsCollector
import com.nbcamp_14_project.domain.toSearchEntity
import java.util.concurrent.atomic.AtomicInteger

interface MainFragmentRepository {
    suspend fun getNews(
        query: String,
        display: Int? = null,
        start: Int? = null
    ): SearchEntity
    fun getList():List<HomeModel>
    //라이브데이터에 아이템 추가하는 기능
    fun addHeadLineItem(item: HomeModel?):List<HomeModel>
    fun addNewsItem(item:HomeModel?):List<HomeModel>
    //라이브데이터에서 아이템 삭제하는 기능
    fun removeHeadLineItem(item: HomeModel?):List<HomeModel>
    fun removeNewsItem(item: HomeModel?):List<HomeModel>
    fun modifyHeadLineItem(item: HomeModel?):List<HomeModel>
    fun modifyNewsItem(item: HomeModel?):List<HomeModel>
}

class MainFragmentRepositoryImpl(
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

    override fun addHeadLineItem(item: HomeModel?):List<HomeModel> {
        if(item == null){
            return list
        }
        list.add(
            item.copy(
                id = idGenerate.getAndIncrement()
            )
        )

        return ArrayList<HomeModel>(list)
    }
    override fun addNewsItem(item: HomeModel?):List<HomeModel> {
        if(item == null){
            return newsList
        }
        newsList.add(
            item.copy(
                id = idGenerate.getAndIncrement()
            )
        )

        return ArrayList<HomeModel>(newsList)
    }

    override fun modifyHeadLineItem(item: HomeModel?):List<HomeModel> {
        fun findIndex(item: HomeModel?):Int{
            if(item == null) return 0
            val findItem = list.find{
                it.id == item.id
            }
            return list.indexOf(findItem)
        }
        val findPosition = findIndex(item)
        if(findPosition < 0){
            return list
        }
        if(item == null) return list
        list[findPosition] = item
        return ArrayList<HomeModel>(list)

    }
    override fun modifyNewsItem(item: HomeModel?):List<HomeModel> {
        fun findIndex(item: HomeModel?):Int{
            if(item == null) return 0
            val findItem = newsList.find{
                it.id == item.id
            }
            return newsList.indexOf(findItem)
        }
        val findPosition = findIndex(item)
        if(findPosition < 0){
            return newsList
        }
        if(item == null) return newsList
        newsList[findPosition] = item
        return ArrayList<HomeModel>(newsList)

    }

    override fun removeHeadLineItem(item: HomeModel?):List<HomeModel> {
        fun findIndex(item: HomeModel?):Int{
            if(item == null) return 0
            val findItem = list.find{
                it.id == item.id
            }
            return list.indexOf(findItem)
        }
        val findPosition = findIndex(item)
        if(findPosition < 0){
            return list
        }
        list.removeAt(findPosition)
        return ArrayList<HomeModel>(list)
    }
    override fun removeNewsItem(item: HomeModel?):List<HomeModel> {
        fun findIndex(item: HomeModel?):Int{
            if(item == null) return 0
            val findItem = newsList.find{
                it.id == item.id
            }
            return newsList.indexOf(findItem)
        }
        val findPosition = findIndex(item)
        if(findPosition < 0){
            return newsList
        }
        newsList.removeAt(findPosition)
        return ArrayList<HomeModel>(newsList)
    }
}