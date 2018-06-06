package revenue_express.citythai.model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by NEO on 26/3/2561.
 */

public interface APIService {
    //SplashScreenActivity
    @FormUrlEncoded
    @POST("theme/index")
    Call<ResponseBody>getTheme(@Field("access_token") String access_token, @Field("shop_id") String shop);

    //HomeFragment
    @FormUrlEncoded
    @POST("business_shop/data")
    Call<ResponseBody>getShopData(@Field("access_token") String access_token, @Field("shop") String shop);

    //OrderFragment
    @FormUrlEncoded
    @POST("shop_menu/list_category")
    Call<ResponseBody>getCategoery(@Field("access_token") String access_token, @Field("shop") String shop);

    //ReviewFragment
    @FormUrlEncoded
    @POST("reviews/finder")
    Call<ResponseBody>getReview(@Field("access_token") String access_token, @Field("shop") String code, @Field("sort_by") String sort_by);

    //BarcodePointActivity
    @FormUrlEncoded
    @POST("reward/member_point")
    Call<ResponseBody>getPion(@Field("access_token") String access_token, @Field("access_user_key") String access_user_key, @Field("shop") String shop);

    //DishDetailsActivity
    @FormUrlEncoded
    @POST("shop_menu/get_menu_all_size")
    Call<ResponseBody>getMenu(@Field("access_token") String access_token, @Field("item_id") String item_id, @Field("shop") String shop);

    @FormUrlEncoded
    @POST("shop_menu/get_menu_by_size")
    Call<ResponseBody>getMenuBySize(@Field("access_token") String access_token, @Field("item_id") String item_id, @Field("size_id") String size_id, @Field("shop") String shop);

    //ForgetPasswordActivity
    @FormUrlEncoded
    @POST("accounts/pwd_request")
    Call<ResponseBody>getForgetPassword(@Field("access_token") String access_token, @Field("m_user") String m_user);

    //MenuOrderActivity
    @FormUrlEncoded
    @POST("shop_menu/list_menu")
    Call<ResponseBody>getMenuData(@Field("access_token") String access_token, @Field("category_id") String category_id, @Field("shop") String shop);

    //PaymentActivity
    @FormUrlEncoded
    @POST("payment/confirm")
    Call<ResponseBody>getPayment(@Field("access_token") String access_token, @Field("access_user_key") String access_user_key, @Field("card_number") String card_number,@Field("card_holder") String card_holder, @Field("card_month") String card_month, @Field("card_year") String card_year,@Field("card_cvv") String card_cvv, @Field("order_id") String order_id, @Field("order_amount") String order_amount);

    //PointHistoryPageActivity
    @FormUrlEncoded
    @POST("reward/member_point")
    Call<ResponseBody>getPointMember(@Field("access_token") String access_token, @Field("access_user_key") String access_user_key, @Field("shop") String shop);

    @FormUrlEncoded
    @POST("reward/history_member")
    Call<ResponseBody>getHistoryPointMember(@Field("access_token") String access_token, @Field("access_user_key") String access_user_key, @Field("shop") String shop);

    //RegisterActivity
    @FormUrlEncoded
    @POST("accounts/signup")
    Call<ResponseBody>getRegister(@Field("access_token") String access_token, @Field("m_firstname") String m_firstname, @Field("m_lastname") String m_lastname,@Field("m_email") String m_email, @Field("m_pass1") String m_pass1, @Field("m_pass2") String m_pass2);

    //ReviewDetailActivity
    @FormUrlEncoded
    @POST("reviews/show")
    Call<ResponseBody>getReviewShow(@Field("access_token") String access_token ,@Field("review") String review);

    //SignInActivity
    @FormUrlEncoded
    @POST("accounts/login")
    Call<ResponseBody>getloginUserGeneral(@Field("access_token") String access_token ,@Field("m_user") String m_user,@Field("m_pass") String m_pass,@Field("shop") String shop);

    @FormUrlEncoded
    @POST("fb_connect/mobile_login")
    Call<ResponseBody>getloginUserFB(@Field("shop") String shop,@Field("access_token") String access_token ,@Field("m_oauth_token") String m_oauth_token);

    @FormUrlEncoded
    @POST("gg_connect/mobile_login")
    Call<ResponseBody>getloginUserGG(@Field("shop") String shop,@Field("access_token") String access_token ,@Field("m_oauth_token") String m_oauth_token);

    //UserProfileActivity
    @FormUrlEncoded
    @POST("accounts_update/info")
    Call<ResponseBody>postUserSave(@Field("access_token") String access_token ,@Field("access_user_key") String access_user_key,@Field("m_display") String m_display,@Field("m_firstname") String m_firstname,@Field("m_lastname") String m_lastname,@Field("m_gender") String m_gender);

    @FormUrlEncoded
    @POST("accounts_update/pwd")
    Call<ResponseBody>postUserSavePassword(@Field("access_token") String access_token ,@Field("access_user_key") String access_user_key,@Field("old_pass") String old_pass ,@Field("new_pass") String new_pass,@Field("confirm_pass") String confirm_pass);


    // SumOrderActivity,callSyncUploadImage in UserProfileActivity,WriteReviewsActivity ***** Not us Retrofit

}
