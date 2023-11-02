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
    private val _economyList: MutableLiveData<List<NewspaperModel>> = MutableLiveData()
    val economyList: LiveData<List<NewspaperModel>> get() = _economyList
    private val _societyList: MutableLiveData<List<NewspaperModel>> = MutableLiveData()
    val societyList: LiveData<List<NewspaperModel>> get() = _societyList
    private val _lifeList: MutableLiveData<List<NewspaperModel>> = MutableLiveData()
    val lifeList: LiveData<List<NewspaperModel>> get() = _lifeList

    private val _cultureList: MutableLiveData<List<NewspaperModel>> = MutableLiveData()
    val cultureList: LiveData<List<NewspaperModel>> get() = _cultureList
    private val _itList: MutableLiveData<List<NewspaperModel>> = MutableLiveData()
    val itList: LiveData<List<NewspaperModel>> get() = _itList
    private val _scienceList: MutableLiveData<List<NewspaperModel>> = MutableLiveData()
    val scienceList: LiveData<List<NewspaperModel>> get() = _scienceList
    private val _worldList: MutableLiveData<List<NewspaperModel>> = MutableLiveData()
    val worldList: LiveData<List<NewspaperModel>> get() = _worldList

    init {
        _politicsList.value = repository.getPoliticsList()
        _economyList.value = repository.getEconomyList()
        _societyList.value = repository.getSocietyList()
        _lifeList.value = repository.getLifeList()
        _cultureList.value = repository.getCultureList()
        _itList.value=repository.getItList()
        _scienceList.value=repository.getScienceList()
        _worldList.value=repository.getWorldList()
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