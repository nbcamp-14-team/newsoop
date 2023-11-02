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
    fun getLifeList():List<NewspaperModel>
    fun getCultureList():List<NewspaperModel>
    fun getItList():List<NewspaperModel>
    fun getScienceList():List<NewspaperModel>
    fun getWorldList():List<NewspaperModel>
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
            "https://www.yna.co.kr/politics/index",
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
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "연합뉴스",
            R.drawable.ic_yna,
            "https://www.yna.co.kr/economy/index?site=navi_economy_depth01",
            "경제"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "이데일리",
            R.drawable.ic_edaily,
            "https://www.edaily.co.kr/",
            "경제"
        )
    )
    private val societyList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "연합뉴스",
            R.drawable.ic_yna,
            "https://www.yna.co.kr/society/index?site=navi_society_depth01",
            "사회"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "이데일리",
            R.drawable.ic_edaily,
            "https://www.edaily.co.kr/",
            "사회"
        )
    )
    private val lifeList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "연합뉴스",
            R.drawable.ic_yna,
            "https://www.yna.co.kr/lifestyle/index?site=navi_lifestyle_depth01",
            "생활"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "이데일리",
            R.drawable.ic_edaily,
            "https://www.edaily.co.kr/",
            "생활"
        )
    )
    private val cultureList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "연합뉴스",
            R.drawable.ic_yna,
            "https://www.yna.co.kr/culture/index?site=navi_culture_depth01",
            "문화"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "이데일리",
            R.drawable.ic_edaily,
            "https://www.edaily.co.kr/",
            "문화"
        )

    )
    private val itList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "이데일리",
            R.drawable.ic_edaily,
            "https://www.edaily.co.kr/",
            "IT"
        )
    )
    private val scienceList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "이데일리",
            R.drawable.ic_edaily,
            "https://www.edaily.co.kr/",
            "과학"
        )
    )
    private val worldList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "연합뉴스",
            R.drawable.ic_yna,
            "https://www.yna.co.kr/international/index?site=navi_international_depth01",
            "세계"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "이데일리",
            R.drawable.ic_edaily,
            "https://www.edaily.co.kr/",
            "세계"
        )
    )

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

    override fun getWorldList(): List<NewspaperModel> {
        return worldList
    }

    override fun getEconomyList(): List<NewspaperModel> {
        return economyList
    }

    override fun getSocietyList(): List<NewspaperModel> {
        return societyList
    }

    override fun getLifeList(): List<NewspaperModel> {
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

