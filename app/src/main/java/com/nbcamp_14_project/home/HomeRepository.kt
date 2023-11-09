package com.nbcamp_14_project.home

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
    fun getHeadLineList():List<HomeModel>
    fun getNewsList():List<HomeModel>
    //라이브데이터에 아이템 추가하는 기능
    fun addHeadLineItem(item: HomeModel?):List<HomeModel>
    fun addNewsItem(item:HomeModel?):List<HomeModel>
    //라이브데이터에서 아이템 삭제하는 기능
    fun removeHeadLineItem(item: HomeModel?):List<HomeModel>
    fun removeNewsItem(item: HomeModel?):List<HomeModel>
    fun removeLoadingTypeItem():List<HomeModel>
    fun modifyHeadLineItem(item: HomeModel?):List<HomeModel>
    fun modifyNewsItem(item: HomeModel?):List<HomeModel>
    fun modifyNewsItemIsLikeToLink(item: HomeModel?):List<HomeModel>
    fun clearNewsItems():List<HomeModel>
    fun clearHeadLineItems():List<HomeModel>
    fun checkLastItem():Boolean

}

class MainFragmentRepositoryImpl(
    private val idGenerate: AtomicInteger,
    private val remoteDatasource: NewsCollector
) : MainFragmentRepository {
    private val headLineList = mutableListOf<HomeModel>()
    private val newsList = mutableListOf<HomeModel>()

    /**
     * API 쿼리문 보내주는 함수
     */
    override suspend fun getNews(query: String, display: Int?, start: Int?,sort:String?): SearchEntity =
        remoteDatasource.getNews(
            query,
            display,
            start,
            sort
        ).toSearchEntity()
    /**
     * 헤드라인 리스트 가져오는 함수
     */
    override fun getHeadLineList(): List<HomeModel> {
        return headLineList
    }
    /**
     * 뉴스 리스트 가져오는 함수
     */
    override fun getNewsList(): List<HomeModel> {
        return newsList
    }
    /**
     * 헤드라인 리스트에 아이템 추가하는 함수
     */
    override fun addHeadLineItem(item: HomeModel?):List<HomeModel> {
        if(item == null){
            return headLineList
        }
        headLineList.add(
            item.copy(
                id = idGenerate.getAndIncrement()
            )
        )

        return ArrayList<HomeModel>(headLineList)
    }
    /**
     * 뉴스 리스트에 아이템 추가하는 함수
     */
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
    /**
     * 헤드라인 리스트에 있는 아이템 수정하는 함수
     */
    override fun modifyHeadLineItem(item: HomeModel?):List<HomeModel> {
        fun findIndex(item: HomeModel?):Int{
            if(item == null) return 0
            val findItem = headLineList.find{
                it.id == item.id
            }
            return headLineList.indexOf(findItem)
        }
        val findPosition = findIndex(item)
        if(findPosition < 0){
            return headLineList
        }
        if(item == null) return headLineList
        headLineList[findPosition] = item
        return ArrayList<HomeModel>(headLineList)

    }
    /**
     * 뉴스 리스트에 있는 아이템 수정하는 함수
     */
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
    /**
     * 뉴스 리스트에 있는 아이템의 즐겨찾기 상태를 바꾸는 함수
     */
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
    /**
     * 헤드라인 리스트에 있는 아이템 삭제하는 함수
     */
    override fun removeHeadLineItem(item: HomeModel?):List<HomeModel> {
        fun findIndex(item: HomeModel?):Int{
            if(item == null) return 0
            val findItem = headLineList.find{
                it.id == item.id
            }
            return headLineList.indexOf(findItem)
        }
        val findPosition = findIndex(item)
        if(findPosition < 0){
            return headLineList
        }
        headLineList.removeAt(findPosition)
        return ArrayList<HomeModel>(headLineList)
    }
    /**
     * 뉴스 리스트에 있는 아이템 삭제하는 함수
     */
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
    /**
     * Loading타입 아이템 삭제하는 함수
     */
    override fun removeLoadingTypeItem(): List<HomeModel> {
        if(newsList.isNotEmpty()){
            fun findIndex():Int{
                val findItem = newsList.find{
                    it.viewType == 1
                }
                return newsList.indexOf(findItem)
            }
            val findPosition = findIndex()
            if(findPosition < 0) return ArrayList<HomeModel>(newsList)

            newsList.removeAt(findPosition)
        }
        return ArrayList<HomeModel>(newsList)
    }

    /**
     * 뉴스리스트 삭제하는 함수
     */
    override fun clearNewsItems(): List<HomeModel> {
        newsList.clear()
        return ArrayList<HomeModel>(newsList)
    }
    /**
     * 헤드라인 리스트 삭제하는 함수
     */
    override fun clearHeadLineItems(): List<HomeModel> {
        headLineList.clear()
        return ArrayList<HomeModel>(headLineList)
    }
    /**
     * 마지막 아이템의 뷰타입 확인하는 함수
     */
    override fun checkLastItem(): Boolean {
        if(headLineList.lastIndex<=0) return false
        return headLineList[headLineList.lastIndex].viewType != 0
    }


}