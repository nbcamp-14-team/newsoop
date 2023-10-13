package com.nbcamp_14_project.Main

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
    private val _list: MutableLiveData<List<MainFragmentModel>> = MutableLiveData()
    val list: LiveData<List<MainFragmentModel>> get() = _list
    fun search(query:String){
        viewModelScope.launch {
            val test = searchNews(query)
            val test2 = test.items
            if(test2 == null) return@launch
            for(i in test2.indices){
                val Thumbnail = Utils.getThumbnail(test2[i].link.toString())
                Log.d("testa2a","$Thumbnail")
            }

            Log.d("testaa","$test")
        }
    }



    fun addItem(item: MainFragmentModel?) {//라이브데이터에 아이템 추가하는 기능
        searchNews

    }

    fun removeItem(item: MainFragmentModel?) {//라이브데이터에서 아이템 삭제하는 기능
        repository.removeItem(item)
    }

    fun modifyItem(item: MainFragmentModel?) {
        repository.modifyItem(item)
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