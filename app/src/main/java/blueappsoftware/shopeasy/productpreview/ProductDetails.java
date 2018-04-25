package blueappsoftware.shopeasy.productpreview;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import blueappsoftware.shopeasy.MainActivity;
import blueappsoftware.shopeasy.R;
import blueappsoftware.shopeasy.Utility.AppUtilits;
import blueappsoftware.shopeasy.Utility.Constant;
import blueappsoftware.shopeasy.Utility.NetworkUtility;
import blueappsoftware.shopeasy.Utility.SharePreferenceUtils;
import blueappsoftware.shopeasy.WebServices.ServiceWrapper;
import blueappsoftware.shopeasy.beanResponse.ProductDetail_Res;
import blueappsoftware.shopeasy.cart.CartDetails;
import blueappsoftware.shopeasy.home.BestSelling_Adapter;
import blueappsoftware.shopeasy.home.BestSelling_Model;
import blueappsoftware.shopeasy.home.HomeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kamal_bunkar on 17-01-2018.
 */

public class ProductDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG ="productDetails";
    private String prod_id="";
    private TextView prod_name, prod_price, prod_oldprice, prod_rating_count, prod_stock, prod_qty;
    private AppCompatRatingBar prod_rating;
    private ImageView add_to_cart,add_to_wishlist;
    // related product
    private RecyclerView recycler_relatedProd;
    private ArrayList<BestSelling_Model> relatedProdModelArrayList = new ArrayList<BestSelling_Model>();
    private BestSelling_Adapter relatedProdAdapter;
    // overview and review tab layout
    private TabLayout tablayout;
    private FrameLayout frag_container;
    public String prod_overview ="";
    public String prod_fulldetails ="";
    public String prod_review ="";

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Menu mainmenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_preview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        final Intent intent = getIntent();
        prod_id =  intent.getExtras().getString("prod_id");

        prod_name = (TextView) findViewById(R.id.prod_name);
        prod_price =(TextView) findViewById(R.id.prod_price);
        prod_oldprice = (TextView) findViewById(R.id.price_old);
        prod_rating_count = (TextView) findViewById(R.id.rating_count);
        prod_stock = (TextView) findViewById(R.id.stock_avail);
        prod_qty = (TextView) findViewById(R.id.prod_qty_value);
        prod_rating = (AppCompatRatingBar) findViewById(R.id.prod_rating);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        frag_container = (FrameLayout) findViewById(R.id.frag_container);
        add_to_cart = (ImageView) findViewById(R.id.add_to_cart);
        add_to_wishlist = (ImageView) findViewById(R.id.add_to_wishlist);


        tablayout.addTab( tablayout.newTab().setText(getString(R.string.overview)));
        tablayout.addTab( tablayout.newTab().setText(getString(R.string.details)));
        tablayout.addTab( tablayout.newTab().setText(getString(R.string.review)));

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // "blueappsoftware.shopeasy.productpreview.tabfragment"
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment PrevFrag = fragmentManager.findFragmentByTag("blueappsoftware.shopeasy.productpreview.tabfragment");
                if (PrevFrag !=null){
                    fragmentTransaction.remove(PrevFrag);
                }

                if (tab.getPosition()==0){
                    Overview overview = new Overview();
                    fragmentTransaction.add( R.id.frag_container, overview, "blueappsoftware.shopeasy.productpreview.tabfragment");
                    fragmentTransaction.commit();

                }else if (tab.getPosition()==1){
                    Details details = new Details();
                    fragmentTransaction.add( R.id.frag_container, details, "blueappsoftware.shopeasy.productpreview.tabfragment");
                    fragmentTransaction.commit();

                }else if (tab.getPosition()==2){
                    Review review = new Review();
                    fragmentTransaction.add( R.id.frag_container, review, "blueappsoftware.shopeasy.productpreview.tabfragment");
                    fragmentTransaction.commit();
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        getProductDetails();




        recycler_relatedProd = (RecyclerView) findViewById(R.id.recycler_relatedprod);
        LinearLayoutManager mLayoutManger = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false);
        recycler_relatedProd.setLayoutManager(mLayoutManger);
        recycler_relatedProd.setItemAnimator(new DefaultItemAnimator());

        relatedProdAdapter = new BestSelling_Adapter(this,  relatedProdModelArrayList, GetScreenWidth() );
        recycler_relatedProd.setAdapter(relatedProdAdapter);


        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // store produ id with user id on server  and get quate id as response and store it on share preferernce
                 // addtocartapi();
                SharePreferenceUtils.getInstance().saveString(Constant.QUOTE_ID, "");

                SharePreferenceUtils.getInstance().saveInt( Constant.CART_ITEM_COUNT,   SharePreferenceUtils.getInstance().getInteger(Constant.CART_ITEM_COUNT) +1);
                AppUtilits.UpdateCartCount(mainmenu);
            }
        });

        add_to_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public int GetScreenWidth(){
        int  width =100;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager =  (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;

    }


    public void getProductDetails(){

        if (!NetworkUtility.isNetworkConnected(ProductDetails.this)){
            AppUtilits.displayMessage(ProductDetails.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<ProductDetail_Res> call = service.getProductDetails("1234", prod_id );
            call.enqueue(new Callback<ProductDetail_Res>() {
                @Override
                public void onResponse(Call<ProductDetail_Res> call, Response<ProductDetail_Res> response) {
                    Log.e(TAG, "reposne is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().getDetails().getName()!=null){
                                prod_name.setText(response.body().getInformation().getDetails().getName());

                                if (response.body().getInformation().getDetails().getStock() >0){
                                    prod_stock.setText( getString(R.string.instock));
                                }else {
                                    prod_stock.setText( getString(R.string.outofstock));
                                }


                                prod_oldprice.setText( response.body().getInformation().getDetails().getMrp());
                                prod_price.setText(response.body().getInformation().getDetails().getPrice());
                                prod_qty.setText("1");
                                // prod image
                               // Log.e(TAG, "rating count "+)
                                prod_rating_count.setText(response.body().getInformation().getDetails().getRatingCount());
                                prod_rating.setRating(response.body().getInformation().getDetails().getRating());

                                if (response.body().getInformation().getRelatedprod().size()>0){
                                    relatedProdModelArrayList.clear();
                                    for (int i = 0; i < response.body().getInformation().getRelatedprod().size(); i++) {

                                        relatedProdModelArrayList.add(new BestSelling_Model(response.body().getInformation().getRelatedprod().get(i).getId(),
                                                response.body().getInformation().getRelatedprod().get(i).getName(),
                                                response.body().getInformation().getRelatedprod().get(i).getImgUrl(),
                                                response.body().getInformation().getRelatedprod().get(i).getMrp(),
                                                response.body().getInformation().getRelatedprod().get(i).getPrice(),
                                               String.valueOf( response.body().getInformation().getRelatedprod().get(i).getRating())) );


                                    }
                                    relatedProdAdapter.notifyDataSetChanged();


                                }
                                 prod_overview =response.body().getInformation().getDetails().getDesc();
                                 prod_fulldetails = response.body().getInformation().getDetails().getFulldetails();
                                 prod_review = response.body().getInformation().getReview();


                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                Overview overview = new Overview();
                                fragmentTransaction.add( R.id.frag_container, overview, "blueappsoftware.shopeasy.productpreview.tabfragment");
                                fragmentTransaction.commit();



                            }


                        } else {
                            Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    } else {
                        Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                        // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<ProductDetail_Res> call, Throwable t) {
                    Log.e(TAG, " fail best sell "+ t.toString());
                }
            });

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.myaccount_toolbar_menu, menu);
        mainmenu = menu;
        if (mainmenu!=null)
            AppUtilits.UpdateCartCount(mainmenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Log.e(TAG, "  option click "+ item.getTitle() );
        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {

            // Associate searchable configuration with the SearchView
            SearchManager searchManager =  (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =  (SearchView) mainmenu.findItem(R.id.search).getActionView();
            final EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

            searchEditText.setHint(getString(R.string.search_name));

            searchEditText.setHintTextColor(getResources().getColor(R.color.white));
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));

            searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                  //  Log.e("onClick: ", "-- " + searchEditText.getText().toString().trim());
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        //run query to the server
                        Log.e("onClick: ", "-- " + searchEditText.getText().toString().trim());
                        if ( null!=searchEditText.getText().toString().trim() && !searchEditText.getText().toString().trim().equals("")){

                        }
                          //  AppUtils.GetSearchResult( HomeActivity.this, TAG, searchEditText.getText().toString());
                    }
                    return false;
                }
            });
            return true;
        }else if (id==R.id.cart){
            Intent intent = new Intent(this, CartDetails.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id =  item.getItemId();

        if (id == R.id.nav_home){


        }else if (id == R.id.nav_new_prod){


        }else if (id == R.id.nav_myaccount){


        }else if (id == R.id.nav_wishlist){


        }else if (id == R.id.nav_logout){


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainmenu!=null)
            AppUtilits.UpdateCartCount(mainmenu);
    }
}
