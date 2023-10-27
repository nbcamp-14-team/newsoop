package com.nbcamp_14_project.data.model

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUserData : LiveData<FirebaseUser?>() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        value = firebaseAuth.currentUser
    }

    override fun onActive() {
        firebaseAuth.addAuthStateListener (authStateListener)
    }

    //로그아웃했을때 observing 하고있을 메모리 지우기
    override fun onInactive() {
        firebaseAuth.removeAuthStateListener (authStateListener)
    }
}