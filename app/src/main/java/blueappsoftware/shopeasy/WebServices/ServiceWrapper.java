package blueappsoftware.shopeasy.WebServices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import blueappsoftware.shopeasy.BuildConfig;
import blueappsoftware.shopeasy.Utility.Constant;
import blueappsoftware.shopeasy.beanResponse.ForgotPassword;
import blueappsoftware.shopeasy.beanResponse.NewPassword;
import blueappsoftware.shopeasy.beanResponse.NewProdResopnce;
import blueappsoftware.shopeasy.beanResponse.ProductDetail_Res;
import blueappsoftware.shopeasy.beanResponse.userSignin;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import blueappsoftware.shopeasy.beanResponse.NewUserRegistration;

/**
 * Created by kamal_bunkar on 26-12-2017.
 */

public class ServiceWrapper  {

    private ServiceInterface mServiceInterface;

    public ServiceWrapper(Interceptor mInterceptorheader) {
        mServiceInterface = getRetrofit(mInterceptorheader).create(ServiceInterface.class);
    }

    public Retrofit getRetrofit(Interceptor mInterceptorheader) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient mOkHttpClient = null;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(Constant.API_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(Constant.API_READ_TIMEOUT, TimeUnit.SECONDS);

//        if (BuildConfig.DEBUG)
//            builder.addInterceptor(loggingInterceptor);

        if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }


        mOkHttpClient = builder.build();
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mOkHttpClient)
                .build();
        return retrofit;
    }

    public Call<NewUserRegistration> newUserRegistrationCall( String fullname, String phone, String username, String password){
       return mServiceInterface.NewUserRegistrationCall( convertPlainString(fullname), convertPlainString(phone), convertPlainString( username),
               convertPlainString(password));
    }
    ///  user signin
    public Call<userSignin> UserSigninCall( String phone,  String password){
        return mServiceInterface.UserSigninCall(convertPlainString(phone),  convertPlainString(password));
    }

    ///  user signin
    public Call<ForgotPassword> UserForgotPassword(String phone){
        return mServiceInterface.UserForgotPassword(convertPlainString(phone));
    }
    ///  user new password
    public Call<NewPassword> UserNewPassword(String userid, String password){
        return mServiceInterface.UserNewPassword(convertPlainString(userid), convertPlainString(password));
    }

    ///  new product details
    public Call<NewProdResopnce> getNewProductRes(String securcode){
        return mServiceInterface.getNewPorduct(convertPlainString(securcode));
    }

    ///  best selling product details
    public Call<NewProdResopnce> getBestselling(String securcode){
        return mServiceInterface.getBestSelling(convertPlainString(securcode));
    }
    ///  get trending  product details
    public Call<NewProdResopnce> getTrendingPRod(String securcode){
        return mServiceInterface.getTrendingProd(convertPlainString(securcode));
    }
    ///  get conditional  product details
    public Call<NewProdResopnce> getConditionalPRod(String securcode){
        return mServiceInterface.getConditionalProd(convertPlainString(securcode));
    }

    // get product detials
    ///  get conditional  product details
    public Call<ProductDetail_Res> getProductDetails(String securcode, String prod_id){
        return mServiceInterface.getProductDetials(convertPlainString(securcode), convertPlainString(prod_id));
    }


    // convert aa param into plain text
    public RequestBody convertPlainString(String data){
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return  plainString;
    }
}


