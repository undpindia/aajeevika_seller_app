package com.aajeevika.clf.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import com.aajeevika.clf.databinding.AlertDialogBinding
import com.aajeevika.clf.utility.UtilityActions

class AlertDialog(
    context: Context,
    message: String,
    positive: String,
    negative: String? = null,
    positiveClick: () -> Unit = {},
    negativeClick: () -> Unit = {},
    cancelOnOutsideClick: Boolean = false,
) :  Dialog(context) {
    private var viewDataBinding: AlertDialogBinding

    init {
        val layoutInflater = LayoutInflater.from(context)
        viewDataBinding = AlertDialogBinding.inflate(layoutInflater, null, false)
        viewDataBinding.let {
            it.message = message
            it.positive = positive
            it.negative = negative
        }

        setCancelable(cancelOnOutsideClick)
        setContentView(viewDataBinding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout((UtilityActions.windowWidth() * .9).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)

        viewDataBinding.positiveBtn.setOnClickListener {
            dismiss()
            positiveClick()
        }

        viewDataBinding.negativeBtn.setOnClickListener {
            dismiss()
            negativeClick()
        }
    }
}