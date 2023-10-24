package com.nbcamp_14_project

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.data.model.User
import com.nbcamp_14_project.databinding.ActivityLoginBinding
import com.nbcamp_14_project.databinding.ActivitySignUpBinding
import com.nbcamp_14_project.detail.DetailFragment
import com.nbcamp_14_project.ui.login.CategoryFragment
import com.nbcamp_14_project.ui.login.LoginActivity
import com.nbcamp_14_project.ui.login.LoginViewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var fbFireStore: FirebaseFirestore
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailTextWatcher()
        fbFireStore = FirebaseFirestore.getInstance()
        binding.btnSignUp.setOnClickListener {
            signUp()
        }
        binding.tvCategory.setOnClickListener {
            showCategory()
        }

    }

    private fun showCategory(){
        val detailFragment = CategoryFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frag_category, detailFragment)
        transaction.setReorderingAllowed(true)
        transaction.addToBackStack(null)
        transaction.commit()
        binding.tvChooseCategory.text = loginViewModel.category
    }
    private fun signUp(){
        val email = binding.etEmail.text
        val pw = binding.etPassword.text
        val name = binding.etName.text
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email.toString(), pw.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val curUser = auth.currentUser
                    val user = User()
                    user.email = curUser?.email
                    user.name = name.toString()
                    user.category = loginViewModel.category
                    fbFireStore.collection("User").document(curUser!!.uid).set(user)
                    val intent = Intent(this, LoginActivity::class.java).apply {
                        putExtra("id", email.toString())
                    }
                    setResult(RESULT_OK,intent)
                    if (!isFinishing) finish()
                } else {
                    Log.e("LoginActivity", "login fail")

                }
            }
    }

    private fun emailTextWatcher(){
        binding.etEmail.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailPattern()

            }



        })
    }

    private fun emailPattern():Boolean{
        val email = binding.etEmail.text.toString().trim()
        val pattern = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        if (pattern){
            //이메일 형식일 때
            binding.etEmail.setBackgroundResource(R.drawable.et_border_radius)
            return true
        }else{
            //이메일 형식이 아닐때
            binding.etEmail.setBackgroundResource(R.drawable.et_border_radius_red)
            return false
        }
    }

}