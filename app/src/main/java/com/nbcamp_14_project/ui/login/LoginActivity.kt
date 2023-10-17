package com.nbcamp_14_project.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.nbcamp_14_project.SignUpActivity
import com.nbcamp_14_project.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val userId = it.data?.getStringExtra("id") ?: ""
                    val userPw = it.data?.getStringExtra("pw") ?: ""

                    binding.etUsername.setText(userId)
                    binding.etPassword.setText(userPw)
                }
            }

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        binding.btnLogin.setOnClickListener {
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


        binding.ivGoogleLogin.setOnClickListener {}


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