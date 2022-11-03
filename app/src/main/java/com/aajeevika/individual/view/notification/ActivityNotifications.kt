package com.aajeevika.individual.view.notification

import android.os.Bundle
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityNotificationsBinding
import com.aajeevika.individual.view.notification.adapter.NotificationsRecyclerViewAdapter
import com.aajeevika.individual.view.notification.viewmodel.NotificationsViewModel

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