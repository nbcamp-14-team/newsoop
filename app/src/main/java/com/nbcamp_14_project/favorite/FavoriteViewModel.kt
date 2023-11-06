package com.nbcamp_14_project.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbcamp_14_project.Utils
import com.nbcamp_14_project.detail.DetailInfo
import com.nbcamp_14_project.domain.GetSearchNewsUseCase
import com.nbcamp_14_project.home.HomeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.EOFException
import java.io.IOException
import java.util.Date
import java.util.zip.ZipException

class FavoriteViewModel(private val searchNews: GetSearchNewsUseCase) : ViewModel() {
    // ViewModel 클래스 정의
    private val _list: MutableLiveData<List<HomeModel>> = MutableLiveData()
    val list: LiveData<List<HomeModel>> get() = _list

    private val _authorList: MutableLiveData<List<String>> = MutableLiveData()
    val authorList: LiveData<List<String>> get() = _authorList
    private val _favoriteList: MutableLiveData<List<DetailInfo>> = MutableLiveData()
    val favoriteList: LiveData<List<DetailInfo>> get() = _favoriteList



    var isLogin: Boolean = false
    // 사용자 로그인 상태를 저장하는 변수

    // 즐겨찾기 아이템 추가
    fun addFavoriteItem(item: DetailInfo) {
        val currentList = _favoriteList.value?.toMutableList() ?: mutableListOf()
        // 현재의 즐겨찾기 목록을 가져오거나, 비어있는 리스트를 생성
        currentList.add(0, item) // 새로운 아이템을 목록의 맨 앞에 추가
        _favoriteList.value = currentList // 변경된 목록을 LiveData에 설정
    }



    // 즐겨찾기 아이템 삭제
    fun removeFavoriteItem(item: DetailInfo) {
        val currentList = _favoriteList.value?.toMutableList() ?: return
        // 현재의 즐겨찾기 목록을 가져오거나, 목록이 비어있으면 함수 종료
        if (currentList.contains(item)) {
            currentList.remove(item) // 목록에서 해당 아이템 삭제
            _favoriteList.value = currentList // 변경된 목록을 LiveData에 설정
        }
    }

    // 즐겨찾기 아이템을 지정된 위치에서 삭제
    fun removeFavoriteItemToPosition(item: DetailInfo) {
        val currentList = _favoriteList.value?.toMutableList() ?: return
        // 현재의 즐겨찾기 목록을 가져오거나, 목록이 비어있으면 함수 종료
        fun findIndex(item: DetailInfo?): Int {
            if (item == null) return 0
            // 입력된 아이템이 null이면 0 반환
            val findItem = currentList.find {
                it.thumbnail == item.thumbnail
            }
            // 현재 목록에서 아이템을 고유 식별자로 찾음
            return currentList.indexOf(findItem)
            // 아이템의 위치를 반환
        }
        val findPosition = findIndex(item)
        if (findPosition < 0) {
            return // 찾은 위치가 0 미만이면 함수 종료
        }
        if (item == null) return
        // 아이템이 null이면 함수 종료
        currentList.removeAt(findPosition)
        // 아이템을 찾은 위치에서 삭제
        _favoriteList.value = currentList
        // 변경된 목록을 LiveData에 설정
    }

    // 즐겨찾기 목록을 설정
    fun setFavoriteList(list: List<DetailInfo>) {
        _favoriteList.value = list
    }

    fun detailNews(query: String, startingNum: Int? = null, display: Int? = 5) {
        viewModelScope.launch {
            val docs = searchNews(query, display, startingNum?.plus(5), sort = "sim")
            val item = docs.items ?: return@launch
            var currentList = _list.value?.toMutableList()
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
                currentList?.add(
                    HomeModel(
                        title = title,
                        thumbnail = thumbnail,
                        description = description,
                        link = link,
                        pubDate = date,
                        author = author,
                    )
                )
            }

            _list.value = currentList!!
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


}
