package com.aajeevika.clf.baseclasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aajeevika.clf.R
import com.aajeevika.clf.utility.AppPreferencesHelper
import org.koin.android.ext.android.get

abstract class BaseFragment<D : ViewDataBinding>(private val resourceId: Int) : Fragment() {
    protected val preferencesHelper: AppPreferencesHelper = get()
    protected lateinit var viewDataBinding: D

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, resourceId, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    protected fun onErrorReturn(message: String?) {
        viewDataBinding.root.run {
            findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)?.isRefreshing = false
            findViewById<TextView>(R.id.error_message_txt)?.text = message
        }
    }

    protected fun setError(message: String?) {
        viewDataBinding.root.run {
            findViewById<TextView>(R.id.error_message_txt)?.text = message
        }
    }

    protected fun stopSwipeToRefreshRefresh() {
        viewDataBinding.root.run {
            findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)?.isRefreshing = false
            findViewById<TextView>(R.id.error_message_txt)?.text = null
        }
    }

    open fun initListener() {}
}
