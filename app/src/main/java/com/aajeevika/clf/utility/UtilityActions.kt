package com.aajeevika.clf.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.aajeevika.clf.BuildConfig
import com.aajeevika.clf.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object UtilityActions {
    fun windowWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    fun isValidMobileNumber(mobileNumber: String): Boolean {
        return mobileNumber.toLongOrNull() != null && mobileNumber.length == 10
    }

    fun openKeyboard(context: Context, view: EditText) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        view.postDelayed({
            view.requestFocus()
            inputMethodManager.showSoftInput(view, 0)
        }, 50)
    }

    fun closeKeyboard(context: Context, view: View) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        view.clearFocus()
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * In case if log is allowed, displays the [otp] in a [Toast]. To enable/disable the Logs configure the [BuildConfig.LOG].
     */
    fun Context.debugOtp(otp: Int?) {
        Toast.makeText(this, "OTP: $otp", Toast.LENGTH_SHORT).show()
    }

    /**
     * Display a snack bar on the top on the screen.
     */
    fun View.showMessage(message: String?) {
        message?.let {
            Snackbar.make(this, message, Snackbar.LENGTH_SHORT).run {
                animationMode = Snackbar.ANIMATION_MODE_SLIDE
                show()
            }
        }
    }

    /**
     * Compare the size of a [file] with the [maxSize] allowed and returns "true" if [file] size is
     * less than [maxSize] otherwise return "false".
     */
    fun validateFileSize(file: File, maxSize: Float): Boolean = run {
        val fileSize = file.length()/1048576F
        fileSize < maxSize
    }

    /**
     * Get uri for the file to save the picture clicked from the camera and store that to the private directory of the application.
     * That can't be accessed by outside or any other application.
     */
    fun getPicturesFileUri(activity: Activity, fileName: String): Uri? = run {
        val tempFile = try {
            File(activity.cacheDir, fileName).apply {
                if(!exists()) createNewFile()
            }
        } catch (e: Exception) { null }

        tempFile?.let {
            FileProvider.getUriForFile(activity, "${activity.packageName}.fileprovider", it)
        } ?: run {
            null
        }
    }

    /**
     * Redirects to the [url] to display the data. See [Intent.ACTION_VIEW].
     */
    fun redirectTo(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    /**
     * Build and return an thumbnail of an video using the video id. Video id is extracted using [getYoutubeId].
     */
    fun getVideoThumbnail(url: String?) = run {
        "https://img.youtube.com/vi/${getYoutubeId(url)}/0.jpg"
    }

    /**
     * Extract the youtube video id from an [url].
     */
    private fun getYoutubeId(url: String?): String? {
        return url?.let {
            val pattern = "https?://(?:[0-9A-Z-]+\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|</a>))[?=&+%\\w]*"
            val compiledPattern: Pattern = Pattern.compile(
                pattern,
                Pattern.CASE_INSENSITIVE
            )
            val matcher: Matcher = compiledPattern.matcher(url)

            if (matcher.find()) matcher.group(1)
            else null
        }
    }

    fun startUcropActivityForResult(
        activity: Activity,
        sourceFile: Uri,
        destinationUri: Uri,
        activityResultCallback: ActivityResultLauncher<Intent>,
        aspectRatioX: Float = 1F,
        aspectRatioY: Float = 1F,
        setCircular: Boolean = false
    ) {
        val uCropOptions = UCrop.Options().apply {
            setCompressionQuality(40)
            setCircleDimmedLayer(setCircular)
            setActiveControlsWidgetColor(ContextCompat.getColor(activity, R.color.orange))
        }

        val uCropIntent = UCrop.of(sourceFile, destinationUri)
            .withAspectRatio(aspectRatioX, aspectRatioY)
            .withOptions(uCropOptions)
            .getIntent(activity)

        activityResultCallback.launch(uCropIntent)
    }

    fun formatDate(date: Date) : String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatInterestDate(date: Date) : String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy | hh:mm a", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatTicketDate(date: Date) : String {
        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatChatDate(date: Date) : String {
        val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun parseDate(date: String) : Date? {
        if(date.isEmpty()) return null

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.parse(date)
    }

    fun parseInterestDate(date: String) : Date? {
        if(date.isEmpty()) return null

        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        return dateFormat.parse(date)
    }

    fun multipartImage(file: File, partName: String) : MultipartBody.Part {
        return MultipartBody.Part.createFormData(partName, file.name, file.asRequestBody("image/jpg".toMediaTypeOrNull()))
    }

    fun getLatLngFromAddress(context: Context, address: String) : Address? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addressList = geocoder.getFromLocationName(address, 1)
        return if(addressList.isNotEmpty()) addressList[0] else null
    }

    /**
     * Deletes the old "FCM" token and generate a new token and save to the preferences using [PreferencesHelper].
     */
    fun updateFCM(preferences: AppPreferencesHelper) {
        FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                it.result?.let { token -> preferences.fcmToken = token }
            }
        }
    }
}