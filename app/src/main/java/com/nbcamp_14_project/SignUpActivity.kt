package com.nbcamp_14_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.data.model.User
import com.nbcamp_14_project.databinding.ActivitySignUpBinding
import com.nbcamp_14_project.ui.login.CategoryFragment
import com.nbcamp_14_project.ui.login.LoginActivity
import com.nbcamp_14_project.ui.login.LoginViewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var fbFireStore: FirebaseFirestore
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var shakeAnimation: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fbFireStore = FirebaseFirestore.getInstance()
        setTextWatcher()
        binding.ivSignUpBack.setOnClickListener {
            finish()
        }
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake)
        //회원가입 완료버튼
        binding.btnSignUp.setOnClickListener {
            passSignUp()
        }
        //카테고리 버튼
        binding.tvChooseCategory.setOnClickListener {
            showCategory()
        }
    }

    private fun passSignUp() {

        val signIdText = binding.etEmail.text.toString()
        val signPwText = binding.etPassword.text.toString()
        val signPwCheckText = binding.etCheckPw.text.toString()
        val nameText = binding.etName.text.toString()
        val signIdWatcher = binding.tvEmailWatcher.text.toString()
        val signPwCheckWatcher = binding.tvCheckPw
        if (signIdText.isEmpty() || signPwText.isEmpty() || signPwCheckText.isEmpty() || nameText.isEmpty()) {
            Toast.makeText(this, "입력되지 않은 정보가 있습니다.", Toast.LENGTH_SHORT).show()
        } else if (signIdWatcher != "사용가능한 이메일입니다.") {
            when(signIdWatcher){
                "중복체크를 해주세요." -> {Toast.makeText(this, "중복확인을 완료해주세요.", Toast.LENGTH_SHORT).show()}
                else ->{Toast.makeText(this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()}
            }
        }else if (!signPwCheckWatcher.isVisible){
            Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
        }else {
            signUp()
        }
    }

    private fun checkEmail() {
        val emailToCheck = binding.etEmail.text.toString()
        fbFireStore.collection("User")
            .whereEqualTo("email", emailToCheck)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val querySnapshot = task.result
                    if (querySnapshot != null && !querySnapshot.isEmpty) {
                        // 중복된 이메일이 이미 존재함
                        binding.btnCheckExist.startAnimation(shakeAnimation)
                        Toast.makeText(this, "중복된 이메일이 존재합니다", Toast.LENGTH_SHORT).show()
                    } else {
                        // 이메일이 중복되지 않음
                        Toast.makeText(this, "사용가능 합니다", Toast.LENGTH_SHORT).show()
                        binding.tvEmailWatcher.text = "사용가능한 이메일입니다."
                        binding.btnCheckExist.visibility = View.INVISIBLE
                        binding.ivCheck.visibility = View.VISIBLE
                    }
                }
            }
    }

    private fun showCategory() {
        val detailFragment = CategoryFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frag_category, detailFragment)
        transaction.setReorderingAllowed(true)
        transaction.addToBackStack(null)
        transaction.commit()
        loginViewModel.category.observe(this) { text ->
            binding.tvFirstCategory.text = text
        }

        loginViewModel.secondCategory.observe(this) { text ->
            if (text.isNotEmpty()){
                binding.tvSecondCategory.text = ", $text"
            }
            else {
                binding.tvSecondCategory.text = null
            }
        }

        loginViewModel.thirdCategory.observe(this) { text ->
            if (text.isNotEmpty()){
                binding.tvThirdCategory.text = ", $text"
            }else {
                binding.tvThirdCategory.text = null
            }
        }

    }

    private fun signUp() {
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
                    user.category = loginViewModel.category.value
                    user.secondCategory = loginViewModel.secondCategory.value
                    user.thirdCategory = loginViewModel.thirdCategory.value
                    fbFireStore.collection("User").document(curUser!!.uid).set(user)
                    val intent = Intent(this, LoginActivity::class.java).apply {
                        putExtra("id", email.toString())
                    }
                    setResult(RESULT_OK, intent)
                    if (!isFinishing) finish()
                } else {
                    Log.e("LoginActivity", "login fail")
                }
            }
    }

    private fun setTextWatcher() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnCheckExist.visibility = View.VISIBLE
                binding.ivCheck.visibility = View.INVISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.run {
                    val length: Int = s?.length ?: 0
                    //커서 따라가기
                    etEmail.requestFocus()
                    //아이디의 길이에 따른 에러메세지
                    when (length) {
                        0 -> {
                            binding.etEmail.setBackgroundResource(R.drawable.et_border_radius_red)
                            tvEmailWatcher.text = "이메일 형식으로 입력해주세요"
                            tvEmailWatcher.setTextColor(
                                ContextCompat.getColor(
                                    this@SignUpActivity,
                                    R.color.wrongColor
                                )
                            )
                        }

                        in 1..25 ->
                            if (!emailPattern()) {
                                tvEmailWatcher.text = "이메일 형식으로 입력해주세요"
                            } else {
                                tvEmailWatcher.text = "중복체크를 해주세요."
                                binding.btnCheckExist.setOnClickListener {
                                    checkEmail()
                                }
                                tvEmailWatcher.setTextColor(
                                    ContextCompat.getColor(
                                        this@SignUpActivity,
                                        R.color.green
                                    )
                                )
                            }

                        else -> {
                            tvEmailWatcher.text = "이메일이 너무 길어요"
                            binding.etEmail.setBackgroundResource(R.drawable.et_border_radius_red)
                            tvEmailWatcher.setTextColor(
                                ContextCompat.getColor(
                                    this@SignUpActivity,
                                    R.color.wrongColor
                                )
                            )
                        }
                    }
                }


            }


        })
        binding.etPassword.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    binding.run {
                        val length: Int = s?.length ?: 0
                        etPassword.requestFocus()
                        val regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&+=]).+\$".toRegex()
                        when (length) {
                            0 -> {
                                tvPasswordWatcher.text = "6자 이상, 숫자 기호 문자를 각각 하나 이상씩 포함해주세요"
                                binding.etPassword.setBackgroundResource(R.drawable.et_border_radius_red)
                                tvPasswordWatcher.setTextColor(
                                    ContextCompat.getColor(
                                        this@SignUpActivity,
                                        R.color.wrongColor
                                    )
                                )
                            }

                            in 1..5 -> {
                                tvPasswordWatcher.text = "비밀번호가 너무 짧아요"
                                binding.etPassword.setBackgroundResource(R.drawable.et_border_radius_red)
                                tvPasswordWatcher.setTextColor(
                                    ContextCompat.getColor(
                                        this@SignUpActivity,
                                        R.color.wrongColor
                                    )
                                )
                            }

                            else ->
                                if (!regex.matches(s.toString())) {
                                    tvPasswordWatcher.text = "1개 이상의 숫자, 기호, 문자를 포함해주세요"
                                } else {
                                    binding.etPassword.setBackgroundResource(R.drawable.et_border_radius_green)
                                    tvPasswordWatcher.text = "사용 가능한 비밀번호 입니다."
                                    tvPasswordWatcher.setTextColor(
                                        ContextCompat.getColor(
                                            this@SignUpActivity,
                                            R.color.green
                                        )
                                    )
                                }

                        }
                    }


                }


            })
        binding.etCheckPw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etCheckPw.requestFocus()
                val checkedPw = binding.etPassword.text
                if ((binding.tvPasswordWatcher.text == "사용 가능한 비밀번호 입니다.") && (checkedPw.toString() == binding.etCheckPw.text.toString())) {
                    binding.tvCheckPw.visibility = View.VISIBLE
                    binding.etCheckPw.setBackgroundResource(R.drawable.et_border_radius_green)
                }else{
                    binding.tvCheckPw.visibility = View.INVISIBLE
                    binding.etCheckPw.setBackgroundResource(R.drawable.et_border_radius_red)
                }

            }
        })
    }

    private fun emailPattern(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        val pattern = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        if (pattern) {
            //이메일 형식일 때
            binding.etEmail.setBackgroundResource(R.drawable.et_border_radius_green)
            return true
        } else {
            //이메일 형식이 아닐때
            binding.etEmail.setBackgroundResource(R.drawable.et_border_radius_red)
            return false
        }
    }

}


