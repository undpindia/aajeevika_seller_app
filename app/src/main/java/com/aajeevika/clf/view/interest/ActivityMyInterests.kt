package com.aajeevika.clf.view.interest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityMyInterestsBinding
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.view.interest.adapter.MyInterestsRecyclerViewAdapter
import com.aajeevika.clf.view.interest.viewmodel.MyInterestsViewModel
import com.google.gson.Gson

class ActivityMyInterests : BaseActivityVM<ActivityMyInterestsBinding, MyInterestsViewModel>(
    R.layout.activity_my_interests,
    MyInterestsViewModel::class
) {
    private val isToAddSale by lazy { intent.getBooleanExtra(Constant.IS_TO_ADD_SALE, false) }
    private val myInterestsRecyclerViewAdapter by lazy {
        MyInterestsRecyclerViewAdapter(isToAddSale) {
            val intent = Intent()
            intent.putExtra(Constant.INTEREST_ID, it)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.title = if (isToAddSale) getString(R.string.select_interest_id) else getString(R.string.my_interest)

        viewDataBinding.recyclerView.run {
            adapter = myInterestsRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F, 8F, 8F, 8F))
        }

        viewModel.getMyInterests()
    }

    override fun observeData() {
        super.observeData()

        viewModel.myInterestsLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            myInterestsRecyclerViewAdapter.addData(it.interest_list)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getMyInterests()
            }
        }
    }
}