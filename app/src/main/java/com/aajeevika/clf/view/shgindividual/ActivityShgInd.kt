package com.aajeevika.clf.view.shgindividual

import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityShgIndividualsBinding
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.view.shgindividual.adapter.ShgIndRecyclerViewAdapter
import com.aajeevika.clf.view.shgindividual.viewmodel.ShgIndViewModel

class ActivityShgInd : BaseActivityVM<ActivityShgIndividualsBinding, ShgIndViewModel>(
    R.layout.activity_shg_individuals,
    ShgIndViewModel::class
) {
    private val shgIndRecyclerViewAdapter = ShgIndRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = shgIndRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }

        viewModel.getIndividualSalesList()
    }

    override fun observeData() {
        super.observeData()

        viewModel.shgListLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            shgIndRecyclerViewAdapter.addData(it.individual_list)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getIndividualSalesList()
            }
        }
    }
}