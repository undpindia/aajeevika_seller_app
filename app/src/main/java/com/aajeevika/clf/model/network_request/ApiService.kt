package com.aajeevika.clf.model.network_request

import com.aajeevika.clf.model.data_model.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/get_countries")
    suspend fun getCountries(): Response<BaseDataModel<CountryModel>>

    @FormUrlEncoded
    @POST("api/get_state")
    suspend fun getState(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<StateModel>>

    @FormUrlEncoded
    @POST("api/get_city")
    suspend fun getCity(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<CityModel>>

    @FormUrlEncoded
    @POST("api/get-block")
    suspend fun getBlock(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<BlockModel>>

    @Multipart
    @POST("api/registration")
    suspend fun registerUser(
        @PartMap fields: HashMap<String, RequestBody>,
        @Part file: MultipartBody.Part,
    ): Response<BaseDataModel<OtpModel>>

    @FormUrlEncoded
    @POST("api/login")
    suspend fun loginUser(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<UserProfileModel>>

    @FormUrlEncoded
    @POST("api/verifyotp")
    suspend fun verifyOtp(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<UserProfileModel>>

    @FormUrlEncoded
    @POST("api/updatemobile")
    suspend fun updateMobileNumber(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/resendotp")
    suspend fun resendOtp(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<OtpModel>>

    @FormUrlEncoded
    @POST("api/forgetpassword")
    suspend fun forgotPassword(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<OtpModel>>

    @FormUrlEncoded
    @POST("api/changepassword")
    suspend fun changePassword(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/getnotification")
    suspend fun getNotification(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<NotificationDataModel>>

    @GET("api/getprofile")
    suspend fun getProfile(): Response<BaseDataModel<UserProfileModel>>

    @FormUrlEncoded
    @POST("api/updateuserprofile")
    suspend fun updateUserProfile(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/updateaddress")
    suspend fun updateAddress(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @Multipart
    @POST("api/updateprofileimage")
    suspend fun uploadProfileImage(@Part file: MultipartBody.Part): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/getshgartisanhome")
    suspend fun getHomeData(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<HomeDataModel>>

    @FormUrlEncoded
    @POST("api/viewsartisancategoryproduct")
    suspend fun getSubCategoryProducts(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<SubCategoryDataModel>>

    @FormUrlEncoded
    @POST("api/viewproduct")
    suspend fun getProductDetails(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<ProductDetailDataModel>>

    @GET("api/getcategory")
    suspend fun getCategory(): Response<BaseDataModel<ProductCategoryResponse>>

    @FormUrlEncoded
    @POST("api/getsubcategory")
    suspend fun getSubCategory(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<ProductCategoryResponse>>

    @FormUrlEncoded
    @POST("api/getmaterial")
    suspend fun getMaterial(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<MaterialResponse>>

    @FormUrlEncoded
    @POST("api/gettemplate")
    suspend fun getTemplate(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<TemplateResponse>>

    @POST("api/interest_list")
    suspend fun getMyInterests(): Response<BaseDataModel<InterestListDataModel>>

    @FormUrlEncoded
    @POST("api/get_interest_byid")
    suspend fun getInterestById(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<InterestDetailDataModel>>

    @FormUrlEncoded
    @POST("api/get_seller_product")
    suspend fun getSellerProducts(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<SalesSellerProductsDataModel>>

    @GET("api/getdraftproduct")
    suspend fun getDraftProduct(): Response<BaseDataModel<ProductDraftResponse>>

    @GET("api/get-survey-list")
    suspend fun getSurveyList(): Response<BaseDataModel<SurveyDataModel>>

    @FormUrlEncoded
    @POST("api/removedraftproduct")
    suspend fun removeDraftProduct(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @POST("api/order_list")
    suspend fun getCompletedOrderList(): Response<BaseDataModel<MyOrdersListData>>

    @FormUrlEncoded
    @POST("api/get_order_byid")
    suspend fun getOrderById(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<OrderDetailsListData>>

    @FormUrlEncoded
    @POST("api/order_list")
    suspend fun getSales(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<SalesListData>>

    @FormUrlEncoded
    @POST("api/searchshg")
    suspend fun searchProduct(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<SearchDataModel>>

    @FormUrlEncoded
    @POST("api/get_reviews")
    suspend fun getReviews(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<RatingDataModel>>

    @FormUrlEncoded
    @POST("api/get-clf-ind-order-list")
    suspend fun getIndividualSalesList(@FieldMap body: HashMap<String, Any> = HashMap()): Response<BaseDataModel<BuyManagerDataModel>>

    @FormUrlEncoded
    @POST("api/get-clf-ind-order-details")
    suspend fun getIndividualSaleDetails(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<BuyDetailDataModel>>

    @FormUrlEncoded
    @POST("api/add-ind-rating")
    suspend fun addRatingToSale(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/get-ind-ividual-user-list")
    suspend fun getShgIndList(@FieldMap body: HashMap<String, Any> = HashMap()): Response<BaseDataModel<ShgIndDataModel>>

    @FormUrlEncoded
    @POST("api/get-collection-center-list")
    suspend fun getCollectionCenterList(@FieldMap body: HashMap<String, Any> = HashMap()): Response<BaseDataModel<CollectionCenterDataModel>>

    @FormUrlEncoded
    @POST("api/get-certificate-type-list")
    suspend fun getCertificateTypeList(@FieldMap body: HashMap<String, Any> = HashMap()): Response<BaseDataModel<CertificateTypeDataModel>>

    @FormUrlEncoded
    @POST("api/get-grievance-type-list")
    suspend fun getGrievanceTypeList(@FieldMap body: HashMap<String, Any> = HashMap()): Response<BaseDataModel<GrievanceTypeDataModel>>

    @FormUrlEncoded
    @POST("api/add-grievance-ticket")
    suspend fun addGrievanceTicket(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/get-ticket-list")
    suspend fun getTicketList(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<TicketsDataModel>>

    @FormUrlEncoded
    @POST("api/get-ticket-chat-list")
    suspend fun getTicketChatList(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<GrievanceChatDataModel>>

    @FormUrlEncoded
    @POST("api/add-grievance-message")
    suspend fun sendMessage(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @POST("api/add-new-sale")
    suspend fun addNewSale(@Body data: JsonObject): Response<BaseDataModel<AddNewSaleResponse>>

    @Multipart
    @POST("api/addproduct")
    suspend fun addProduct(
        @PartMap fields: HashMap<String, RequestBody>,
        @Part files: ArrayList<MultipartBody.Part>,
    ): Response<BaseDataModel<Any>>

    @Multipart
    @POST("api/updatedraftproduct")
    suspend fun updateDraft(
        @PartMap fields: HashMap<String, RequestBody>,
        @Part files: ArrayList<MultipartBody.Part> = ArrayList()
    ): Response<BaseDataModel<Any>>

    @Multipart
    @POST("api/adddocumentandaddress")
    suspend fun addDocumentAndAddress(
        @PartMap fields: HashMap<String, RequestBody>,
        @Part files: ArrayList<MultipartBody.Part> = ArrayList()
    ): Response<BaseDataModel<UserProfileModel>>

    @Multipart
    @POST("api/updatedocument")
    suspend fun reUploadDocument(
        @PartMap fields: HashMap<String, RequestBody>,
        @Part files: ArrayList<MultipartBody.Part> = ArrayList()
    ): Response<BaseDataModel<UserProfileModel>>
}