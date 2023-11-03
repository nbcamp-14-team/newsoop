package com.nbcamp_14_project.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.recaptcha.RecaptchaAction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.databinding.ActivityLoginBinding
import com.nbcamp_14_project.R
import com.nbcamp_14_project.SignUpActivity
import com.nbcamp_14_project.data.model.User
import com.nbcamp_14_project.home.HomeViewPagerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import kotlin.coroutines.CoroutineContext


class LoginActivity : AppCompatActivity() {
    companion object{
        const val CHECK_LOGIN = "Login"
    }
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var fbFireStore: FirebaseFirestore
    private val homeViewPagerViewModel: HomeViewPagerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        fbFireStore = FirebaseFirestore.getInstance()

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
//                        CategoryDialog(this).show()
                    val intent = Intent().apply{
                        putExtra(
                            CHECK_LOGIN,
                            "Login"
                        )
                    }//로그인 검사 기능 추가구현

                    GlobalScope.launch(Dispatchers.IO) {
                        delay(1000) // 1000 milliseconds = 1 second
                        setResult(Activity.RESULT_OK,intent)//로그인 검사 기능 추가구현
                        finish()
                    }

                } catch (e: ApiException) {
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        binding.btnLogin.setOnClickListener {
            passLogin()
        }

        binding.tvFixPw.setOnClickListener {
            goFixFragment()
        }

        binding.ivGoogleLogin.setOnClickListener {
            loginViewModel.signOut()
            setGoogleLogin()
            googleLogin()

        }

    }

    private fun goFixFragment() {
        val fixFragment = FixPwFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frag_fix, fixFragment)
        transaction.setReorderingAllowed(true)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun setGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun googleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        getResult.launch(signInIntent)

    }

    private fun passLogin() {
        val loginId = binding.etUsername.text.toString()
        val loginPw = binding.etPassword.text.toString()
        if (loginId.isNotEmpty() && loginPw.isNotEmpty()) {
            logIn()
        } else {
            Toast.makeText(this, "빈칸을 확인해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun logIn() {
        val email = binding.etUsername.text
        val pw = binding.etPassword.text
        val pattern = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email.toString(), pw.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "환영합니다", Toast.LENGTH_SHORT).show()
                    val intent = Intent().apply{
                        putExtra(
                            CHECK_LOGIN,
                            "Login"
                        )
                    }
                    setResult(Activity.RESULT_OK,intent)//로그인 검사 기능 추가구현
                    finish()
                } else {
                    if (!pattern) {
                        Toast.makeText(this, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "아이디, 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }

                }
            }

    }
}
