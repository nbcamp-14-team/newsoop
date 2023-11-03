package com.nbcamp_14_project.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcamp_14_project.api.RetrofitInstance
import com.nbcamp_14_project.domain.GetSearchNewsUseCase
import java.util.concurrent.atomic.AtomicInteger

class HomeViewPagerViewModel(
    private val repository: HomeViewPagerRepository
): ViewModel() {
    private val _list: MutableLiveData<List<HomeFragmentTabs>> = MutableLiveData()
    val list: LiveData<List<HomeFragmentTabs>> get() = _list

    init{
        _list.value =repository.getList()
    }
    fun removeListAtFirst(){
        _list.value = repository.removeListAtFront()
    }

    fun addListAtFirst(query:String,title:String){
        _list.value = repository.addListAtFront(query,title)
    }
}
class HomeViewPagerViewModelFactory : ViewModelProvider.Factory {

    private val repository = HomeViewPagerRepository(
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewPagerViewModel::class.java)) {
            return HomeViewPagerViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }

    }
}