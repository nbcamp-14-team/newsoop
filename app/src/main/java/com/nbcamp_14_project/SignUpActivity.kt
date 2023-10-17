package com.nbcamp_14_project

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.nbcamp_14_project.databinding.ActivityLoginBinding
import com.nbcamp_14_project.databinding.ActivitySignUpBinding
import com.nbcamp_14_project.ui.login.LoginActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text
            val pw = binding.etPassword.text
            auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email.toString(), pw.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Log.d("LoginActivity", "login success! ${user?.email}")
                        val intent = Intent(this, LoginActivity::class.java).apply {
                            putExtra("id", email)
                            putExtra("pw", pw)
                        }
                        setResult(RESULT_OK,intent)
                        if (!isFinishing) finish()
                    } else {
                        Log.e("LoginActivity", "login fail")
                    }
                }
        }

    }
}