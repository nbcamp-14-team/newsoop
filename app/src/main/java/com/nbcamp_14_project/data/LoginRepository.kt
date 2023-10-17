package com.nbcamp_14_project.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.nbcamp_14_project.data.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository() {

    private val auth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser?{
        return auth.currentUser
    }

    fun signOut(){
        auth.currentUser?.let {
            auth.signOut()
        }
    }


}