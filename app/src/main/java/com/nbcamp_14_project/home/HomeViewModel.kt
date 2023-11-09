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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    /**
     * 하단 리사이클러뷰에 출력할 뉴스 데이터 가져오는 함수
     */
    suspend fun addNewsItem(
        query: String,
        display: Int? = 5,
        startingNum: Int? = 1,
        sort: String? = "sim"
    ) {
        val docs = searchNews(query, display, startingNum, sort) //API를 이용하여 받아온 데이터를 문서로 저장
        val item = docs.items ?: return
        for (i in item.indices) {//아이템 개수만큼 for문 실행
            val thumbnail = getThumbnail(item[i].link.toString())
            var title = item[i].title!!.replace("<b>", "")
            title = title.replace("</b>", "")
            title = title.replace("&quot;", "\"")
            title = title.replace("&amp;", "&")
            var description = item[i].description?.replace("<b>", "")
            description = description?.replace("</b>", "")
            description = description?.replace("&quot;", "\"")
            description = description?.replace("&amp;", "&")
            val link = item[i].link
            val pubDate = item[i].pubDate
            var date = Date(pubDate)
            Log.d("date", "$date")
            val author = getAuthor(item[i].link.toString())
            Log.d("linkRecycler", "$link + $author")
            Log.d("inputItemFirst", "${item[i]}")
            repository.addNewsItem(
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
    }

    /**
     * 로딩 바 추가 함수
     */
    fun addLoadingItem() {
        repository.addNewsItem(
            HomeModel(
                viewType = VIEW_TYPE_LOADING
            )
        )
    }

    /**
     * 헤드라인에 출력한 뉴스 데이터 가져오는 함수
     */

    fun headLineNews(query: String, display: Int? = 5) {
        if (query.isNullOrBlank()) return
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

    /**
     * 하단 리사이클러뷰에 데이터값 주입 함수
     */
    fun detailNews(query: String, startingNum: Int? = 1, display: Int? = 5) {
        viewModelScope.launch {
            Log.d("iswork", "detailNews")
            addNewsItem(query, display, startingNum)
            Log.d("detail", "${repository.getNewsList()}")
            addLoadingItem()
            Log.d("detailAddLoading", "${repository.getNewsList()}")
            _newsList.value = repository.getNewsList()
        }

    }

    /**
     * 인피니티 스크롤을 구현하기 위해 사용되는 함수
     */

    fun detailNewsInfinity(
        query: String
        /** 검색어 */
        , startingNum: Int? = null
        /** 토큰 */
    ) {
        viewModelScope.launch {
            repository.removeLoadingTypeItem()
            addNewsItem(query, 10, startingNum)
//            val docs = searchNews(query, 10, startingNum, sort = "sim")
//            val item = docs.items ?: return@launch
//            repository.removeLoadingTypeItem()
//            for (i in item.indices) {//아이템 개수만큼 for문 실행
//                val thumbnail = getThumbnail(item[i].link.toString())
//                var title = item[i].title!!.replace("<b>", "")
//                title = title.replace("</b>", "")
//                title = title.replace("&quot;", "\"")
//                title = title.replace("&amp;", "&")
//                var description = item[i].description?.replace("<b>", "")
//                description = description?.replace("</b>", "")
//                description = description?.replace("&quot;", "\"")
//                val link = item[i].link
//                val pubDate = item[i].pubDate
//                var date = Date(pubDate)
//                val author = getAuthor(item[i].link.toString())
//                repository.addNewsItem(
//                    HomeModel(
//                        title = title,
//                        thumbnail = thumbnail,
//                        description = description,
//                        link = link,
//                        pubDate = date,
//                        author = author,
//                        viewType = VIEW_TYPE_ITEM
//                    )
//                )
//            }
            addLoadingItem()
//            var currentList: List<HomeModel> = repository.addNewsItem(
//                HomeModel(
//                    viewType = VIEW_TYPE_LOADING
//                )
//            )
            _newsList.value = repository.getNewsList()
            isLoading = true
        }
    }

    /**
     * 추천 탭 인피니티 인피니티 스크롤을 구현하기 위해 사용되는 함수
     */

    fun detailNewsInfinityToRecommend(
        firstCategory: String?,
        secondCategory: String?,
        thirdCategory: String?,
        startingNum: Int? = null
        /** 토큰 */
    ) {
        viewModelScope.launch {
            if (thirdCategory.isNullOrBlank()) {
                repository.removeLoadingTypeItem()
                addNewsItem(firstCategory ?: "생활", 5, startingNum)
                addLoadingItem()

                _newsList.value = repository.getNewsList()
                isLoading = true
            } else if (secondCategory.isNullOrBlank()) {
                repository.removeLoadingTypeItem()
                async {
                    addNewsItem(firstCategory ?: "생활", 7, startingNum)
                }
                addNewsItem(secondCategory ?: "정치", 3, startingNum)
                addLoadingItem()

                _newsList.value = repository.getNewsList()
                isLoading = true
            } else {
                repository.removeLoadingTypeItem()
                async {
                    addNewsItem(firstCategory ?: "정치", 5, startingNum)
                }
                async {
                    addNewsItem(secondCategory, 3, startingNum)
                }
                addNewsItem(thirdCategory ?: "정치", 2, startingNum)
                delay(1000)
                addLoadingItem()

                _newsList.value = repository.getNewsList()
                isLoading = true
                Log.d("LoadingViewModel", "$isLoading")
            }

        }

    }

    /**
     * 사용자가 지정한 카테고리가 2개일 때 사용하는 함수
     */

    fun twoCategoryDetailNews(
        firstCategory: String?,
        secondCategory: String?,
        startingNum: Int? = null
    ) {
        viewModelScope.launch {
            addNewsItem(firstCategory ?: "생활", 5, startingNum)
            delay(500)
            addNewsItem(secondCategory ?: "사회", 5, startingNum)
            addLoadingItem()

            _newsList.value = repository.getNewsList()
            isLoading = true
        }
    }

    /**
     * 사용자가 지정한 카테고리가 3개일 때 사용하는 함수
     */
    fun threeCategoryDetailNews(
        firstCategory: String?,
        secondCategory: String?,
        thirdCategory: String?,
        startingNum: Int? = null
    ) {
        viewModelScope.launch {
            Log.d("inputcategory", "$firstCategory $secondCategory $thirdCategory")
            async {
                addNewsItem(firstCategory ?: "사회", 5, startingNum)
            }
            async {
                addNewsItem(secondCategory ?: "정치", 5, startingNum)
            }
            addNewsItem(thirdCategory ?: "문화", 5, startingNum)
            addLoadingItem()
            _newsList.value = repository.getNewsList()
            isLoading = true
        }
    }

    /**
     * 썸네일 이미지를 가져오는 함수(JSOP 이용)
     */

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

    /**
     * 기자 성함을 가져오는 함수(JSOP 이용)
     */
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