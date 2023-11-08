package com.nbcamp_14_project.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.play.integrity.internal.f
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
                val collectionRef = fbFireStore.collection("User")
                    .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                collectionRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result

                            val category = document.getString("category") ?:""
                            val secondcategory = document.getString("secondCategory") ?:""
                            val thirdcategory = document.getString("thirdCategory") ?:""
                            val user = User()
                            user.email = auth.currentUser?.email
                            user.name = auth.currentUser?.displayName
                            user.category = category
                            user.secondCategory = secondcategory
                            user.thirdCategory = thirdcategory
                            fbFireStore.collection("User")?.document(auth!!.currentUser!!.uid)?.set(user)

                    } else {
                        Log.d("data", "no data")
                    }
                }

                Log.d("login","data")




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