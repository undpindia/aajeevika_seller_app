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

abstract class BaseFragmentVM<D : ViewDataBinding, V : BaseViewModel>(
    resourceId: Int,
    private val viewModelClass: KClass<V>,
) : BaseFragment<D>(resourceId) {
    protected val viewModel: V by lazy { getViewModel(clazz = viewModelClass) }
    private val progressDialog by lazy { ProgressDialog.createDialog(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.lifecycleOwner = this
        observeData()

        onErrorReturn(null)
    }

    @CallSuper
    open fun observeData() {
        viewModel.progressHandler.observe(requireActivity(), {
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
                    viewDataBinding.root.run {
                        if((findViewById<RecyclerView>(R.id.recycler_View)?.layoutManager?.childCount ?: 0) > 0) {
                            findViewById<TextView>(R.id.error_message_txt)?.text = null
                        }

                        findViewById<ProgressBar>(R.id.progress_bar_circular)?.visibility = View.GONE
                    }
                }
                ProgressAction.NONE -> {
                }
            }
        })

        viewModel.errorMessage.observe(requireActivity(), {
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
                ErrorType.TOAST -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                ErrorType.NONE -> { }
                ErrorType.LOGOUT -> {
                    preferencesHelper.clear()
                    UtilityActions.updateFCM(preferencesHelper)

                    val intent = Intent(requireContext(), ActivityLogin::class.java)
                    startActivity(intent)
                    requireActivity().finishAffinity()
                }
            }
        })
    }
}
