package com.nbcamp_14_project.newspaper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.concurrent.atomic.AtomicInteger

class NewspaperViewModel(
    private val repository: NewspaperRepositoryImpl
):ViewModel() {
    private val _politicsList: MutableLiveData<List<NewspaperModel>> = MutableLiveData()
    val politicsList: LiveData<List<NewspaperModel>> get() = _politicsList
    init {
        _politicsList.value = repository.getPoliticsList()
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