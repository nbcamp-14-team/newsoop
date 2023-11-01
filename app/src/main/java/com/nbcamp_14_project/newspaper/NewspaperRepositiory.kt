package com.nbcamp_14_project.newspaper

import com.nbcamp_14_project.R
import com.nbcamp_14_project.api.NewsCollector
import com.nbcamp_14_project.domain.SearchEntity
import com.nbcamp_14_project.domain.toSearchEntity
import com.nbcamp_14_project.home.HomeModel
import java.util.concurrent.atomic.AtomicInteger


interface NewspaperRepository{
    fun getPoliticsList():List<NewspaperModel>
    fun getEconomyList():List<NewspaperModel>
    fun getSocietyList():List<NewspaperModel>
    fun getLifeListList():List<NewspaperModel>
    fun getCultureList():List<NewspaperModel>
    fun getItList():List<NewspaperModel>
    fun getScienceList():List<NewspaperModel>
    fun getWordList():List<NewspaperModel>
//    fun addNewspaper(item: NewspaperModel?):List<NewspaperModel>

}
class NewspaperRepositoryImpl(
    private val idGenerate: AtomicInteger
):NewspaperRepository {
    private val politicsList = mutableListOf(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "연합뉴스",
            R.drawable.ic_yna,
            "https://www.yna.co.kr/",
            "정치"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "이데일리",
            R.drawable.ic_edaily,
            "https://www.edaily.co.kr/",
            "정치"
        )
    )
    private val economyList = mutableListOf<NewspaperModel>(

    )
    private val societyList = mutableListOf<NewspaperModel>()

    private val lifeList = mutableListOf<NewspaperModel>()
    private val cultureList = mutableListOf<NewspaperModel>()
    private val itList = mutableListOf<NewspaperModel>()
    private val scienceList = mutableListOf<NewspaperModel>()
    private val wordList = mutableListOf<NewspaperModel>()

    init{

    }
    override fun getPoliticsList(): List<NewspaperModel> {
        return politicsList
    }

//    override fun addNewspaper(item: NewspaperModel?): List<NewspaperModel> {
//
//    }

    override fun getCultureList(): List<NewspaperModel> {
        return cultureList
    }

    override fun getItList(): List<NewspaperModel> {
        return itList
    }

    override fun getScienceList(): List<NewspaperModel> {
        return scienceList
    }

    override fun getWordList(): List<NewspaperModel> {
        return wordList
    }

    override fun getEconomyList(): List<NewspaperModel> {
        return economyList
    }

    override fun getSocietyList(): List<NewspaperModel> {
        return societyList
    }

    override fun getLifeListList(): List<NewspaperModel> {
        return lifeList
    }


//    override fun addNewspaper(item: NewspaperModel?):List<NewspaperModel> {
//        if(item == null){
//            return list
//        }
//        list.add(
//            item.copy(
//                id = idGenerate.getAndIncrement()
//            )
//        )
//
//        return ArrayList<NewspaperModel>(list)
//    }
}

