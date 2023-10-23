package com.nbcamp_14_project.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcamp_14_project.api.RetrofitInstance
import com.nbcamp_14_project.domain.GetSearchNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.EOFException
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.ZipException

class MainFragmentViewModel(
    private val searchNews: GetSearchNewsUseCase,
    private val repository: MainFragmentRepositoryImpl
) : ViewModel() {
    private val _list: MutableLiveData<List<HomeModel>> = MutableLiveData()
    val list: LiveData<List<HomeModel>> get() = _list
    private val _newsList: MutableLiveData<List<HomeModel>> = MutableLiveData()
    val newsList: LiveData<List<HomeModel>> get() = _newsList

    init {
        _list.value = repository.getList()
    }

    fun headLineNews(query: String) {
        viewModelScope.launch {
            val docs = searchNews(query + "관련 뉴스", 5)
            val item = docs.items ?: return@launch
            for (i in item.indices) {//아이템 개수만큼 for문 실행
                val thumbnail = getThumbnail(item[i].link.toString())
                var title = item[i].title!!.replace("<b>", "")
                title = title.replace("</b>", "")
                title = title.replace("&quot;", "\"")
                var description = item[i].description?.replace("<b>", "")
                description = description?.replace("</b>", "")
                description = description?.replace("&quot;", "\"")
                val link = item[i].link
                Log.d("link", "$link")
                val pubDate = item[i].pubDate
                val author = getAuthor(item[i].link.toString())
                _list.value = repository.addHeadLineItem(
                    HomeModel(
                        title = title,
                        thumbnail = thumbnail,
                        description = description,
                        link = link,
                        pubDate = pubDate,
                        author = author,
                        viewType = 1
                    )
                )
            }
        }
    }

    fun detailNews(query: String, startingNum: Int? = null) {
        viewModelScope.launch {
            val docs = searchNews(query + "관련 뉴스", 5, startingNum)
            val item = docs.items ?: return@launch
            for (i in item.indices) {//아이템 개수만큼 for문 실행
                val thumbnail = getThumbnail(item[i].link.toString())
                var title = item[i].title!!.replace("<b>", "")
                title = title.replace("</b>", "")
                title = title.replace("&quot;", "\"")
                var description = item[i].description?.replace("<b>", "")
                description = description?.replace("</b>", "")
                description = description?.replace("&quot;", "\"")
                val link = item[i].link
                val pubDate = item[i].pubDate
                val author = getAuthor(item[i].link.toString())
                Log.d("linkRecycler", "$link + $author")
                _newsList.value = repository.addNewsItem(
                    HomeModel(
                        title = title,
                        thumbnail = thumbnail,
                        description = description,
                        link = link,
                        pubDate = pubDate,
                        author = author,
                        viewType = 0
                    )
                )
            }
        }
    }

    suspend fun getThumbnail(url: String): String? {//썸네일 가져오기
        Log.d("ERRR", "$url")
        var thumbnail: String?
        try {
            withContext(Dispatchers.IO) {
                val docs = Jsoup.connect(url).get()
                thumbnail = docs.select("meta[property=og:image]").attr("content")
                Log.d("success2", "$thumbnail")
            }
            if (thumbnail == null || thumbnail == "") return null
            return thumbnail
        } catch (e: ZipException) { //ZipException 예외처리
            Log.d("errorAtZip", "$url")
            return null
        } catch (e: IOException) {//IOException 에외처리
            e.printStackTrace()//디버깅 추적을 돕기위한 메서드
            return null
        } catch (e: EOFException) {
            Log.d("errAtZipEOFE", "$url")
            return null
        }

    }

    suspend fun getAuthor(url: String): String? {
        var author: String?
        try {
            withContext(Dispatchers.IO) {
                val docs = Jsoup.connect(url).get()
                author = docs.select("meta[name=dable:author]").attr("content")
                    .toString()//radioKorea에서 가져오는법

                if (author == "") {
                    author = docs.select("span[class=byline_s]").html()//네이버

                    Log.d("test", "$author")
                    if (author == "") {
                        author = docs.select("meta[property=og:article:author]").attr("content")
                        Log.d("test1", "$author")
                        if (author == "") {
                            author = docs.select("meta[property=article:author]").attr("content")
                            if (author == "") {
                                author = docs.select("meta[property=dd:author]").attr("content")
                                if (author == "") {
                                    author = docs.select("meta[name=twitter:creator]")
                                        .attr("content")//월간조선
                                    if (author == "") {
                                        author = docs.select("em[media_end_head_journalist_name]")
                                            .toString()//작동이 잘 안됨
                                        if (author == "") {
                                            author = docs.select("span[class=d_newsName]").html()
                                            if (author == "") {
                                                author = docs.select("span[class=writer]").html()
                                                if (author == "") {
                                                    author = "기자 정보가 없습니다."
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
                Log.d("author", "$author")
            }
            if (author == null) return author
            return author
        } catch (e: ZipException) { //ZipException 예외처리
            Log.d("errorAtZip", "$url")
            return null
        } catch (e: IOException) {//IOException 에외처리
            e.printStackTrace()//디버깅 추적을 돕기위한 메서드
            return null
        } catch (e: EOFException) {
            Log.d("errAtZipEOFE", "$url")
            return null
        }
    }

    fun clearAllItems() {//viewPager와 리사이클러뷰 리스트 초기화 함수
        _list.value = repository.clearHeadLineItems()
        _newsList.value = repository.clearNewsItems()
    }

    fun addHeadLineItem(item: HomeModel?) {//라이브데이터에 아이템 추가하는 기능
        _list.value = repository.addHeadLineItem(item)

    }

    fun addNewsItem(item: HomeModel?) {//라이브데이터에 아이템 추가하는 기능
        _newsList.value = repository.addNewsItem(item)

    }

    fun removeHeadLineItem(item: HomeModel?) {//라이브데이터에서 아이템 삭제하는 기능
        _list.value = repository.removeHeadLineItem(item)
    }

    fun removeNewsItem(item: HomeModel?) {//라이브데이터에서 아이템 삭제하는 기능
        _newsList.value = repository.removeNewsItem(item)
    }

    fun modifyHeadLineItem(item: HomeModel?) {
        _list.value = repository.modifyHeadLineItem(item)
    }

    fun modifyItem(item: HomeModel?) {
        _newsList.value = repository.modifyNewsItem(item)
    }
}

class MainFragmentModelFactory : ViewModelProvider.Factory {

    private val repository = MainFragmentRepositoryImpl(
        AtomicInteger(0),
        RetrofitInstance.search
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            return MainFragmentViewModel(
                GetSearchNewsUseCase(repository),
                repository
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }

    }
}