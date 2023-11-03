package com.nbcamp_14_project.newspaper

import com.nbcamp_14_project.R
import java.util.concurrent.atomic.AtomicInteger


interface NewspaperRepository {
    fun getPoliticsList(): List<NewspaperModel>
    fun getEconomyList(): List<NewspaperModel>
    fun getSocietyList(): List<NewspaperModel>
    fun getLifeList(): List<NewspaperModel>
    fun getCultureList(): List<NewspaperModel>
    fun getItList(): List<NewspaperModel>
    fun getScienceList(): List<NewspaperModel>
    fun getWorldList(): List<NewspaperModel>
//    fun addNewspaper(item: NewspaperModel?):List<NewspaperModel>

}

class NewspaperRepositoryImpl(
    private val idGenerate: AtomicInteger
) : NewspaperRepository {
    private val politicsList = mutableListOf(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "네이버",
            R.drawable.logo_naver,
            "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100",
            "정치"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "다음",
            R.drawable.logo_daum,
            "https://news.daum.net/politics#1",
            "정치"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "연합뉴스",
            R.drawable.logo_yna,
            "https://www.yna.co.kr/politics/index?site=navi_politics_depth01",
            "정치"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "이데일리",
            R.drawable.logo_edaily,
            "https://www.edaily.co.kr/articles/economy/policy",
            "정치"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "중앙일보",
            R.drawable.logo_thejoongang,
            "https://www.joongang.co.kr/politics",
            "정치"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "주간조선",
            R.drawable.logo_chosun,
            "https://weekly.chosun.com/news/articleList.html?sc_section_code=S1N2&view_type=sm",
            "정치"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "YTN",
            R.drawable.logo_ytn,
            "https://m.ytn.co.kr/newslist/news_list.php?s_mcd=0101",
            "정치"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "뉴스원",
            R.drawable.logo_news1,
            "https://www.news1.kr/categories/?1",
            "정치"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "조선일보",
            R.drawable.logo_chosunews,
            "https://www.chosun.com/politics/",
            "정치"
        ),
    )
    private val economyList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "네이버",
            R.drawable.logo_naver,
            "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=101",
            "경제"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "다음",
            R.drawable.logo_daum,
            "https://news.daum.net/economic#1",
            "경제"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "연합뉴스",
            R.drawable.logo_yna,
            "https://www.yna.co.kr/economy/index?site=navi_economy_depth01",
            "경제"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "이데일리",
            R.drawable.logo_edaily,
            "https://www.edaily.co.kr/article/economy",
            "경제"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "중앙일보",
            R.drawable.logo_thejoongang,
            "https://www.joongang.co.kr/money",
            "경제"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "주간조선",
            R.drawable.logo_chosun,
            "https://weekly.chosun.com/news/articleList.html?sc_section_code=S1N3&view_type=sm",
            "경제"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "YTN",
            R.drawable.logo_ytn,
            "https://m.ytn.co.kr/newslist/news_list.php?s_mcd=0102",
            "경제"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "아시아경제",
            R.drawable.logo_asiae,
            "https://www.asiae.co.kr/list/economy",
            "경제"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "뉴스원",
            R.drawable.logo_news1,
            "https://www.news1.kr/categories/?13",
            "경제"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "조선일보",
            R.drawable.logo_chosunews,
            "https://www.chosun.com/investment/",
            "경제"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "한국경제",
            R.drawable.logo_koreae,
            "https://www.hankyung.com/",
            "경제"
        ),
    )
    private val societyList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),//오류있음
            "네이버",
            R.drawable.logo_naver,
            "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=102",
            "사회"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),//오류있음
            "다음",
            R.drawable.logo_daum,
            "https://news.daum.net/society#1",
            "사회"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "연합뉴스",
            R.drawable.logo_yna,
            "https://www.yna.co.kr/society/index?site=navi_society_depth01",
            "사회"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "중앙일보",
            R.drawable.logo_thejoongang,
            "https://www.joongang.co.kr/society",
            "사회"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "주간조선",
            R.drawable.logo_chosun,
            "https://weekly.chosun.com/news/articleList.html?sc_section_code=S1N4&view_type=sm",
            "사회"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "YTN",
            R.drawable.logo_ytn,
            "https://m.ytn.co.kr/newslist/news_list.php?mcd=0103",
            "사회"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "뉴스원",
            R.drawable.logo_news1,
            "https://www.news1.kr/categories/?7",
            "사회"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "조선일보",
            R.drawable.logo_chosunews,
            "https://www.chosun.com/national/",
            "사회"
        ),
    )
    private val lifeList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "네이버",
            R.drawable.logo_naver,
            "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=103",
            "생활"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "연합뉴스",
            R.drawable.logo_yna,
            "https://www.yna.co.kr/lifestyle/index?site=navi_lifestyle_depth01",
            "생활"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "중앙일보",
            R.drawable.logo_thejoongang,
            "https://www.joongang.co.kr/lifestyle",
            "생활"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "주간조선",
            R.drawable.logo_chosun,
            "https://weekly.chosun.com/news/articleList.html?sc_section_code=S1N5&view_type=sm",
            "생활"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "조선일보",
            R.drawable.logo_chosunews,
            "https://www.chosun.com/medical/",
            "생활"
        ),

        )
    private val cultureList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "연합뉴스",
            R.drawable.logo_yna,
            "https://www.yna.co.kr/culture/index?site=navi_culture_depth01",
            "문화"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "중앙일보",
            R.drawable.logo_thejoongang,
            "https://www.joongang.co.kr/culture",
            "문화"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "주간조선",
            R.drawable.logo_chosun,
            "https://weekly.chosun.com/news/articleList.html?sc_section_code=S1N5&view_type=sm",
            "문화"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "다음",
            R.drawable.logo_daum,
            "https://news.daum.net/culture#1",
            "문화"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "YTN",
            R.drawable.logo_ytn,
            "https://m.ytn.co.kr/newslist/news_list.php?mcd=0106",
            "문화"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "조선일보",
            R.drawable.logo_chosunews,
            "https://www.chosun.com/culture-style/",
            "문화"
        ),

        )
    private val itList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "이데일리",
            R.drawable.logo_edaily,
            "https://www.edaily.co.kr/",
            "IT"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "중앙일보",
            R.drawable.logo_thejoongang,
            "https://www.joongang.co.kr/money/science",
            "IT"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "주간조선",
            R.drawable.logo_chosun,
            "https://weekly.chosun.com/news/articleList.html?sc_section_code=S1N7&view_type=sm",
            "IT"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "다음",
            R.drawable.logo_daum,
            "https://news.daum.net/digital#1",
            "IT"
        ),

    )
    private val scienceList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "주간조선",
            R.drawable.logo_chosun,
            "https://weekly.chosun.com/news/articleList.html?sc_section_code=S1N7&view_type=sm",
            "과학"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "네이버",
            R.drawable.logo_naver,
            "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=105",
            "과학"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "YTN",
            R.drawable.logo_ytn,
            "https://m.ytn.co.kr/newslist/news_list.php?mcd=0105",
            "과학"
        ),
    )
    private val worldList = mutableListOf<NewspaperModel>(
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "연합뉴스",
            R.drawable.logo_yna,
            "https://www.yna.co.kr/international/index?site=navi_international_depth01",
            "세계"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "중앙일보",
            R.drawable.logo_thejoongang,
            "https://www.joongang.co.kr/world",
            "세계"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "주간조선",
            R.drawable.logo_chosun,
            "https://weekly.chosun.com/news/articleList.html?sc_section_code=S1N6&view_type=sm",
            "세계"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "네이버",
            R.drawable.logo_naver,
            "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=104",
            "세계"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "다음",
            R.drawable.logo_daum,
            "https://news.daum.net/foreign#1",
            "세계"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "YTN",
            R.drawable.logo_ytn,
            "https://m.ytn.co.kr/newslist/news_list.php?s_mcd=0104",
            "세계"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "뉴스원",
            R.drawable.logo_news1,
            "https://www.news1.kr/categories/?31",
            "세계"
        ),
        NewspaperModel(
            idGenerate.getAndIncrement(),
            "조선일보",
            R.drawable.logo_chosunews,
            "https://www.chosun.com/international/",
            "세계"
        ),
    )


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

