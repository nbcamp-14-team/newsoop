package com.nbcamp_14_project.newspaper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.concurrent.atomic.AtomicInteger

class NewspaperViewModel(
    private val repository: NewspaperRepositoryImpl
) : ViewModel() {
    private val _currentList: MutableLiveData<List<NewspaperModel>> = MutableLiveData()
    val currentList: LiveData<List<NewspaperModel>> get() = _currentList
    fun getCurrentList(query: String) {
        when (query) {
            "정치" -> _currentList.value = repository.getPoliticsList()
            "경제" -> _currentList.value = repository.getEconomyList()
            "사회" -> _currentList.value = repository.getSocietyList()
            "생활" -> _currentList.value = repository.getLifeList()
            "문화" -> _currentList.value = repository.getCultureList()
            "IT" -> _currentList.value = repository.getItList()
            "과학" -> _currentList.value = repository.getScienceList()
            "세계" -> _currentList.value = repository.getWorldList()
        }
    }
}
class NewspaperModelFactory : ViewModelProvider.Factory {

    private val repository = NewspaperRepositoryImpl(
        AtomicInteger(0)
    )
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewspaperViewModel::class.java)) {
            return NewspaperViewModel(
                repository
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}