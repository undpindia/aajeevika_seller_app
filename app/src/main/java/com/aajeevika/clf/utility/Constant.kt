package com.aajeevika.clf.utility

import android.content.Context
import com.aajeevika.clf.R

object Constant {
    const val DEFAULT_COUNTRY_ID = 101
    const val DEFAULT_STATE_ID = 39

    const val AADHAR_CARD_FRONT = "aadhar_card_front.jpg"
    const val AADHAR_CARD_BACK = "aadhar_card_back.jpg"
    const val USER_PROFILE = "user_profile.jpg"
    const val PAN_CARD = "pan_card.jpg"
    const val BRN_CARD = "brn_card.jpg"

    const val CERTIFICATE_NAME_1 = "certificate_name_1.jpg"
    const val CERTIFICATE_NAME_2 = "certificate_name_2.jpg"
    const val CERTIFICATE_NAME_3 = "certificate_name_3.jpg"
    const val CERTIFICATE_NAME_4 = "certificate_name_4.jpg"
    const val CERTIFICATE_NAME_5 = "certificate_name_5.jpg"

    const val VERIFICATION_TYPE = "VERIFICATION_TYPE"
    const val TICKET_ID_DISPLAY = "TICKET_ID_DISPLAY"
    const val SUB_CATEGORY_ID = "SUB_CATEGORY_ID"
    const val IS_TO_ADD_SALE = "IS_TO_ADD_SALE"
    const val MOBILE_NUMBER = "MOBILE_NUMBER"
    const val DOCUMENT_TYPE = "DOCUMENT_TYPE"
    const val PRODUCT_DATA = "PRODUCT_DATA"
    const val INTEREST_ID = "INTEREST_ID"
    const val PRODUCT_ID = "PRODUCT_ID"
    const val USER_ROLE = "USER_ROLE"
    const val TICKET_ID = "TICKET_ID"
    const val EMAIL_ID = "EMAIL_ID"
    const val ORDER_ID = "ORDER_ID"
    const val WEB_URL = "WEB_URL"
    const val ADDRESS = "ADDRESS"
    const val TITLE = "TITLE"
    const val OTP = "OTP"

    const val CLF_ROLE_ID = 2
    const val SHG_ENTERPRISE_ROLE_ID = 3
    const val SARAS_CENTER_ROLE_ID = 7
    const val GROWTH_CENTER_ROLE_ID = 8

    fun getUserTypes(context: Context): ArrayList<Pair<Int, String>> = run {
        arrayListOf(
            Pair(CLF_ROLE_ID, context.getString(R.string.clf)),
            Pair(SHG_ENTERPRISE_ROLE_ID, context.getString(R.string.shg_enterprise)),
            Pair(SARAS_CENTER_ROLE_ID, context.getString(R.string.saras_centre)),
            Pair(GROWTH_CENTER_ROLE_ID, context.getString(R.string.growth_centre)),
        )
    }
}