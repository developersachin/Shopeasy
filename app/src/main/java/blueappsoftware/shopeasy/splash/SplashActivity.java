package blueappsoftware.shopeasy.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import blueappsoftware.shopeasy.R;
import blueappsoftware.shopeasy.Utility.SharePreferenceUtils;
import blueappsoftware.shopeasy.home.HomeActivity;
import blueappsoftware.shopeasy.login.SigninActivity;

/**
 * Created by kamal_bunkar on 19-12-2017.
 */

public class SplashActivity extends AppCompatActivity {
    private String TAG ="splashAcctivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        Log.e(TAG, " splash start showing");
    }

    public void init(){

        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                /// if user registered user
                // then show home screen
                // else  show login screen
                // key  register_user
                Log.e(TAG, "  counter running ");
                if (SharePreferenceUtils.getInstance().getString("register_user").equalsIgnoreCase("")){
                    // not registted user  so show login screen
                    Intent intent = new Intent(SplashActivity.this, SigninActivity.class);
                    startActivity(intent);
                }else {
                    // home sscreen
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                finish();

            }
        }, 3000 );

    }
}
