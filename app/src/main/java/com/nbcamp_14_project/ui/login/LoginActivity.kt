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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.nbcamp_14_project.databinding.ActivityLoginBinding
import com.nbcamp_14_project.R
import com.nbcamp_14_project.SignUpActivity
import com.nbcamp_14_project.data.model.FirebaseUserData
import com.nbcamp_14_project.data.model.User
import com.nbcamp_14_project.favorite.FavoriteFragment
import com.nbcamp_14_project.home.HomeFragment
import com.nbcamp_14_project.mainpage.MainActivity
import com.nbcamp_14_project.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var fbFireStore: FirebaseFirestore
    private var firebaseAuthStateListener: FirebaseAuth.AuthStateListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        fbFireStore = FirebaseFirestore.getInstance()




//        val naverClientId = getString(R.string.social_login_info_naver_client_id)
//        val naverClientSecret = getString(R.string.social_login_info_naver_client_secret)
//        val naverClientName = getString(R.string.social_login_info_naver_client_name)
//        NaverIdLoginSDK.initialize(this, naverClientId, naverClientSecret, naverClientName)


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
            setGoogleLogin()
            googleLogin()
            getCurrentProfile()

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

    private fun logIn() {
        val email = binding.etUsername.text
        val pw = binding.etPassword.text
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email.toString(), pw.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val curUser = auth.currentUser
                    val user = User()
                    user.email = auth.currentUser?.email
                    user.name = auth.currentUser?.displayName
                    Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("LoginActivity", "login fail")
                }
            }
    }

    private fun getCurrentProfile(){
        val curUser = GoogleSignIn.getLastSignedInAccount(this)
        curUser?.let{
            val user = User()
            val email = curUser.email.toString()
            val displayName = curUser.displayName.toString()

            Log.d("이메일",email)
            Log.d("이름",displayName)

        }
    }
    private fun movePage(user: FirebaseUser?){
        if (user != null){
            startActivity(Intent(this, FavoriteFragment::class.java))
            finish()
        }
    }



}
