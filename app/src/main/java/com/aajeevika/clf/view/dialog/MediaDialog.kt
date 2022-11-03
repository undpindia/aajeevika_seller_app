package com.aajeevika.clf.view.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import com.aajeevika.clf.databinding.MediaDialogBinding
import com.aajeevika.clf.utility.UtilityActions

class MediaDialog(
    context: Context,
    cameraFileName: String,
    galleryCallback: ActivityResultLauncher<String>,
    cameraCallback: ActivityResultLauncher<Uri>,
    youtubeCallback: (String) -> Unit = {},
    youtubeInputEnabled: Boolean = false,
) : Dialog(context) {
    private var viewDataBinding: MediaDialogBinding

    init {
        val layoutInflater = LayoutInflater.from(context)
        viewDataBinding = MediaDialogBinding.inflate(layoutInflater, null, false)

        if (!youtubeInputEnabled) {
            viewDataBinding.saveBtn.visibility = View.GONE
            viewDataBinding.separator.visibility = View.GONE
            viewDataBinding.inputYoutubeUrlLayout.visibility = View.GONE
        }

        setContentView(viewDataBinding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout((UtilityActions.windowWidth() * .9).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)

        viewDataBinding.galleryBtn.setOnClickListener {
            dismiss()
            galleryCallback.launch("image/jpg")
        }

        viewDataBinding.cameraBtn.setOnClickListener {
            dismiss()

            UtilityActions.getPicturesFileUri(context as Activity, cameraFileName)?.let { uri ->
                cameraCallback.launch(uri)
            }
        }

        viewDataBinding.saveBtn.setOnClickListener {
            val youtubeUrl = viewDataBinding.inputYoutubeUrl.text.toString().trim()

            if (youtubeUrl.isNotEmpty()) {
                dismiss()
                youtubeCallback(youtubeUrl)
            }
        }
    }
}