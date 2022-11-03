package com.aajeevika.clf.view.product

import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivitySubCategoryBinding
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.view.product.adapter.SubCategoryRecyclerViewAdapter
import com.aajeevika.clf.view.product.viewmodel.SubCategoryViewModel

class ActivitySubCategory : BaseActivityVM<ActivitySubCategoryBinding, SubCategoryViewModel>(
    R.layout.activity_sub_category,
    SubCategoryViewModel::class
) {
    private val subCategoryId by lazy { intent.getIntExtra(Constant.SUB_CATEGORY_ID, -1) }
    private val title by lazy { intent.getStringExtra(Constant.TITLE) }

    private val subCategoryRecyclerViewAdapter = SubCategoryRecyclerViewAdapter { page ->
        viewModel.getSubCategory(preferencesHelper.uid, subCategoryId, page)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.title = title

        viewDataBinding.recyclerView.run {
            adapter = subCategoryRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }

        viewModel.getSubCategory(preferencesHelper.uid, subCategoryId)
    }

    override fun observeData() {
        super.observeData()

        viewModel.subCategoryLiveData.observe(this, {
            stopSwipeToRefreshRefresh()

            subCategoryRecyclerViewAdapter.addData(
                it.products,
                it.pagination.current_page,
                it.pagination.last_page,
            )
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getSubCategory(preferencesHelper.uid, subCategoryId)
            }
        }
    }
}