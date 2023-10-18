package com.nbcamp_14_project.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcamp_14_project.Utils
import com.nbcamp_14_project.api.RetrofitInstance
import com.nbcamp_14_project.domain.GetSearchNewsUseCase
import com.nbcamp_14_project.home.HomeModel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class SearchViewModel(
    private val searchNews: GetSearchNewsUseCase,
    private val repository: SearchFragmentRepositoryImpl
) : ViewModel() {
    private val _list: MutableLiveData<List<HomeModel>> = MutableLiveData()
    val list: LiveData<List<HomeModel>> get() = _list
    private val _newsList: MutableLiveData<List<HomeModel>> = MutableLiveData()
    val newsList: LiveData<List<HomeModel>> get() = _newsList

    init {
        _list.value = repository.getList()
    }

    fun getSearchNews(query: String) {
        viewModelScope.launch {
            val docs = searchNews(query)
            val item = docs.items ?: return@launch
            for (i in item.indices) {//아이템 개수만큼 for문 실행
                val thumbnail = Utils.getThumbnail(item[i].link.toString())
                var title = item[i].title!!.replace("<b>", "")
                title = title.replace("</b>", "")
                title = title.replace("&quot;", "\"")
                val description = item[i].description
                val link = item[i].link
                val pubDate = item[i].pubDate
                val author = Utils.getAuthor(item[i].link.toString())
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

class SearchFragmentModelFactory : ViewModelProvider.Factory {

    private val repository = SearchFragmentRepositoryImpl(
        AtomicInteger(0),
        RetrofitInstance.search
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(
                GetSearchNewsUseCase(repository),
                repository
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }

    }
}