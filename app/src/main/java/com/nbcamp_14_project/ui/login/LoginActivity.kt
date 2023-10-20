package com.nbcamp_14_project.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.nbcamp_14_project.SignUpActivity
import com.nbcamp_14_project.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //TODO : 임시
        //setGoogleLogin()
        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val userId = it.data?.getStringExtra("id") ?: ""
                    binding.etUsername.setText(userId)
                }
            }

        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    loginViewModel.getCurrentUser(account.idToken!!)
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT)
                        .show()

                } catch (e: ApiException) {
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        binding.btnLogin.setOnClickListener {
            logIn()
        }

        binding.ivGoogleLogin.setOnClickListener {
            googleLogin()
        }
    }

//    private fun setGoogleLogin() {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//    }

    private fun googleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        getResult.launch(signInIntent)
    }


    private fun logIn() {
        val email = binding.etUsername.text
        val pw = binding.etPassword.text
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email.toString(), pw.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("LoginActivity", "login success! ${user?.email}")
                    Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("LoginActivity", "login fail")
                }
            }
    }

}


//fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
//    this.addTextChangedListener(object : TextWatcher {
//        override fun afterTextChanged(editable: Editable?) {
//            afterTextChanged.invoke(editable.toString())
//        }
//
//        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//    })
//}