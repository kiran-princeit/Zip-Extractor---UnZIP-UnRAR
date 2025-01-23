package com.files.zip.unzip.unrar.ultrapro.adsprosimple.am_ads;

import static com.files.zip.unzip.unrar.ultrapro.adsprosimple.GlobalVar.appData;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.files.zip.unzip.unrar.ultrapro.MyApp;
import com.files.zip.unzip.unrar.ultrapro.adsprosimple.OnActivityResultLauncher1;

public class AM_Appopen {

    public static void loadAppOpenAd(Activity act, OnActivityResultLauncher1.OnActivityResultLauncher2 resultLauncher) {
        if (appData.getshowOpenad() == 1) {
            if (!MyApp.isNetworkConnected(act)) {
                return;
            }
            String adUnitId = appData.getamOpenadid();
            if (adUnitId.isEmpty()) {
                return;
            }

//            if (BuildConfig.DEBUG) {
                // Test Ad ID for debug builds
                adUnitId = "ca-app-pub-3940256099942544/3419835294";
//            } else {
//                // Ad ID from Firebase Remote Config for release builds
//                adUnitId = appData.getamOpenadid();
//            }

            AppOpenAd.load(act, adUnitId, new AdRequest.Builder().build(), AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd ad) {
                    ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            resultLauncher.onLauncher();

                            Log.e("XXX", "onAdDismissedFullScreenContent");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError error) {
                            resultLauncher.onLauncher();
                            Log.e("XXX", "onAdFailedToShowFullScreenContent");

                        }
                    });
                    ad.show(act);

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    resultLauncher.onLauncher();
                    Log.e("XXX", "onAdFailedToLoad");

                }
            });

        } else {
            resultLauncher.onLauncher();
        }
    }

}