package com.nbcamp_14_project.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcamp_14_project.Utils
import com.nbcamp_14_project.api.RetrofitInstance
import com.nbcamp_14_project.home.HomeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.EOFException
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.ZipException

class SearchViewModel(
    private val repository: SearchRepository
) : ViewModel() {
    private val _searchResultList: MutableLiveData<List<HomeModel>> = MutableLiveData()
    val searchResultList: LiveData<List<HomeModel>> get() = _searchResultList

    fun clearAllItems() {
        _searchResultList.value = repository.clearList()
    }

    fun getSearchNews(query: String, display: Int, start: Int) {
        viewModelScope.launch {
            try {
                //docs item이 들어오는지 확인
                val docs = repository.getNews(query, display = display, start = start)
                val item = docs.items ?: return@launch
                for (i in item.indices) {//아이템 개수만큼 for문 실행
                    val thumbnail = getThumbnail(item[i].link.toString())
                    var title = item[i].title?.replace("<b>", "") ?: "제목 없음"
                    title = title.replace("</b>", "")
                    title = title.replace("&quot;", "\"")
                    val description = item[i].description
                    val link = item[i].link
                    val pubDate = item[i].pubDate
                    val author = getAuthor(item[i].link.toString())
                    repository.addNewsItem(
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
                _searchResultList.value = repository.getList()
            } catch (e: Exception) {
                Log.d("getSearch", e.message.toString())
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
                author = docs.select("meta[name=dable:author]")?.attr("content")
                    .toString()//radioKorea에서 가져오는법

                if (author == "") {
                    author = docs.select("span[class=byline_s]")?.html()//네이버

                    Log.d("test", "$author")
                    if (author == "") {
                        author = docs.select("meta[property=og:article:author]")?.attr("content")
                        Log.d("test1", "$author")
                        if (author == "") {
                            author = docs.select("meta[property=article:author]")?.attr("content")
                            if (author == "") {
                                author = docs.select("meta[property=dd:author]")?.attr("content")
                                if (author == "") {
                                    author = docs.select("meta[name=twitter:creator]")
                                        ?.attr("content")//월간조선
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
}

class SearchViewModelFactory : ViewModelProvider.Factory {

    private val repository = SearchRepositoryImpl(
        AtomicInteger(0),
        RetrofitInstance.search
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }

    }
}