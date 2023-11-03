package com.nbcamp_14_project.newspaper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nbcamp_14_project.R
import com.nbcamp_14_project.databinding.ActivityMainBinding
import com.nbcamp_14_project.databinding.DialogNewspaperBinding

class NewspaperDialog : AppCompatActivity() {
    companion object {
        fun newInstance(
            context: Context,
            link: String
        ) = Intent(context, NewspaperDialog::class.java).apply {
            putExtra("link", link)
        }
    }

    private val binding by lazy { DialogNewspaperBinding.inflate(layoutInflater) }
    private val link by lazy {
        intent.getStringExtra("link")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_up, R.anim.none)
        setContentView(binding.root)
        initView()
    }

    private var backPressedTime = 0L
    override fun onBackPressed() {
        super.onBackPressed()
        with(binding) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish()
                    return
                    overridePendingTransition(R.anim.none, R.anim.slide_down)
                }
                Toast.makeText(this@NewspaperDialog, "뒤로가기를 두번 눌러서 종료시켜주세요", Toast.LENGTH_SHORT)
                    .show()
                backPressedTime = System.currentTimeMillis()
            }
        }
    }

    inner class MyWebChromeClient : WebChromeClient() {
        //프로그래스바 적용하기위한 커스텀
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            binding.progressBar.progress = newProgress
        }
    }

    inner class MyWebViewClient : WebViewClient() {
        // 페이지가 로딩이 시작하는 시점 콜백
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        // 페이지가 로딩이 끝나는 시점 콜백
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.progressBar.visibility = View.GONE
        }

        // 페이지가 보여지는 시점 콜백
        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
        }
    }

    private fun initView() = with(binding) {

        webView.apply {
//            settings.userAgentString += "Newsoop"
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH)//랜더 우선순위
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN//레이아웃 알고리즘 관련
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true //window.open() 동작 설정
            settings.useWideViewPort = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE//브라우저 캐쉬 허용
            settings.databaseEnabled = true
            settings.loadsImagesAutomatically = true
            settings.domStorageEnabled = true // 로컬저장소를 허락
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.mixedContentMode =
                    WebSettings.MIXED_CONTENT_ALWAYS_ALLOW//https에서 http 컨텐츠가 호출 안되는 현상 수정코드
            }
            webChromeClient = MyWebChromeClient()
            webViewClient = MyWebViewClient()
        }
        /**
         * 하드웨어 가속
         */
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            webView.setLayerType(WebView.LAYER_TYPE_HARDWARE, null)
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        webView.loadUrl(link ?: "https://webit22.tistory.com/75")
        btnFinish.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.slide_down)
        }
    }

}