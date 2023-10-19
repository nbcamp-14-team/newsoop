package com.nbcamp_14_project.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcamp_14_project.Utils
import com.nbcamp_14_project.api.RetrofitInstance
import com.nbcamp_14_project.home.HomeModel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class SearchViewModel(
    private val repository: SearchRepository
) : ViewModel() {
    private val _searchResultList: MutableLiveData<List<HomeModel>> = MutableLiveData()
    val searchResultList: LiveData<List<HomeModel>> get() = _searchResultList

    fun getSearchNews(query: String, display: Int, start: Int) {
        viewModelScope.launch {
            //docs item이 들어오는지 확인
            val docs = repository.getNews(query, display = display, start = start)
            val item = docs.items ?: return@launch
            repository.clearList()
            for (i in item.indices) {//아이템 개수만큼 for문 실행
                val thumbnail = Utils.getThumbnail(item[i].link.toString())
                var title = item[i].title?.replace("<b>", "") ?: "제목 없음"
                title = title.replace("</b>", "")
                title = title.replace("&quot;", "\"")
                val description = item[i].description
                val link = item[i].link
                val pubDate = item[i].pubDate
                //TODO : fix SocketTimeoutException: timeout
                //val author = Utils.getAuthor(item[i].link.toString())
                repository.addNewsItem(
                    HomeModel(
                        title = title,
                        thumbnail = thumbnail,
                        description = description,
                        link = link,
                        pubDate = pubDate,
                        author = "test",
                        viewType = 1
                    )
                )
            }
            _searchResultList.value = repository.getList()
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