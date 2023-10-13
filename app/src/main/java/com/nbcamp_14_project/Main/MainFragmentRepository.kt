package com.nbcamp_14_project.Main

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
    fun getList():List<MainFragmentModel>
    //라이브데이터에 아이템 추가하는 기능
    fun addItem(item: MainFragmentModel?):List<MainFragmentModel>
    //라이브데이터에서 아이템 삭제하는 기능
    fun removeItem(item: MainFragmentModel?):List<MainFragmentModel>
    fun modifyItem(item: MainFragmentModel?):List<MainFragmentModel>
}

class MainFragmentRepositoryImpl(
    private val idGenerate: AtomicInteger,
    private val remoteDatasource: NewsCollector
) : MainFragmentRepository {
    private val list = mutableListOf<MainFragmentModel>()
    override suspend fun getNews(query: String, display: Int?, start: Int?): SearchEntity =
        remoteDatasource.getNews(
            query,
            display,
            start
        ).toSearchEntity()


    override fun getList(): List<MainFragmentModel> {
        list.addAll(
            arrayListOf<MainFragmentModel>().apply {
                for (i in 0 until 3) {
                    add(
                        MainFragmentModel(
                            idGenerate.getAndIncrement(),
                            "title $i",
                            "description $i"
                        )
                    )
                }
            }
        )
        Log.d("List","$list")
        return list
    }

    override fun addItem(item: MainFragmentModel?):List<MainFragmentModel> {
        if(item == null){
            return list
        }
        list.add(
            item.copy(
                id = idGenerate.getAndIncrement()
            )
        )

        return list
    }


    override fun modifyItem(item: MainFragmentModel?):List<MainFragmentModel> {
        fun findIndex(item: MainFragmentModel?):Int{
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
        return ArrayList<MainFragmentModel>(list)

    }

    override fun removeItem(item: MainFragmentModel?):List<MainFragmentModel> {
        fun findIndex(item: MainFragmentModel?):Int{
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
        return ArrayList<MainFragmentModel>(list)
    }
}