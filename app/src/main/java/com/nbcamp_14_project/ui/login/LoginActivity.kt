package com.nbcamp_14_project.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbcamp_14_project.databinding.ActivityLoginBinding
import com.nbcamp_14_project.R
import com.nbcamp_14_project.SignUpActivity
import com.nbcamp_14_project.data.model.User


class LoginActivity : AppCompatActivity() {
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var fbFireStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val naverClientId = getString(R.string.social_login_info_naver_client_id)
//        val naverClientSecret = getString(R.string.social_login_info_naver_client_secret)
//        val naverClientName = getString(R.string.social_login_info_naver_client_name)
//        NaverIdLoginSDK.initialize(this, naverClientId, naverClientSecret, naverClientName)

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
            finish()
        }

        binding.ivGoogleLogin.setOnClickListener {
            setGoogleLogin()
            googleLogin()
            finish()

        }

        binding.ivNaver.setOnClickListener {
//            naverLogin()
        }
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

    private fun logIn() {
        val email = binding.etUsername.text
        val pw = binding.etPassword.text
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email.toString(), pw.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("LoginActivity", "login fail")
                }
            }
    }

    //    private fun naverLogin() {
//        var naverToken: String? = ""
//
//        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
//            override fun onSuccess(response: NidProfileResponse) {
//                val userId = response.profile?.id
////                binding.tvResult.text = "id: ${userId} \ntoken: ${naverToken}"
//                Toast.makeText(this@LoginActivity, "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onFailure(httpStatus: Int, message: String) {
//                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                Toast.makeText(
//                    this@LoginActivity, "errorCode: ${errorCode}\n" +
//                            "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onError(errorCode: Int, message: String) {
//                onFailure(errorCode, message)
//            }
//        }
//        val oauthLoginCallback = object : OAuthLoginCallback {
//            override fun onSuccess() {
//                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
//                naverToken = NaverIdLoginSDK.getAccessToken()
////                var naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
////                var naverExpiresAt = NaverIdLoginSDK.getExpiresAt().toString()
////                var naverTokenType = NaverIdLoginSDK.getTokenType()
////                var naverState = NaverIdLoginSDK.getState().toString()
//                //로그인 유저 정보 가져오기
//                NidOAuthLogin().callProfileApi(profileCallback)
//            }
//
//            override fun onFailure(httpStatus: Int, message: String) {
//                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                Toast.makeText(
//                    this@LoginActivity, "errorCode: ${errorCode}\n" +
//                            "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onError(errorCode: Int, message: String) {
//                onFailure(errorCode, message)
//            }
//        }
//        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
//    }


}
