package com.nbcamp_14_project.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.nbcamp_14_project.data.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
   private val currentUser = MutableLiveData<FirebaseUser>()

    val email: String? = null
    val password: String? = null
    init {
        currentUser.value=loginRepository.getCurrentUser()
    }


    fun getCurrentUser(): LiveData<FirebaseUser>{
        return currentUser
    }

    fun signOut(){
        loginRepository.signOut()
    }
}