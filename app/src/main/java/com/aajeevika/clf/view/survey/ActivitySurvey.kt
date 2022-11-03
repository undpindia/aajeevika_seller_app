package com.aajeevika.clf.view.survey

import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivitySurveyBinding
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.view.survey.adapter.SurveyRecyclerViewAdapter
import com.aajeevika.clf.view.survey.viewmodel.SurveyViewModel

class ActivitySurvey: BaseActivityVM<ActivitySurveyBinding, SurveyViewModel>(
    R.layout.activity_survey,
    SurveyViewModel::class
) {
    private val surveyRecyclerViewAdapter by lazy { SurveyRecyclerViewAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = surveyRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }

        viewModel.getSurveyList()
    }

    override fun observeData() {
        super.observeData()

        viewModel.surveyListLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            surveyRecyclerViewAdapter.addData(it)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getSurveyList()
            }
        }
    }
}