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
        start: Int? = null,
        sort: String? = null
    ): SearchEntity
    fun getList():List<HomeModel>
    fun getNewsList():List<HomeModel>
    //라이브데이터에 아이템 추가하는 기능
    fun addHeadLineItem(item: HomeModel?):List<HomeModel>
    fun addNewsItem(item:HomeModel?):List<HomeModel>
    //라이브데이터에서 아이템 삭제하는 기능
    fun removeHeadLineItem(item: HomeModel?):List<HomeModel>
    fun removeNewsItem(item: HomeModel?):List<HomeModel>
    fun removeLastNewsItem():List<HomeModel>
    fun modifyHeadLineItem(item: HomeModel?):List<HomeModel>
    fun modifyNewsItem(item: HomeModel?):List<HomeModel>
    fun modifyNewsItemIsLikeToLink(item: HomeModel?):List<HomeModel>
    fun clearNewsItems():List<HomeModel>
    fun clearHeadLineItems():List<HomeModel>

}

class MainFragmentRepositoryImpl(
    private val idGenerate: AtomicInteger,
    private val remoteDatasource: NewsCollector
) : MainFragmentRepository {
    private val list = mutableListOf<HomeModel>()
    private val newsList = mutableListOf<HomeModel>()
    override suspend fun getNews(query: String, display: Int?, start: Int?,sort:String?): SearchEntity =
        remoteDatasource.getNews(
            query,
            display,
            start,
            sort
        ).toSearchEntity()


    override fun getList(): List<HomeModel> {
        return list
    }

    override fun getNewsList(): List<HomeModel> {
        return newsList
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
    override fun modifyNewsItemIsLikeToLink(item: HomeModel?):List<HomeModel> {
        fun findIndex(item: HomeModel?):Int{
            if(item == null) return 0
            val findItem = newsList.find{
                it.link == item.link
            }
            return newsList.indexOf(findItem)
        }
        val findPosition = findIndex(item)
        if(findPosition < 0){
            return newsList
        }
        if(item == null) return newsList
        newsList[findPosition].isLike = item.isLike
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

    override fun removeLastNewsItem(): List<HomeModel> {
        if(newsList.isNotEmpty()){
            newsList.removeAt(newsList.lastIndex)
        }

        return ArrayList<HomeModel>(newsList)
    }


    override fun clearNewsItems(): List<HomeModel> {
        newsList.clear()
        return ArrayList<HomeModel>(newsList)
    }

    override fun clearHeadLineItems(): List<HomeModel> {
        list.clear()
        return ArrayList<HomeModel>(list)
    }


}