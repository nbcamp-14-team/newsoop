package com.nbcamp_14_project.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.nbcamp_14_project.data.LoginRepository

class LoginViewModel: ViewModel() {
   private val loginRepository: LoginRepository = LoginRepository()
    private val _userLiveData = loginRepository.userLiveData
    var category = MutableLiveData<String>()
    var secondCategory = MutableLiveData<String>()
    var thirdCategory = MutableLiveData<String>()
    private val _isCategoryBooleanValue = MutableLiveData<Boolean>()
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



    fun signOut(){
        loginRepository.signOut()
    }


}