package com.aajeevika.clf.model.data_model

import java.io.Serializable

data class OtpModel(
    var otp: Int? = null,
)

data class UserProfileModel(
    var user: UserData? = null,
    var address: Address? = null,
    var rating: RatingData? = null,
    var is_otp_verified: Int? = null,
    var mobile: String? = null,
    var otp: Int? = null,
)

data class UserData(
    var id: Int?,
    var name: String?,
    var email: String?,
    var mobile: String?,
    var member_id: String?,
    var organization_name: String?,
    var member_designation: String?,
    var email_verified_at: String?,
    var profileImage: String?,
    var role_id: Int?,
    var title: String?,
    var district: String?,
    var language: String?,
    var api_token: String?,
    var isActive: Int?,
    var adhar_card_front_file: String?,
    var adhar_card_back_file: String?,
    var pancard_file: String?,
    var brn_file: String?,
    var is_document_added: Int?,
    var is_document_verified: Int?,
    var is_address_added: Int?,
    var is_adhar_verify: Int?,
    var is_pan_verify: Int?,
    var is_brn_verify: Int?,
    var is_aadhar_added: Int?,
    var is_pan_added: Int?,
    var is_brn_added: Int?,
    var lat: Double?,
    var log: Double?,
)

data class Address(
    var personal: AddressData? = null,
    var registered: AddressData? = null,
)

data class AddressData(
    var id: Int? = null,
    var address_line_one: String? = null,
    var address_line_two: String? = null,
    var pincode: String? = null,
    var countryId: Int? = null,
    var stateId: Int? = null,
    var districtId: Int? = null,
    var blockId: Int? = null,
    var country: String? = null,
    var block: String? = null,
    var state: String? = null,
    var district: String? = null,
) : Serializable

data class RatingData(
    val reviewCount: String,
    val ratingAvgStar: String,
)