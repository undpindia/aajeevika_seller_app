package com.aajeevika.clf.view.grievance

import android.content.Intent
import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityGrievanceBinding
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.view.grievance.adapter.GrievanceRecyclerViewAdapter
import com.aajeevika.clf.view.grievance.viewmodel.GrievanceViewModel

class ActivityGrievance : BaseActivityVM<ActivityGrievanceBinding, GrievanceViewModel>(
    R.layout.activity_grievance,
    GrievanceViewModel::class
) {
    private val grievanceRecyclerViewAdapter = GrievanceRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.run {
            adapter = grievanceRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getGrievanceTypeList(preferencesHelper.uid)
    }

    override fun observeData() {
        super.observeData()

        viewModel.newGrievanceLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            grievanceRecyclerViewAdapter.addData(it)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            raiseTicketBtn.setOnClickListener {
                val intent = Intent(this@ActivityGrievance, ActivityCreateGrievance::class.java)
                startActivity(intent)
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getGrievanceTypeList(preferencesHelper.uid)
            }
        }
    }
}