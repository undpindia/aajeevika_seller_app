package com.aajeevika.clf.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import com.aajeevika.clf.databinding.ImagePreviewDialogBinding
import com.aajeevika.clf.utility.UtilityActions

class ImagePreviewDialog(context: Context, documentImageOne: String, documentImageTwo: String? = null) : Dialog(context) {

    init {
        val layoutInflater = LayoutInflater.from(context)
        val viewDataBinding = ImagePreviewDialogBinding.inflate(layoutInflater, null, false)

        viewDataBinding.documentImageOne = documentImageOne
        viewDataBinding.documentImageTwo = documentImageTwo

        setContentView(viewDataBinding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout((UtilityActions.windowWidth() * .9).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)

        viewDataBinding.closeBtn.setOnClickListener {
            dismiss()
        }
    }
}