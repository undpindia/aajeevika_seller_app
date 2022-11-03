package com.aajeevika.clf.view.application

import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityFaqsBinding
import com.aajeevika.clf.databinding.ActivityNotificationsBinding
import com.aajeevika.clf.view.application.adapter.FaqsRecyclerViewAdapter
import com.aajeevika.clf.view.application.viewmodel.FaqViewModel
import com.aajeevika.clf.view.application.viewmodel.NotificationsViewModel

class ActivityFaqs : BaseActivityVM<ActivityFaqsBinding, FaqViewModel>(
    R.layout.activity_faqs,
    FaqViewModel::class
) {
    private val faqsRecyclerViewAdapter = FaqsRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.adapter = faqsRecyclerViewAdapter
    }

    override fun observeData() {
        super.observeData()
    }

    override fun initListener() {
        viewDataBinding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}