package com.nbcamp_14_project.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.data.model.User

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository() {

    private val auth = FirebaseAuth.getInstance()
    private val fbFireStore = FirebaseFirestore.getInstance()
    private val _userLiveData = MutableLiveData<FirebaseUser>()
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData
    fun getCurrentUser(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                _userLiveData.postValue(auth.currentUser)
                Log.d("login","data")
                val user = User()
                user.email = auth.currentUser?.email
                user.name = auth.currentUser?.displayName
                user.category = null
                fbFireStore.collection("User")?.document(auth!!.currentUser!!.uid)?.set(user)
            } else {
                Log.e("login","no data")
            }
        }
    }


    fun signOut(){
        auth.currentUser?.let {
            auth.signOut()
        }
    }


}