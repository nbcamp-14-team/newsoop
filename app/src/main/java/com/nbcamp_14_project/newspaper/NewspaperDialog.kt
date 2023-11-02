package com.nbcamp_14_project.newspaper

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nbcamp_14_project.databinding.ActivityMainBinding
import com.nbcamp_14_project.databinding.DialogNewspaperBinding

class NewspaperDialog : AppCompatActivity() {
    companion object{
        fun newInstance(
            context: Context,
            link: String
        ) = Intent(context,NewspaperDialog::class.java).apply{
            putExtra("link",link)
        }
    }

    private val binding by lazy { DialogNewspaperBinding.inflate(layoutInflater) }
    private val link by lazy{
        intent.getStringExtra("link")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        with(binding) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                finish()
            }
        }
    }
    private fun initView() = with(binding){

        webView.apply{

            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE//브라우저 캐쉬 허용
            settings.databaseEnabled = true
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW//https에서 http 컨텐츠가 호출 안되는 현상 수정코드
            }

        }
        webView.webViewClient = WebViewClient()
        webView.loadUrl(link?:"https://webit22.tistory.com/75")
        btnFinish.setOnClickListener {
            finish()
        }
    }
}