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
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class MainFragmentViewModel(
    private val searchNews: GetSearchNewsUseCase,
    private val repository: MainFragmentRepositoryImpl
) : ViewModel() {
    private val _list: MutableLiveData<List<HomeModel>> = MutableLiveData()
    val list: LiveData<List<HomeModel>> get() = _list

    init {
        _list.value = repository.getList()
    }

    fun search(query: String) {
        viewModelScope.launch {
            val docs = searchNews(query)
            val item = docs.items ?:  return@launch
            for (i in item.indices) {
                val thumbnail = Utils.getThumbnail(item[i].link.toString())
                var title = item[i].title!!.replace("<b>","")
                title = title.replace("</b>","")
                title = title.replace("&quot;","\"")

                val description = item[i].description
                _list.value = repository.addItem(
                    HomeModel(
                        title = title,
                        thumbnail = thumbnail,
                        description = description
                    )
                )
                Log.d("testa2a", "$thumbnail")
            }
            Log.d("testaa", "$docs")
        }
    }


    fun addItem(item: HomeModel?) {//라이브데이터에 아이템 추가하는 기능
        _list.value = repository.addItem(item)

    }

    fun removeItem(item: HomeModel?) {//라이브데이터에서 아이템 삭제하는 기능
        _list.value = repository.removeItem(item)
    }

    fun modifyItem(item: HomeModel?) {
        _list.value = repository.modifyItem(item)
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