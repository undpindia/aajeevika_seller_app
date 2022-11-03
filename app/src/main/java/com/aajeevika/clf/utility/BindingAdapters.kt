package com.aajeevika.clf.utility

import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.aajeevika.clf.R
import com.aajeevika.clf.view.application.ActivityWebView
import com.bumptech.glide.Glide
import java.lang.StringBuilder
import java.util.ArrayList

@BindingAdapter("app:setRegisterAgreementText")
fun TextView.setRegisterAgreementText(value: Boolean?) {
    if (value == true) {
        val spannableString = SpannableString(context.getString(R.string.register_agreement))
        val termsAndConditionsText = context.getString(R.string.terms_and_conditions)
        val privacyPolicyText = context.getString(R.string.privacy_policy)

        spannableString.setSpan(
            object : ClickableSpan() {
                override fun onClick(p0: View) {
                    val intent = Intent(context, ActivityWebView::class.java)
                    intent.putExtra(Constant.TITLE, context.getString(R.string.terms_and_conditions))
                    intent.putExtra(Constant.WEB_URL, BaseUrls.termsAndConditions)
                    context.startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = ContextCompat.getColor(this@setRegisterAgreementText.context, R.color.orange)
                }
            },
            spannableString.indexOf(termsAndConditionsText),
            spannableString.indexOf(termsAndConditionsText) + termsAndConditionsText.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE,
        )

        spannableString.setSpan(
            object : ClickableSpan() {
                override fun onClick(p0: View) {
                    val intent = Intent(context, ActivityWebView::class.java)
                    intent.putExtra(Constant.TITLE, context.getString(R.string.privacy_policy))
                    intent.putExtra(Constant.WEB_URL, BaseUrls.privacyPolicy)
                    context.startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = ContextCompat.getColor(this@setRegisterAgreementText.context, R.color.orange)
                }
            },
            spannableString.indexOf(privacyPolicyText),
            spannableString.indexOf(privacyPolicyText) + privacyPolicyText.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE,
        )

        text = spannableString
        movementMethod = LinkMovementMethod.getInstance()
    }
}

@BindingAdapter("app:dropDownMenu")
fun AutoCompleteTextView.enableDropDown(menu: ArrayList<String>?) {
    menu?.let {
        val adapter = ArrayAdapter(this.context, android.R.layout.simple_list_item_1, it)
        this.setAdapter(adapter)
        this.threshold = 1
        this.setOnClickListener {
            UtilityActions.closeKeyboard(context, this)
            this.showDropDown()
        }
    }
}

@BindingAdapter("app:disableSpace")
fun EditText.disableSpace(value: Boolean?) {
    if (value == true) {
        this.doOnTextChanged { text, _, _, _ ->
            if(text?.contains(" ") == true)
                this.setText(text.toString().replace(" ", ""))
        }
    }
}

@BindingAdapter(value = ["app:loadImageFromNetwork", "app:placeholder"], requireAll = false)
fun ImageView.loadImageFromNetwork(image: String?, placeholder: Drawable?) {
    if(!image.isNullOrEmpty()) {
        val requestBuilder = Glide.with(this).load(image)
        placeholder?.run { requestBuilder.placeholder(this) }
        requestBuilder.into(this)
    } else {
        setImageDrawable(placeholder)
    }
}

@BindingAdapter("app:camelCaseText")
fun TextView.camelCaseText(value: String?) {
    value?.let {
        val finalValue = StringBuilder()

        it.trim().split(" ").forEach { splitText ->
            if (splitText.isNotEmpty()) finalValue.append(splitText[0].uppercaseChar() + splitText.substring(1)).append(" ")
        }

        text = finalValue.trim().toString()
    }
}