package com.nbcamp_14_project

import android.util.Log
import com.nbcamp_14_project.api.NewsCollector
import com.nbcamp_14_project.api.NewsDTO
import com.nbcamp_14_project.api.RetrofitInstance
import com.nbcamp_14_project.Main.MainFragmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    suspend fun getThumbnail(url: String):String? {
        var thumbnail: String?
        withContext(Dispatchers.IO){
            val docs = Jsoup.connect(url).get().select("meta[property=og:image]").attr("content")
            thumbnail = docs
            Log.d("success2", "$thumbnail")
        }
        return thumbnail
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