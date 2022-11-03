package com.aajeevika.clf.view.grievance

import android.content.Intent
import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityCreateGrievanceBinding
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.view.dialog.AlertDialog
import com.aajeevika.clf.view.grievance.viewmodel.CreateGrievanceViewModel
import com.aajeevika.clf.view.home.ActivityDashboard

class ActivityCreateGrievance : BaseActivityVM<ActivityCreateGrievanceBinding, CreateGrievanceViewModel>(
    R.layout.activity_create_grievance,
    CreateGrievanceViewModel::class
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getGrievanceTypeList()
    }

    override fun observeData() {
        super.observeData()

        viewModel.grievanceListLiveData.observe(this, { list ->
            viewDataBinding.list = list.map { it.name }.toCollection(ArrayList())
        })

        viewModel.newGrievanceLiveData.observe(this, {
            AlertDialog(
                context = this,
                cancelOnOutsideClick = false,
                message = it,
                positive = getString(R.string.ok),
                positiveClick = { onBackPressed() }
            ).show()
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            submitBtn.setOnClickListener {
                val issue = inputIssue.text.toString().trim()
                val message = inputMessage.text.toString().trim()
                val issueId = viewModel.grievanceListLiveData.value?.firstOrNull { it.name == issue }?.id ?: -1

                validateFormField(issueId, message)?.run { root.showMessage(this) } ?: run {
                    viewModel.addGrievanceTicket(preferencesHelper.uid, issueId, message)
                }
            }
        }
    }

    private fun validateFormField(issue: Int, message: String): String? {
        return if(issue == -1 || message.isEmpty()) getString(R.string.fill_all_required_fields) else null
    }
}