package com.raktatech.winzip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.raktatech.winzip.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivitySplashBinding inflate = ActivitySplashBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
//        Resizer.getheightandwidth(this);
//        Resizer.setSize(this.binding.bgSplash, 955, 955, true);
//        Resizer.setSize(this.binding.zipLogo, 366, 372, true);
//        Resizer.setSize(this.binding.switchGif, ServiceStarter.ERROR_UNKNOWN, ServiceStarter.ERROR_UNKNOWN, true);
//        new AdsSplashUtils(this, new AdsSplashUtils.SplashInterface() {
//            public void nextActivity() {
//                SplashActivity.this.startActivity(new Intent(SplashActivity.this, HomeActivity.class));
//                SplashActivity.this.finish();
//            }
//
//            public void callNativePreload() {
//                new AdsPreloadUtils(SplashActivity.this).callPreloadSmallNative(AdsVariable.full_Home_Activity_Small_Native);
//            }
//        });

        new Handler().postDelayed(() -> {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            SplashActivity.this.finish();
        }, 2000);
    }
}
