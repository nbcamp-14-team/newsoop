package com.nbcamp_14_project.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcamp_14_project.domain.GetSearchNewsUseCase
import com.nbcamp_14_project.Utils
import com.nbcamp_14_project.api.RetrofitInstance
import com.nbcamp_14_project.detail.DetailInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.EOFException
import java.io.IOException
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.ZipException

class HomeViewModel(
    private val searchNews: GetSearchNewsUseCase, private val repository: MainFragmentRepositoryImpl
) : ViewModel() {
    private val _list: MutableLiveData<List<HomeModel>> = MutableLiveData()
    val list: LiveData<List<HomeModel>> get() = _list
    private val _newsList: MutableLiveData<List<HomeModel>> = MutableLiveData()
    val newsList: LiveData<List<HomeModel>> get() = _newsList
    var isLoading: Boolean = false
    var category: String? = null

    init {
        _list.value = repository.getList()
    }

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    fun headLineNews(query: String, display: Int? = 5) {
        viewModelScope.launch {
            val docs = searchNews(query, display, sort = "sim")
            val item = docs.items ?: return@launch
            var currentList = repository.getNewsList()
            for (i in item.indices) {//아이템 개수만큼 for문 실행
                val thumbnail = getThumbnail(item[i].link.toString())
                var title = item[i].title!!.replace("<b>", "")
                title = title.replace("</b>", "")
                title = title.replace("&quot;", "\"")
                title = title.replace("&amp;", "&")
                var description = item[i].description?.replace("<b>", "")
                description = description?.replace("</b>", "")
                description = description?.replace("&quot;", "\"")
                val link = item[i].link
                val pubDate = item[i].pubDate
                var date = Date(pubDate)
                date = date
                val author = getAuthor(item[i].link.toString())
                _list.value = repository.addHeadLineItem(
                    HomeModel(
                        title = title,
                        thumbnail = thumbnail,
                        description = description,
                        link = link,
                        pubDate = date,
                        author = author,
                        viewType = 1
                    )
                )

            }

        }
    }

    fun detailRecommendNews(query: String, startingNum: Int? = null, display: Int? = 5) {
        viewModelScope.launch {
            val docs = searchNews(query, display, startingNum?.plus(5), sort = "sim")
            val item = docs.items ?: return@launch
            var currentList = repository.getNewsList()
            for (i in item.indices) {//아이템 개수만큼 for문 실행
                val thumbnail = getThumbnail(item[i].link.toString())
                var title = item[i].title!!.replace("<b>", "")
                title = title.replace("</b>", "")
                title = title.replace("&quot;", "\"")
                title = title.replace("&amp;", "&")
                var description = item[i].description?.replace("<b>", "")
                description = description?.replace("</b>", "")
                description = description?.replace("&quot;", "\"")
                val link = item[i].link
                val pubDate = item[i].pubDate
                var date = Date(pubDate)
                val author = getAuthor(item[i].link.toString())
                currentList = repository.addNewsItem(
                    HomeModel(
                        title = title,
                        thumbnail = thumbnail,
                        description = description,
                        link = link,
                        pubDate = date,
                        author = author,
                        viewType = VIEW_TYPE_ITEM
                    )
                )

            }
            _newsList.value = currentList

        }
    }

    fun detailNews(query: String, startingNum: Int? = null, display: Int? = 5) {
        viewModelScope.launch {
            val docs = searchNews(query, display, startingNum?.plus(5), sort = "sim")
            val item = docs.items ?: return@launch
            var currentList = repository.getNewsList()
            for (i in item.indices) {//아이템 개수만큼 for문 실행
                val thumbnail = getThumbnail(item[i].link.toString())
                var title = item[i].title!!.replace("<b>", "")
                title = title.replace("</b>", "")
                title = title.replace("&quot;", "\"")
                title = title.replace("&amp;", "&")
                var description = item[i].description?.replace("<b>", "")
                description = description?.replace("</b>", "")
                description = description?.replace("&quot;", "\"")
                val link = item[i].link
                val pubDate = item[i].pubDate
                var date = Date(pubDate)
                val author = getAuthor(item[i].link.toString())
                currentList = repository.addNewsItem(
                    HomeModel(
                        title = title,
                        thumbnail = thumbnail,
                        description = description,
                        link = link,
                        pubDate = date,
                        author = author,
                        viewType = VIEW_TYPE_ITEM
                    )
                )
            }
            currentList = repository.addNewsItem(
                HomeModel(
                    viewType = VIEW_TYPE_LOADING
                )
            )
            _newsList.value = currentList
        }
    }

    fun detailNewsInfinity(
        query: String
        /** 검색어 */
        , startingNum: Int? = null
        /** 토큰 */
    ) {
        viewModelScope.launch {

            val docs = searchNews(query, 10, startingNum, sort = "sim")
            val item = docs.items ?: return@launch
            var currentList = repository.getNewsList()
            currentList = repository.removeLastNewsItem()
            for (i in item.indices) {//아이템 개수만큼 for문 실행
                val thumbnail = getThumbnail(item[i].link.toString())
                var title = item[i].title!!.replace("<b>", "")
                title = title.replace("</b>", "")
                title = title.replace("&quot;", "\"")
                title = title.replace("&amp;", "&")
                var description = item[i].description?.replace("<b>", "")
                description = description?.replace("</b>", "")
                description = description?.replace("&quot;", "\"")
                val link = item[i].link
                val pubDate = item[i].pubDate
                var date = Date(pubDate)
                val author = getAuthor(item[i].link.toString())
                currentList = repository.addNewsItem(
                    HomeModel(
                        title = title,
                        thumbnail = thumbnail,
                        description = description,
                        link = link,
                        pubDate = date,
                        author = author,
                        viewType = VIEW_TYPE_ITEM
                    )
                )
            }
            currentList = repository.addNewsItem(
                HomeModel(
                    viewType = VIEW_TYPE_LOADING
                )
            )
            _newsList.value = currentList
            isLoading = true
        }
    }

    fun detailNewsInfinityToRecommend(
        firstCategory: String?,
        secondCategory: String?,
        thirdCategory: String?,
        startingNum: Int? = null
        /** 토큰 */
    ) {
        viewModelScope.launch {
            val docs = searchNews(firstCategory ?: "생활", 3, startingNum, sort = "sim")
            val item = docs.items ?: return@launch
            var currentList = repository.getNewsList()
            currentList = repository.removeLastNewsItem()
            for (i in item.indices) {//아이템 개수만큼 for문 실행
                val thumbnail = getThumbnail(item[i].link.toString())
                var title = item[i].title!!.replace("<b>", "")
                title = title.replace("</b>", "")
                title = title.replace("&quot;", "\"")
                title = title.replace("&amp;", "&")
                var description = item[i].description?.replace("<b>", "")
                description = description?.replace("</b>", "")
                description = description?.replace("&quot;", "\"")
                val link = item[i].link
                val pubDate = item[i].pubDate
                var date = Date(pubDate)
                Log.d("date", "$date")
                val author = getAuthor(item[i].link.toString())
                Log.d("linkRecycler", "$link + $author")
                currentList = repository.addNewsItem(
                    HomeModel(
                        title = title,
                        thumbnail = thumbnail,
                        description = description,
                        link = link,
                        pubDate = date,
                        author = author,
                        viewType = VIEW_TYPE_ITEM
                    )
                )

            }
            val secondDocs = searchNews(secondCategory ?: "생활", 3, startingNum, sort = "sim")
            val secondItem = secondDocs.items ?: return@launch
            for (i in secondItem.indices) {//아이템 개수만큼 for문 실행
                val thumbnail = getThumbnail(secondItem[i].link.toString())
                var title = secondItem[i].title!!.replace("<b>", "")
                title = title.replace("</b>", "")
                title = title.replace("&quot;", "\"")
                title = title.replace("&amp;", "&")
                var description = secondItem[i].description?.replace("<b>", "")
                description = description?.replace("</b>", "")
                description = description?.replace("&quot;", "\"")
                val link = secondItem[i].link
                val pubDate = secondItem[i].pubDate
                var date = Date(pubDate)
                Log.d("date", "$date")
                val author = getAuthor(secondItem[i].link.toString())
                Log.d("linkRecycler", "$link + $author")
                currentList = repository.addNewsItem(
                    HomeModel(
                        title = title,
                        thumbnail = thumbnail,
                        description = description,
                        link = link,
                        pubDate = date,
                        author = author,
                        viewType = VIEW_TYPE_ITEM
                    )
                )

            }
            val thirdDocs = searchNews(thirdCategory ?: "생활", 3, startingNum, sort = "sim")
            val thirdItem = thirdDocs.items ?: return@launch
            for (i in thirdItem.indices) {//아이템 개수만큼 for문 실행
                val thumbnail = getThumbnail(thirdItem[i].link.toString())
                var title = thirdItem[i].title!!.replace("<b>", "")
                title = title.replace("</b>", "")
                title = title.replace("&quot;", "\"")
                title = title.replace("&amp;", "&")
                var description = thirdItem[i].description?.replace("<b>", "")
                description = description?.replace("</b>", "")
                description = description?.replace("&quot;", "\"")
                val link = thirdItem[i].link
                val pubDate = thirdItem[i].pubDate
                var date = Date(pubDate)
                Log.d("date", "$date")
                val author = getAuthor(thirdItem[i].link.toString())
                Log.d("linkRecycler", "$link + $author")
                currentList = repository.addNewsItem(
                    HomeModel(
                        title = title,
                        thumbnail = thumbnail,
                        description = description,
                        link = link,
                        pubDate = date,
                        author = author,
                        viewType = VIEW_TYPE_ITEM
                    )
                )

            }
            currentList = repository.addNewsItem(
                HomeModel(
                    viewType = VIEW_TYPE_LOADING
                )
            )
            _newsList.value = currentList


            isLoading = true
            Log.d("LoadingViewModel", "$isLoading")
        }

    }

    suspend fun getThumbnail(url: String): String? {//썸네일 가져오기

        var thumbnail: String?
        try {
            withContext(Dispatchers.IO) {
                val docs = Jsoup.connect(url).get()
                thumbnail = docs.select("meta[property=og:image]").attr("content")
            }
            if (thumbnail == null || thumbnail == "") return null
            return thumbnail
        } catch (e: ZipException) { //ZipException 예외처리
            Log.d("errorAtZip", "$url")

        } catch (e: IOException) {//IOException 에외처리
            e.printStackTrace()//디버깅 추적을 돕기위한 메서드
        } catch (e: EOFException) {
            Log.d("errAtZipEOFE", "$url")
        }
        return null
    }

    suspend fun getAuthor(url: String): String? {
        var author: String?
        try {
            withContext(Dispatchers.IO) {
                val docs = Jsoup.connect(url).get()
                author = docs.select("meta[name=dable:author]")?.attr("content")
                    .toString()//radioKorea에서 가져오는법
                author = Utils.getAuthorName(author)
                if (author == "") {
                    author = docs.select("em[class=media_end_head_journalist_name]")?.html()
                        .toString()//radioKorea에서 가져오는법
                    author = Utils.getAuthorName(author)
                    if (author == "") {
                        author = docs.select("meta[property=dable:author]")?.attr("content")
                            .toString()//radioKorea에서 가져오는법
                        author = Utils.getAuthorName(author)
                        if (author == "") {
                            author = docs.select("p[class=byline_p]").select("span").html()
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
                                author = Utils.getAuthorName(author)
                                if (author == "") {
                                    author =
                                        docs.select("meta[property=dd:author]")?.attr("content")
                                    author = Utils.getAuthorName(author)
                                    if (author == "") {
                                        author =
                                            docs.select("div[class=journalist_name]")?.html()
                                        author = Utils.getAuthorName(author)
                                        if (author == "") {
                                            author = docs.select("meta[property=dd:author]")
                                                ?.attr("content")
                                            author = Utils.getAuthorName(author)
                                            if (author == "") {
                                                author =
                                                    docs.select("span[class=d_newsName]")
                                                        .html()
                                                author = Utils.getAuthorName(author)
                                                if (author == "") {
                                                    author =
                                                        docs.select("span[class=writer]")
                                                            .html()
                                                    author = Utils.getAuthorName(author)
                                                    if (author == "") {
                                                        author =
                                                            docs.select("div[class=writer_info]")
                                                                .attr("span")
                                                        author = Utils.getAuthorName(author)
                                                        if (author == "") {
                                                            author =
                                                                docs.select("p[class=wr]")
                                                                    .html()
                                                            author = Utils.getAuthorName(author)
                                                            if (author == "") {
                                                                author =
                                                                    docs.select("p[class=article_byline]")
                                                                        .attr("span")
                                                                author = Utils.getAuthorName(author)
                                                                if (author == "") {
                                                                    author =
                                                                        docs.select("span[class=name]")
                                                                            .html()

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

                                                                    author =
                                                                        Utils.getAuthorName(author)
                                                                    if (author == "") {
                                                                        author =
                                                                            docs.select(
                                                                                "span[id=writeName]"
                                                                            )
                                                                                .html()

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

    fun modifyNewsItemIsLikeToLink(item: HomeModel?) {
        _list.value = repository.modifyNewsItemIsLikeToLink(item)
    }
}

class HomeModelFactory : ViewModelProvider.Factory {

    private val repository = MainFragmentRepositoryImpl(
        AtomicInteger(0), RetrofitInstance.search
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                GetSearchNewsUseCase(repository), repository
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }

    }
}