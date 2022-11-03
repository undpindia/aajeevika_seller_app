package com.aajeevika.clf.view.interest

import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityInterestsDetailsBinding
import com.aajeevika.clf.model.data_model.Interest
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.view.interest.adapter.InterestDetailsProductRecyclerViewAdapter
import com.aajeevika.clf.view.interest.viewmodel.MyInterestsViewModel

class ActivityInterestDetails : BaseActivityVM<ActivityInterestsDetailsBinding, MyInterestsViewModel>(
    R.layout.activity_interests_details,
    MyInterestsViewModel::class
) {
    private val interestId by lazy { intent.getIntExtra(Constant.INTEREST_ID, -1) }
    private val interestDetailsProductRecyclerViewAdapter = InterestDetailsProductRecyclerViewAdapter()
    private lateinit var interest: Interest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.adapter = interestDetailsProductRecyclerViewAdapter

        viewModel.getInterestById(interestId)
    }

    override fun observeData() {
        super.observeData()

        viewModel.interestsDetailLiveData.observe(this, {
            interest = it

            viewDataBinding.run {
                interestDetailsProductRecyclerViewAdapter.addData(it.items)

                UtilityActions.parseInterestDate(it.created_at)?.let { date ->
                    interestDate = UtilityActions.formatInterestDate(date)
                }

                interestId = it.interest_Id
                buyerName = it.buyer.name
                buyerEmail = it.buyer.email
                buyerMobile = it.buyer.mobile
                message = it.message
            }
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            callNowBtn.setOnClickListener {
                UtilityActions.redirectTo(this@ActivityInterestDetails, "tel: ${interest.buyer.mobile}")
            }
        }
    }
}