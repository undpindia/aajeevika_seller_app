package com.aajeevika.clf.view.application

import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityNotificationsBinding
import com.aajeevika.clf.view.application.adapter.NotificationsRecyclerViewAdapter
import com.aajeevika.clf.view.application.viewmodel.NotificationsViewModel

class ActivityNotifications : BaseActivityVM<ActivityNotificationsBinding, NotificationsViewModel>(
    R.layout.activity_notifications,
    NotificationsViewModel::class
) {
    private val notificationsRecyclerViewAdapter = NotificationsRecyclerViewAdapter {
        viewModel.getNotificationList(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.adapter = notificationsRecyclerViewAdapter

        viewModel.getNotificationList()
    }

    override fun observeData() {
        super.observeData()

        viewModel.notificationLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            notificationsRecyclerViewAdapter.addData(it.getnotification, it.pagination.current_page, it.pagination.last_page)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getNotificationList()
            }
        }
    }
}