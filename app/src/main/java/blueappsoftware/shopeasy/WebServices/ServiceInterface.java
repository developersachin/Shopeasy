package blueappsoftware.shopeasy.WebServices;

import blueappsoftware.shopeasy.beanResponse.ForgotPassword;
import blueappsoftware.shopeasy.beanResponse.NewPassword;
import blueappsoftware.shopeasy.beanResponse.NewProdResopnce;
import blueappsoftware.shopeasy.beanResponse.NewUserRegistration;
import blueappsoftware.shopeasy.beanResponse.ProductDetail_Res;
import blueappsoftware.shopeasy.beanResponse.userSignin;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kamal_bunkar on 26-12-2017.
 */

public interface ServiceInterface {

    // method,, return type ,, secondary url
    // ecommerce-android-app-project/new_user_registration.php
    @Multipart
    @POST("shopeasy/new_user_registration.php")
    Call<NewUserRegistration> NewUserRegistrationCall(
            @Part("fullname") RequestBody fullname,
            @Part("phone") RequestBody phone,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );

    ///  user signin request
    @Multipart
    @POST("shopeasy/user_signin.php")
    Call<userSignin> UserSigninCall(
            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password
    );

    ///  user forgot password request
    @Multipart
    @POST("shopeasy/user_forgot_password.php")
    Call<ForgotPassword> UserForgotPassword(
            @Part("phone") RequestBody phone
    );

    ///  create new password request
    @Multipart
    @POST("shopeasy/new_password.php")
    Call<NewPassword> UserNewPassword(
            @Part("userid") RequestBody userid,
            @Part("password") RequestBody password
    );
    // get new products
    @Multipart
    @POST("shopeasy/getnewproduct.php")
    Call<NewProdResopnce> getNewPorduct(
            @Part("securecode") RequestBody securecode
    );

    // get best selling products
    @Multipart
    @POST("shopeasy/getbestsellingprod.php")
    Call<NewProdResopnce> getBestSelling(
            @Part("securecode") RequestBody securecode
    );
    // get trending products
    @Multipart
    @POST("shopeasy/gettrendingprod.php")
    Call<NewProdResopnce> getTrendingProd(
            @Part("securecode") RequestBody securecode
    );
    // get conditional products
    @Multipart
    @POST("shopeasy/getconditionalprod.php")
    Call<NewProdResopnce> getConditionalProd(
            @Part("securecode") RequestBody securecode
    );

    // get product details
    @Multipart
    @POST("shopeasy/getproductdetails.php")
    Call<ProductDetail_Res> getProductDetials(
            @Part("securecode") RequestBody securecode,
            @Part("prod_id") RequestBody prod_id
    );











}
