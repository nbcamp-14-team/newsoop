package com.nbcamp_14_project.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcamp_14_project.Utils
import com.nbcamp_14_project.api.RetrofitInstance
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.home.HomeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.EOFException
import java.io.IOException
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.ZipException

class SearchViewModel(
    private val repository: SearchRepository
) : ViewModel() {
    private val _searchResultList: MutableLiveData<List<HomeModel>> = MutableLiveData()
    val searchResultList: LiveData<List<HomeModel>> get() = _searchResultList
    private val _recentSearchList: MutableLiveData<List<String>> = MutableLiveData()
    val recentSearchList: MutableLiveData<List<String>> get() = _recentSearchList

    fun clearAllItems() {
        _searchResultList.value = repository.clearList()
    }

    fun getRecentSearchList() {
        _recentSearchList.value = repository.getRecentSearchList()
    }

    fun removeRecentSearchItem(searchWord: String) {
        _recentSearchList.value = repository.removeRecentSearchItem(searchWord)
    }

    fun setRecentSearchItem(query: String) {
        _recentSearchList.value = repository.addRecentSearchList(query)
    }
    fun modifyFavoriteItemToPosition(item: DetailInfo){// DetailInfo 아이템 값 수정
        item.isLike = !item.isLike!!
        Log.d("search","search")
        val currentList = _searchResultList.value?.toMutableList() ?: return
        Log.d("searchCurrentList","$currentList")
        fun findIndex(item: DetailInfo?):Int{
            if(item == null) return 0
            val findItem = currentList.find{
                it.thumbnail == item.thumbnail
            }
            return currentList.indexOf(findItem!!)
        }
        val findPosition = findIndex(item)
        Log.d("findPosition","$findPosition")
        if(findPosition < 0){
            return
        }
        currentList[findPosition] = currentList[findPosition].copy(
            isLike = item.isLike
        )
        Log.d("searchList","${currentList[findPosition]}")
        _searchResultList.value = currentList

    }

    fun getSearchNews(query: String, display: Int, start: Int) {
        viewModelScope.launch {
            try {
                //docs item이 들어오는지 확인
                val docs = repository.getNews(query, display = display, start = start)
                val item = docs.items ?: return@launch
                for (i in item.indices) {//아이템 개수만큼 for문 실행
                    val thumbnail = getThumbnail(item[i].link.toString())
                    var title = item[i].title!!.replace("<b>", "")
                    Log.d("title", "$title")
                    title = title.replace("</b>", "")
                    title = title.replace("&quot;", "\"")
                    title = title.replace("&amp;", "&")
                    var description = item[i].description?.replace("<b>", "")
                    description = description?.replace("</b>", "")
                    description = description?.replace("&quot;", "\"")
                    val link = item[i].link
                    val pubDate = item[i].pubDate
                    val date = Date(pubDate)
                    val author = getAuthor(item[i].link.toString())
                    repository.addNewsItem(
                        HomeModel(
                            title = title,
                            thumbnail = thumbnail,
                            description = description,
                            link = link,
                            pubDate = date, //임의로 수정
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
                Log.d("authortest", "$author")
                author = Utils.getAuthorName(author)
                if (author == "") {
                    author = docs.select("em[class=media_end_head_journalist_name]")?.html()
                        .toString()//radioKorea에서 가져오는법
                    Log.d("authortest1", "$author")
                    author = Utils.getAuthorName(author)
                    if (author == "") {
                        author = docs.select("meta[property=dable:author]")?.attr("content")
                            .toString()//radioKorea에서 가져오는법
                        Log.d("authortest2", "$author")
                        author = Utils.getAuthorName(author)
                        if (author == "") {
                            author = docs.select("p[class=byline_p]").select("span").html()
                            Log.d("authortest3", "$author")
                            if (author?.length!!.toInt() > 3) {
                                val position = Utils.findStringIndex(author, "=")
                                if (position != null) {
                                    author = author?.substring(position + 1, position + 4)
                                }
                            }
                            if (author == "") {
                                author =
                                    docs.select("meta[property=og:article:author]")
                                        ?.attr("content")
                                Log.d("authortest4", "$author")
                                author = Utils.getAuthorName(author)
                                if (author == "") {
                                    author =
                                        docs.select("meta[property=dd:author]")?.attr("content")
                                    Log.d("authortest5", "$author")
                                    author = Utils.getAuthorName(author)
                                    if (author == "") {
                                        author =
                                            docs.select("div[class=journalist_name]")?.html()
                                        Log.d("authortest6", "$author")
                                        author = Utils.getAuthorName(author)
                                        if (author == "") {
                                            author = docs.select("meta[property=dd:author]")
                                                ?.attr("content")
                                            Log.d("authortest7", "$author")
                                            author = Utils.getAuthorName(author)
                                            if (author == "") {
                                                author =
                                                    docs.select("span[class=d_newsName]")
                                                        .html()
                                                Log.d("authortest8", "$author")
                                                author = Utils.getAuthorName(author)
                                                if (author == "") {
                                                    author =
                                                        docs.select("span[class=writer]")
                                                            .html()
                                                    Log.d("authortest9", "$author")
                                                    author = Utils.getAuthorName(author)
                                                    if (author == "") {
                                                        author =
                                                            docs.select("div[class=writer_info]")
                                                                .attr("span")
                                                        Log.d("authortest10", "$author")
                                                        author = Utils.getAuthorName(author)
                                                        if (author == "") {
                                                            author =
                                                                docs.select("p[class=wr]")
                                                                    .html()
                                                            Log.d(
                                                                "authortest11",
                                                                "$author"
                                                            )
                                                            author = Utils.getAuthorName(author)
                                                            if (author == "") {
                                                                author =
                                                                    docs.select("p[class=article_byline]")
                                                                        .attr("span")
                                                                Log.d(
                                                                    "authortest12",
                                                                    "$author"
                                                                )
                                                                author = Utils.getAuthorName(author)
                                                                if (author == "") {
                                                                    author =
                                                                        docs.select("span[class=name]")
                                                                            .html()
                                                                    Log.d(
                                                                        "authortest13",
                                                                        "$author"
                                                                    )
                                                                    author =
                                                                        Utils.getAuthorName(author)
                                                                }
                                                                if (author == "") {
//                                                                        author =
//                                                                            docs.select("meta[name=author]")
//                                                                                .attr("content")
                                                                    author =
                                                                        author?.replace(
                                                                            " ",
                                                                            ""
                                                                        )
                                                                    Log.d(
                                                                        "authortest14",
                                                                        "$author"
                                                                    )
                                                                    author =
                                                                        Utils.getAuthorName(author)
                                                                    if (author == "") {
                                                                        author =
                                                                            docs.select(
                                                                                "span[id=writeName]"
                                                                            )
                                                                                .html()
                                                                        Log.d(
                                                                            "authortest15",
                                                                            "$author"
                                                                        )
                                                                        author =
                                                                            Utils.getAuthorName(
                                                                                author
                                                                            )
                                                                        if (author == "") {
                                                                            author =
                                                                                docs.select(
                                                                                    "p[class=arvdate]"
                                                                                )
                                                                                    .attr(
                                                                                        "span"
                                                                                    )
                                                                            Log.d(
                                                                                "authortest16",
                                                                                "$author"
                                                                            )
                                                                            author =
                                                                                Utils.getAuthorName(
                                                                                    author
                                                                                )
                                                                            if (author == "") {
                                                                                author =
                                                                                    "기자 정보가 없습니다."
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
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
        } catch (e: IOException) {//IOException 에외처리
            e.printStackTrace()//디버깅 추적을 돕기위한 메서드
        } catch (e: EOFException) {
            Log.d("errAtZipEOFE", "$url")
        }
        return null
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