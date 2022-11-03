package com.aajeevika.clf.view.home

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivitySearchProductBinding
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.view.home.adapter.SearchProductRecyclerViewAdapter
import com.aajeevika.clf.view.home.viewmodel.SearchViewModel
import java.util.*

class ActivitySearchProduct : BaseActivityVM<ActivitySearchProductBinding, SearchViewModel>(
    R.layout.activity_search_product,
    SearchViewModel::class,
) {
    private lateinit var timer: Timer
    private val searchProductRecyclerViewAdapter = SearchProductRecyclerViewAdapter { page ->
        viewModel.searchProduct(viewDataBinding.inputSearch.text.toString().trim(), page)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = searchProductRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }
    }

    override fun onResume() {
        super.onResume()
        UtilityActions.openKeyboard(this, viewDataBinding.inputSearch)
    }

    override fun observeData() {
        super.observeData()

        viewModel.searchLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            searchProductRecyclerViewAdapter.addData(it.data, it.current_page, it.last_page)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            inputSearch.doOnTextChanged { text, _, _, _ ->
                if (::timer.isInitialized) timer.cancel()
                searchProductRecyclerViewAdapter.resetData()

                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        if(!text.isNullOrEmpty() && text.length > 2) runOnUiThread {
                            viewModel.searchProduct(text.toString().trim())
                        }
                    }
                }, 200L)
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.searchProduct(viewDataBinding.inputSearch.text.toString().trim())
            }
        }
    }

    override fun onDestroy() {
        if (::timer.isInitialized) timer.cancel()
        super.onDestroy()
    }
}