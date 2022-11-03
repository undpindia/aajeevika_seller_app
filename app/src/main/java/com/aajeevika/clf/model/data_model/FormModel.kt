package com.aajeevika.clf.model.data_model

class RegisterModel(
        var name: String? = null,
        var emailId: String? = null,
        var mobileNo: String? = null,
        var password: String? = null,
        var confirmPassword: String? = null
)
class LoginModel(
        var phoneOrEmail: String? = null,
        var password: String? = null
)