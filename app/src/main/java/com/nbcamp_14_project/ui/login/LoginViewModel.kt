package com.nbcamp_14_project.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.nbcamp_14_project.data.LoginRepository
import com.nbcamp_14_project.home.HomeModel

class LoginViewModel: ViewModel() {
   private val loginRepository: LoginRepository = LoginRepository()
    private val _userLiveData = loginRepository.userLiveData
    var category = MutableLiveData<String>()
    var secondCategory = MutableLiveData<String>()
    var thirdCategory = MutableLiveData<String>()
//    private val _categoryList :MutableLiveData<List<String>> =MutableLiveData()
//    val categoryList: LiveData<List<String>> get() = _categoryList


    private val _isCategoryBooleanValue = MutableLiveData<Boolean>()
    var authorList = MutableLiveData<List<String>>()
    val isCategoryBooleanValue: LiveData<Boolean>
        get() = _isCategoryBooleanValue


    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData

    fun updateBooleanValue(value: Boolean) {
        _isCategoryBooleanValue.value = value
    }
    fun getCurrentUser(idToken: String){
        loginRepository.getCurrentUser(idToken)
        
    }
//    fun categoryListChange(){
//        var currentList = _categoryList.value!!.toMutableList()
//        currentList.add("A")
//        _categoryList.value = currentList
//    }



    fun signOut(){
        loginRepository.signOut()
    }


}