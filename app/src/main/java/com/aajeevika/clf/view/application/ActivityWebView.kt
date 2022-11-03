package com.aajeevika.clf.view.application

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivity
import com.aajeevika.clf.databinding.ActivityWebviewBinding
import com.aajeevika.clf.utility.Constant

class ActivityWebView : BaseActivity<ActivityWebviewBinding>(R.layout.activity_webview) {
    private val webUrl by lazy { intent.getStringExtra(Constant.WEB_URL) ?: "" }
    private val title by lazy { intent.getStringExtra(Constant.TITLE) }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.title = title

        viewDataBinding.webView.run {
            settings.javaScriptEnabled = true
            webViewClient = CustomWebViewClient()
            loadUrl(webUrl)
        }
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        viewDataBinding.run {
            if(webView.canGoBack()) webView.goBack()
            else super.onBackPressed()
        }
    }

    private inner class CustomWebViewClient: WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return false
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            viewDataBinding.progressView.progressBarCircular.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            viewDataBinding.progressView.progressBarCircular.visibility = View.GONE
        }
    }
}