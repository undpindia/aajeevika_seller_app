package com.aajeevika.clf.baseclasses

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aajeevika.clf.R
import com.aajeevika.clf.utility.AppPreferencesHelper
import com.aajeevika.clf.utility.LocaleHelper
import org.koin.android.ext.android.inject

abstract class BaseActivity<D : ViewDataBinding>(private val resourceId: Int) : AppCompatActivity() {
    protected val preferencesHelper: AppPreferencesHelper by inject()
    protected lateinit var viewDataBinding: D

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, resourceId)
        viewDataBinding.lifecycleOwner = this

        initListener()
    }

    protected fun onErrorReturn(message: String?) {
        viewDataBinding.root.run {
            if((findViewById<RecyclerView>(R.id.recycler_View)?.layoutManager?.childCount ?: 0) > 0) return@run

            findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)?.isRefreshing = false
            findViewById<TextView>(R.id.error_message_txt)?.text = message
        }
    }

    protected fun setError(message: String?) {
        viewDataBinding.root.run {
            findViewById<TextView>(R.id.error_message_txt)?.text = message
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.updateLanguage(newBase, preferencesHelper.appLanguage))
    }

    protected fun stopSwipeToRefreshRefresh() {
        findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)?.isRefreshing = false
        findViewById<TextView>(R.id.error_message_txt)?.text = null
    }

    protected fun clearErrorMessage() {
        findViewById<TextView>(R.id.error_message_txt)?.text = null
    }

    open fun initListener() {}
}
