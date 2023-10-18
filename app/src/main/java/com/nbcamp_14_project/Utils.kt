package com.nbcamp_14_project

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

object Utils {
    //API 검색 함수, 파라미터 SearchWord = 검색 단어,display = 검색해올 뉴스 개수(최소 10 최대 100),start = 검색시작위치
//    fun getNews(searchWord: String, display: Int? = null, start: Int? = null) {
//        RetrofitInstance.api.getNews(
//            NewsCollector.ID,
//            NewsCollector.SECRET,
//            searchWord,
//            display,
//            start
//        )?.enqueue(object :
//            Callback<NewsDTO> {
//            override fun onResponse(call: Call<NewsDTO>, response: Response<NewsDTO>) {
//                if (response.isSuccessful) {
//                    Log.d("APiSuccess", "Success")
//                    val data = response.body()
//                    Log.d("test", "$data")
//                    val newsList = data?.items
//                    var testData = newsList!![0].description
//                    Log.d("newsList", "$newsList")
//                }
//            }
//
//            override fun onFailure(call: Call<NewsDTO>, t: Throwable) {
//                Log.d("APIfail", "Fail")
//            }
//        })
//    }
    suspend fun getThumbnail(url: String): String? {//썸네일 가져오기
        var thumbnail: String?
        withContext(Dispatchers.IO) {
            thumbnail = Jsoup.connect(url).get().select("meta[property=og:image]").attr("content")
            Log.d("success2", "$thumbnail")
        }
        return thumbnail
    }

    suspend fun getAuthor(url: String): String? {
        var author: String?
        withContext(Dispatchers.IO) {
            val docs = Jsoup.connect(url).get()
            author = docs.select("meta[name=dable:author]")?.attr("content")
                .toString()//radioKorea에서 가져오는법

            if (author == "") {
                author = docs.select("em[class=media_end_head_journalist_name]")?.html()
                Log.d("test", "$author")
                if (author == "") {

                    author = docs.select("meta[property=og:article:author]")?.attr("content")
                    Log.d("test1", "$author")


                }else if(author == ""){
                    author = docs.select("meta[property=og:article:author]")?.attr("content")
                    Log.d("test2", "$author")
                }else{
                    author = "기자 정보가 없습니다."
                }
            }
            Log.d("author", "$author")
        }
        if (author == null) return author
        return author
    }
}


//
//
//
//
//
//fun getThumbnail(url:String){
//    Log.d("success1","success1")
//    Thread{
////            val url = "https://www.yna.co.kr/view/AKR20231012007700079?section=international/all&site=major_news01"
//        val docs = Jsoup.connect(url).get()
//        Log.d("docs","$docs")
//        val test = docs.select("meta[property=og:image]").attr("content")
//        Log.d("select","$test")
//    }.start()
//}