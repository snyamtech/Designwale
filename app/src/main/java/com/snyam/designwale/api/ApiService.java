package com.snyam.designwale.api;

import androidx.lifecycle.LiveData;

import com.snyam.designwale.items.AppInfo;
import com.snyam.designwale.items.BusinessCategoryItem;
import com.snyam.designwale.items.BusinessItem;
import com.snyam.designwale.items.BusinessSubCategoryItem;
import com.snyam.designwale.items.CashFreeOrder;
import com.snyam.designwale.items.CategoryItem;
import com.snyam.designwale.items.CouponItem;
import com.snyam.designwale.items.CustomCategory;
import com.snyam.designwale.items.CustomModel;
import com.snyam.designwale.items.DynamicFrameItem;
import com.snyam.designwale.items.FestivalItem;
import com.snyam.designwale.items.FrameCategoryItem;
import com.snyam.designwale.items.HomeItem;
import com.snyam.designwale.items.ItemVcard;
import com.snyam.designwale.items.LanguageItem;
import com.snyam.designwale.items.MainStrModel;
import com.snyam.designwale.items.NewsItem;
import com.snyam.designwale.items.PersonalItem;
import com.snyam.designwale.items.PostItem;
import com.snyam.designwale.items.ProductModel;
import com.snyam.designwale.items.ReferDetail;
import com.snyam.designwale.items.StickerItem;
import com.snyam.designwale.items.StoryItem;
import com.snyam.designwale.items.SubjectItem;
import com.snyam.designwale.items.SubsPlanItem;
import com.snyam.designwale.items.UploadItem;
import com.snyam.designwale.items.UserFrame;
import com.snyam.designwale.items.UserItem;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //********* User Login ********
    @FormUrlEncoded
    @POST("{API_KEY}/login")
    LiveData<ApiResponse<UserItem>> postUserLogin(@Path("API_KEY") String apiKey,
                                                  @Field("email") String userEmail,
                                                  @Field("password") String userPassword);

    //********* User for Register **********
    @FormUrlEncoded
    @POST("{API_KEY}/registration")
    Call<UserItem> postUser(@Path("API_KEY") String apiKey,
                            @Field("name") String userName,
                            @Field("email") String userEmail,
                            @Field("password") String userPassword,
                            @Field("mobile_no") String userPhone);

    //******** Recent Code ********
    @FormUrlEncoded
    @POST("{API_KEY}/resend-verify-code")
    Call<ApiStatus> resentCodeAgain(
            @Path("API_KEY") String API_KEY,
            @Field("userId") String userId
    );

    //******** Verify Email ********
    @FormUrlEncoded
    @POST("{API_KEY}/verify-account")
    LiveData<ApiResponse<ApiStatus>> verifyEmail(
            @Path("API_KEY") String API_KEY,
            @Field("userId") String userId,
            @Field("code") String code);

    //******** Password Update *******
    @FormUrlEncoded
    @POST("{API_KEY}/change-password")
    LiveData<ApiResponse<ApiStatus>> postPasswordUpdate(@Path("API_KEY") String apiKey,
                                                        @Field("userId") String loginUserId,
                                                        @Field("newPassword") String password);

    //******* POST Forgot Password *******
    @FormUrlEncoded
    @POST("{API_KEY}/forgot-password")
    LiveData<ApiResponse<ApiStatus>> postForgotPassword(@Path("API_KEY") String apiKey, @Field("email") String userEmail);


    //********* Get User Data ********
    @GET("{API_KEY}/user?")
    LiveData<ApiResponse<UserItem>> getUserById(@Path("API_KEY") String apiKey, @Query("id") String user_id);

    //********* Login with google ********
    @FormUrlEncoded
    @POST("{API_KEY}/google-registration")
    Call<UserItem> postGoogleUser(
            @Path("API_KEY") String API_KEY,
            @Field("name") String userName,
            @Field("email") String userEmail,
            @Field("image") String profilePhotoUrl);

    //********* Login with Mobile ********
    @FormUrlEncoded
    @POST("{API_KEY}/phone-login")
    Call<UserItem> postMobileUser(
            @Path("API_KEY") String API_KEY,
            @Field("name") String userName,
            @Field("email") String userEmail,
            @Field("phoneNumber") String mobile);

    //********* POST Upload Image *********
    @Multipart
    @POST("{API_KEY}/profile-update")
    LiveData<ApiResponse<UserItem>> doUploadImage(@Path("API_KEY") String apiKey,
                                                  @Part("id") RequestBody userId,
                                                  @Part("image") RequestBody name,
                                                  @Part MultipartBody.Part file,
                                                  @Part("name") RequestBody userName,
                                                  @Part("email") RequestBody userEmail,
                                                  @Part("mobile_no") RequestBody phone,
                                                  @Part("referralCode") RequestBody referralCode);

    //********** Get Category ********
    @GET("{API_KEY}/category")
    LiveData<ApiResponse<List<CategoryItem>>> getCategory(@Path("API_KEY") String apiKey, @Query("page") String page);

    //********** Get News ********
    @GET("{API_KEY}/news")
    LiveData<ApiResponse<List<NewsItem>>> getNews(@Path("API_KEY") String apiKey, @Query("page") String page);

    //********** Get Story ********
    @GET("{API_KEY}/story")
    LiveData<ApiResponse<List<StoryItem>>> getStory(@Path("API_KEY") String apiKey);

    //********** Get Festival ********
    @GET("{API_KEY}/festival")
    LiveData<ApiResponse<List<FestivalItem>>> getFestival(@Path("API_KEY") String apiKey, @Query("page") String page);

    //********* Get Business ******
    @GET("{API_KEY}/business")
    LiveData<ApiResponse<List<BusinessItem>>> getBusiness(@Path("API_KEY") String apiKey, @Query("userId") String userId);

    //******** Add Business *******
    @Multipart
    @POST("{API_KEY}/add-business")
    LiveData<ApiResponse<List<BusinessItem>>> addBusiness(@Path("API_KEY") String apiKey,
                                                          @Part("userId") RequestBody userId,
                                                          @Part("bussinessImage") RequestBody logo,
                                                          @Part MultipartBody.Part file,
                                                          @Part("bussinessName") RequestBody name,
                                                          @Part("bussinessEmail") RequestBody email,
                                                          @Part("bussinessNumber") RequestBody phone,
                                                          @Part("bussinessWebsite") RequestBody website,
                                                          @Part("bussinessAddress") RequestBody address,
                                                          @Part("businessCategoryId") RequestBody categoryId);

    //******** Update Business *******
    @Multipart
    @POST("{API_KEY}/update-business")
    LiveData<ApiResponse<List<BusinessItem>>> updateBusiness(@Path("API_KEY") String apiKey,
                                                             @Part("bussinessId") RequestBody userId,
                                                             @Part("bussinessImage") RequestBody logo,
                                                             @Part MultipartBody.Part file,
                                                             @Part("bussinessName") RequestBody name,
                                                             @Part("bussinessEmail") RequestBody email,
                                                             @Part("bussinessNumber") RequestBody phone,
                                                             @Part("bussinessWebsite") RequestBody website,
                                                             @Part("bussinessAddress") RequestBody address,
                                                             @Part("businessCategoryId") RequestBody categoryId);

    //******* Delete Business *********
    @FormUrlEncoded
    @POST("{API_KEY}/delete-business")
    Call<ApiStatus> deleteBusiness(@Path("API_KEY") String apiKey, @Field("bussinessId") String bussinessId);

    //******* Set Default Business *********
    @FormUrlEncoded
    @POST("{API_KEY}/set-default-business")
    Call<ApiStatus> setDefault(@Path("API_KEY") String apiKey, @Field("userId") String userId, @Field("bussinessId") String bussinessId);

    //********** Get Languages ********
    @GET("{API_KEY}/language")
    LiveData<ApiResponse<List<LanguageItem>>> getLanguages(@Path("API_KEY") String apiKey);

    //********** Get Post ********
    @GET("{API_KEY}/get-post")
    LiveData<ApiResponse<List<PostItem>>> getPost(@Path("API_KEY") String apiKey,
                                                  @Query("type") String type,
                                                  @Query("id") String id);

    //********** Get Plans ********
    @GET("{API_KEY}/subscription-plan")
    LiveData<ApiResponse<List<SubsPlanItem>>> getPlanData(@Path("API_KEY") String apiKey);

    //********** Get Contact Subject ********
    @GET("{API_KEY}/contact-subject")
    LiveData<ApiResponse<List<SubjectItem>>> getSubjectItems(@Path("API_KEY") String apiKey);

    //******** Send Contact *********
    @FormUrlEncoded
    @POST("{API_KEY}/contact-massage")
    Call<ApiStatus> sendContact(@Path("API_KEY") String apiKey,
                                @Field("name") String name,
                                @Field("email") String email,
                                @Field("mobileNo") String number,
                                @Field("message") String massage,
                                @Field("subjectId") String subjectId);

    //******** Send Payment *********
    @FormUrlEncoded
    @POST("{API_KEY}/create-payment")
    Call<ApiStatus> loadPayment(@Path("API_KEY") String apiKey,
                                @Field("userId") String userId,
                                @Field("planId") String planId,
                                @Field("paymentId") String paymentId,
                                @Field("paymentAmount") String planPrice,
                                @Field("code") String couponCode,
                                @Field("paymentType") String paymentType,
                                @Field("referralCode") String referralCode);

    //******* Get App Info *********
    @GET("{API_KEY}/app-about")
    LiveData<ApiResponse<AppInfo>> getAppInfo(@Path("API_KEY") String apiKey);

    //******* Get Custom Category *********
    @GET("{API_KEY}/custom-category")
    LiveData<ApiResponse<List<CustomCategory>>> getCustomCategory(@Path("API_KEY") String apiKey, @Query("page") String page);

    //******* Get Custom Post *********
    @GET("{API_KEY}/custom-frame")
    LiveData<ApiResponse<List<PostItem>>> getCustomPost(@Path("API_KEY") String apiKey, @Query("id") String festId);

    //******** Get Custom All Data ******
    @GET("{API_KEY}/custom-post")
    LiveData<ApiResponse<CustomModel>> getAllCustom(@Path("API_KEY") String apiKey);

    //******** Get Home Data *****
    @GET("{API_KEY}/get-home-data")
    LiveData<ApiResponse<HomeItem>> getHomeData(@Path("API_KEY") String apiKey);

    //******* Get Business Category *********
    @GET("{API_KEY}/business-category")
    LiveData<ApiResponse<List<BusinessCategoryItem>>> getBusinessCategory(@Path("API_KEY") String apiKey);

    //******* Get Business Category Post *********
    @GET("{API_KEY}/business-frame")
    LiveData<ApiResponse<List<PostItem>>> getBusinessPost(@Path("API_KEY") String apiKey, @Query("id") String festId);

    //******* Get User Frame *********
    @GET("{API_KEY}/user-custom-frame")
    LiveData<ApiResponse<List<UserFrame>>> getUserFrame(@Path("API_KEY") String apiKey, @Query("userId") String userId);

    //******* Get Video ******
    @GET("{API_KEY}/get-video")
    LiveData<ApiResponse<List<PostItem>>> getVideosById(@Path("API_KEY") String apiKey, @Query("id") String id, @Query("type") String type);

    //******** Check Coupon *********
    @FormUrlEncoded
    @POST("{API_KEY}/coupon-code-validation")
    Call<CouponItem> checkCoupon(@Path("API_KEY") String apiKey, @Field("userId") String userId, @Field("code") String couponCode);


    //*********** Upload Image *******
    @Multipart
    @POST("{API_KEY}/profile-card-image-upload")
    Call<UploadItem> upLoadImage(@Path("API_KEY") String apiKey,
                                 @Part("profile_image") RequestBody name,
                                 @Part MultipartBody.Part file);

    @GET("{API_KEY}/profile-card")
    Call<ResponseBody> getPDFData(@Path("API_KEY") String apiKey);


    //******** Create Vcard *********
    @FormUrlEncoded
    @POST("{API_KEY}/profile-card")
    Call<UploadItem> createVcard(@Path("API_KEY") String apiKey,
                                 @Field("comapany_name") String businessName,
                                 @Field("name") String yourName,
                                 @Field("designation") String designation,
                                 @Field("phone") String mobile,
                                 @Field("whatsapp") String whatsapp,
                                 @Field("email") String email,
                                 @Field("website") String website,
                                 @Field("address") String location,
                                 @Field("facebook") String facebook,
                                 @Field("instagram") String insta,
                                 @Field("youtube") String youtube,
                                 @Field("twitter") String twitter,
                                 @Field("linkedin") String linkedin,
                                 @Field("about_us") String about,
                                 @Field("image") String imageUrl,
                                 @Field("template") String tempID);

    //******** Get Vcard *********
    @GET("{API_KEY}/business-card-list")
    LiveData<ApiResponse<List<ItemVcard>>> getVCards(@Path("API_KEY") String apiKey);

    //******** Get Stickers *********
    @GET("{API_KEY}/get-sticker")
    LiveData<ApiResponse<MainStrModel>> getStickers(@Path("API_KEY") String apiKey);

    //******** Search Stickers *********
    @FormUrlEncoded
    @POST("{API_KEY}/search-sticker")
    LiveData<ApiResponse<List<StickerItem>>> getStickersByKeyword(@Path("API_KEY") String key,
                                                                  @Field("keyword") String keyword);

    //******** Get Frames *********
    @FormUrlEncoded
    @POST("{API_KEY}/poster-json")
    LiveData<ApiResponse<List<DynamicFrameItem>>> getFrames(@Path("API_KEY") String key, @Field("ratio")String ratio);

    //******* Get Product ******
    @GET("{API_KEY}/product")
    LiveData<ApiResponse<ProductModel>> getProducts(@Path("API_KEY") String apiKey);

    //****** Send Enquiry ******
    @FormUrlEncoded
    @POST("{API_KEY}/inquiry")
    Call<ApiStatus> sendEnquiry(@Path("API_KEY") String apiKey,
                                @Field("name") String name,
                                @Field("email") String email,
                                @Field("message") String massage,
                                @Field("mobileNo") String number,
                                @Field("productId") String subjectId);


    //******* Get Refer Details ******
    @GET("{API_KEY}/referral-detail")
    LiveData<ApiResponse<ReferDetail>> getReferDetails(@Path("API_KEY") String apiKey, @Query("userId") String id);

    //******* Send Withdraw *******
    @FormUrlEncoded
    @POST("{API_KEY}/withdraw-request")
    Call<ApiStatus> sendWithdrawRequest(@Path("API_KEY") String apiKey,
                                        @Field("userId") String userId,
                                        @Field("upiId") String upiId,
                                        @Field("withdrawAmount") int withdrawAmount);


    //******* Create Order *******
    @FormUrlEncoded
    @POST("{API_KEY}/create-order-cashfree")
    Call<CashFreeOrder> createOrderCashFree(@Path("API_KEY") String apiKey,
                                            @Field("customer_id") String userId,
                                            @Field("order_amount") int order_amount,
                                            @Field("customer_name") String customer_name,
                                            @Field("customer_email") String customer_email,
                                            @Field("customer_phone") String customer_phone);

    //******** Get Business Sub Category ******
    @GET("{API_KEY}/business-sub-category")
    LiveData<ApiResponse<List<BusinessSubCategoryItem>>> getBusinessSubCategory(@Path("API_KEY") String apiKey,
                                                                   @Query("id") String catId);

    //******** Get Personal ******
    @GET("{API_KEY}/personal")
    LiveData<ApiResponse<List<PersonalItem>>> getPersonal(@Path("API_KEY") String apiKey);

    //****** Search Post Data ********
    @FormUrlEncoded
    @POST("{API_KEY}/search")
    Call<List<PostItem>> getPostBySearch(@Path("API_KEY")String key, @Field("term") String query);

    //******** Get Frame Category ******
    @GET("{API_KEY}/poster-category")
    LiveData<ApiResponse<List<FrameCategoryItem>>> getFrameCategories(@Path("API_KEY")String key);
}
