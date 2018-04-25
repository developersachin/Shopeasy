package blueappsoftware.shopeasy.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;


import java.util.ArrayList;
import java.util.List;

import blueappsoftware.shopeasy.R;
import blueappsoftware.shopeasy.Utility.AppUtilits;
import blueappsoftware.shopeasy.Utility.Constant;
import blueappsoftware.shopeasy.Utility.NetworkUtility;
import blueappsoftware.shopeasy.Utility.SharePreferenceUtils;
import blueappsoftware.shopeasy.WebServices.ServiceWrapper;
import blueappsoftware.shopeasy.beanResponse.NewProdResopnce;
import blueappsoftware.shopeasy.login.SigninActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;


public class HomeActivity extends AppCompatActivity {
    private String TAG ="HomeActivity";

    private RecyclerView recycler_bestSelling;
    private ArrayList<BestSelling_Model> bestSellingModelArrayList = new ArrayList<BestSelling_Model>();
    private BestSelling_Adapter bestSellingAdapter;

    private RecyclerView recycler_NewProd;
    private ArrayList<NewProd_Model> newPordModelList = new ArrayList<NewProd_Model>();
    private NewProd_Adapter newProdAdapter;

    private RecyclerView recycler_trending;
    private ArrayList<BestSelling_Model> trendingArrayList = new ArrayList<BestSelling_Model>();
    private BestSelling_Adapter trendingAdapter;


    private RecyclerView recycler_conditional;
    private ArrayList<BestSelling_Model> conditionalArraylist = new ArrayList<BestSelling_Model>();
    private BestSelling_Adapter conditionalAdapter;

    private BannerSlider bannerSlider;
    private List<Banner> remoteBanners=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* for (int i=0; i<4; i++) {
            bestSellingModel = new BestSelling_Model("prod  "+ i, "", "5 USD", "3 USD", "4");
            bestSellingModelArrayList.add(bestSellingModel);
        }*/

        bannerSlider = (BannerSlider) findViewById(R.id.banner_slider1);
        remoteBanners.add(new RemoteBanner("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg"
        ));
        remoteBanners.add(new RemoteBanner("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg"
        ));
        bannerSlider.setBanners(remoteBanners);
        /// for best selling
        recycler_bestSelling = (RecyclerView) findViewById(R.id.recycler_bestselling);
        LinearLayoutManager mLayoutManger = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false);
        recycler_bestSelling.setLayoutManager(mLayoutManger);
        recycler_bestSelling.setItemAnimator(new DefaultItemAnimator());

        bestSellingAdapter = new BestSelling_Adapter(this,  bestSellingModelArrayList, GetScreenWidth());
        recycler_bestSelling.setAdapter(bestSellingAdapter);

        // for trending product
        recycler_trending = (RecyclerView) findViewById(R.id.recycler_trending);
        LinearLayoutManager mLayoutManger1 = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false);
        recycler_trending.setLayoutManager(mLayoutManger1);
        recycler_trending.setItemAnimator(new DefaultItemAnimator());

        trendingAdapter = new BestSelling_Adapter(this,  trendingArrayList, GetScreenWidth());
        recycler_trending.setAdapter(trendingAdapter);

        // conditional prod
        recycler_conditional = (RecyclerView) findViewById(R.id.recycler_condit);
        LinearLayoutManager mLayoutManger2 = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false);
        recycler_conditional.setLayoutManager(mLayoutManger2);
        recycler_conditional.setItemAnimator(new DefaultItemAnimator());

        conditionalAdapter = new BestSelling_Adapter(this,  conditionalArraylist, GetScreenWidth());
        recycler_conditional.setAdapter(conditionalAdapter);
       // bestSellingAdapter.notifyDataSetChanged();

      /*  for (int i=0; i<4; i++) {
            newProdModel = new NewProd_Model("","new product  "+ i, "");
            newPordModelList.add(newProdModel);
        } */
        // for new product
        recycler_NewProd = (RecyclerView) findViewById(R.id.recycler_newProd);
        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false);
        recycler_NewProd.setLayoutManager(mLayoutManger3);
        recycler_NewProd.setItemAnimator(new DefaultItemAnimator());

        newProdAdapter = new NewProd_Adapter(this, newPordModelList, GetScreenWidth());
        recycler_NewProd.setAdapter(newProdAdapter);

        getNewProdRes();
        getBestSelling();
        getTrendingProd();
        getConditionalProd();
    }

    public void getNewProdRes(){

        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)){
            AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<NewProdResopnce> call = service.getNewProductRes("1234");
            call.enqueue(new Callback<NewProdResopnce>() {
                @Override
                public void onResponse(Call<NewProdResopnce> call, Response<NewProdResopnce> response) {
                    Log.e(TAG, " response is "+ response.body().getInformation().toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                                if (response.body().getInformation().size()>0){

                                    newPordModelList.clear();
                                    for (int i=0; i< response.body().getInformation().size(); i++){

                                        newPordModelList.add(new NewProd_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                                response.body().getInformation().get(i).getImgUrl()));

                                    }

                                  newProdAdapter.notifyDataSetChanged();
                                }

                        }else {
                            Log.e(TAG, "failed to get rnew prod "+ response.body().getMsg());
                           // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    }else {
                       // AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.failed_request));

                      //  getNewProdRes();
                    }
                }

                @Override
                public void onFailure(Call<NewProdResopnce> call, Throwable t) {
                    Log.e(TAG, "fail new prod "+ t.toString());

                }
            });

        }

    }

    public void getBestSelling(){
        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)){
            AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<NewProdResopnce> call = service.getBestselling("1234");
            call.enqueue(new Callback<NewProdResopnce>() {
                @Override
                public void onResponse(Call<NewProdResopnce> call, Response<NewProdResopnce> response) {
                    Log.e(TAG, " response is "+ response.body().getInformation().toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                                if (response.body().getInformation().size()>0) {
                                    bestSellingModelArrayList.clear();
                                    for (int i = 0; i < response.body().getInformation().size(); i++) {

                                        bestSellingModelArrayList.add(new BestSelling_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                                response.body().getInformation().get(i).getImgUrl(), response.body().getInformation().get(i).getMrp(),
                                                response.body().getInformation().get(i).getPrice(), response.body().getInformation().get(i).getRating()));


                                    }
                                    bestSellingAdapter.notifyDataSetChanged();
                                }
                        }else {
                            Log.e(TAG, "failed to get rnew prod "+ response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    }else {
                        Log.e(TAG, "failed to get rnew prod "+ response.body().getMsg());
                        // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<NewProdResopnce> call, Throwable t) {
                    Log.e(TAG, " fail best sell "+ t.toString());
                }
            });


        }

    }

    public void getTrendingProd(){
        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)){
            AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<NewProdResopnce> call = service.getTrendingPRod("1234");
            call.enqueue(new Callback<NewProdResopnce>() {
                @Override
                public void onResponse(Call<NewProdResopnce> call, Response<NewProdResopnce> response) {
                    Log.e(TAG, " response is "+ response.body().getInformation().toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            if (response.body().getInformation().size()>0) {
                                if (response.body().getInformation().size()>0) {
                                    trendingArrayList.clear();
                                    for (int i = 0; i < response.body().getInformation().size(); i++) {

                                        trendingArrayList.add(new BestSelling_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                                response.body().getInformation().get(i).getImgUrl(), response.body().getInformation().get(i).getMrp(),
                                                response.body().getInformation().get(i).getPrice(), response.body().getInformation().get(i).getRating()));


                                    }
                                    trendingAdapter.notifyDataSetChanged();
                                }
                            }
                        }else {
                            Log.e(TAG, "failed to get rnew prod "+ response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    }else {
                        Log.e(TAG, "failed to get rnew prod "+ response.body().getMsg());
                        // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<NewProdResopnce> call, Throwable t) {
                    Log.e(TAG, " fail best sell "+ t.toString());
                }
            });


        }

    }

    public void getConditionalProd(){
        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)){
            AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<NewProdResopnce> call = service.getConditionalPRod("1234");
            call.enqueue(new Callback<NewProdResopnce>() {
                @Override
                public void onResponse(Call<NewProdResopnce> call, Response<NewProdResopnce> response) {
                    Log.e(TAG, " response is "+ response.body().getInformation().toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            if (response.body().getInformation().size()>0) {
                                if (response.body().getInformation().size()>0) {
                                    conditionalArraylist.clear();
                                    for (int i = 0; i < response.body().getInformation().size(); i++) {

                                        conditionalArraylist.add(new BestSelling_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                                response.body().getInformation().get(i).getImgUrl(), response.body().getInformation().get(i).getMrp(),
                                                response.body().getInformation().get(i).getPrice(), response.body().getInformation().get(i).getRating()));


                                    }
                                    conditionalAdapter.notifyDataSetChanged();
                                }
                            }
                        }else {
                            Log.e(TAG, "failed to get rnew prod "+ response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    }else {
                        Log.e(TAG, "failed to get rnew prod "+ response.body().getMsg());
                        // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<NewProdResopnce> call, Throwable t) {
                    Log.e(TAG, " fail best sell "+ t.toString());
                }
            });


        }

    }




    public int GetScreenWidth(){
        int  width =100;

        DisplayMetrics  displayMetrics = new DisplayMetrics();
        WindowManager windowManager =  (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;

    }

}
