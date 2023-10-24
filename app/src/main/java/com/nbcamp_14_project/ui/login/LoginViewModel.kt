package com.nbcamp_14_project.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.nbcamp_14_project.data.LoginRepository

class LoginViewModel: ViewModel() {
   private val loginRepository: LoginRepository = LoginRepository()
    private val _userLiveData = loginRepository.userLiveData
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData


    fun getCurrentUser(idToken: String){
        loginRepository.getCurrentUser(idToken)
        
    }

    fun signOut(){
        loginRepository.signOut()
    }
}