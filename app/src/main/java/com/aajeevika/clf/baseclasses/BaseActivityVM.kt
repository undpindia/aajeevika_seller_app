package com.aajeevika.clf.baseclasses

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.clf.R
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction
import com.aajeevika.clf.view.auth.ActivityLogin
import com.aajeevika.clf.view.dialog.AlertDialog
import com.aajeevika.clf.view.dialog.ProgressDialog
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class BaseActivityVM<D : ViewDataBinding, V : BaseViewModel>(
    resourceId: Int,
    private val viewModelClass: KClass<V>
) : BaseActivity<D>(resourceId) {
    protected val viewModel: V by lazy { getViewModel(clazz = viewModelClass) }
    private val progressDialog by lazy { ProgressDialog.createDialog(this) }
    protected lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeData()

        onErrorReturn(null)
    }

    @CallSuper
    open fun observeData() {
        viewModel.progressHandler.observe(this, {
            when (it ?: ProgressAction.NONE) {
                ProgressAction.PROGRESS_DIALOG -> {
                    progressDialog.setMessage(it.message ?: getString(R.string.loading))
                    progressDialog.show()
                }
                ProgressAction.PROGRESS_BAR -> {
                    viewDataBinding.root.findViewById<ProgressBar>(R.id.progress_bar_circular)?.visibility = View.VISIBLE
                }
                ProgressAction.DISMISS -> {
                    progressDialog.dismiss()
                    viewDataBinding.root.findViewById<ProgressBar>(R.id.progress_bar_circular)?.visibility = View.GONE
                }
                ProgressAction.NONE -> {
                }
            }
        })

        viewModel.errorMessage.observe(this, {
            when (it.errorType) {
                ErrorType.DIALOG -> {
                    AlertDialog(
                        context = viewDataBinding.root.context,
                        message = it.message,
                        positive = getString(R.string.ok),
                        cancelOnOutsideClick = false,
                    ).show()
                }
                ErrorType.MESSAGE -> onErrorReturn(it.message)
                ErrorType.TOAST -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                ErrorType.LOGOUT -> {
                    preferencesHelper.clear()
                    UtilityActions.updateFCM(preferencesHelper)

                    val intent = Intent(this, ActivityLogin::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                ErrorType.NONE -> { }
            }
        })
    }
}
