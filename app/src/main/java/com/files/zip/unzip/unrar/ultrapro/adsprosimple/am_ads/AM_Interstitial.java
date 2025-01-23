package com.files.zip.unzip.unrar.ultrapro.adsprosimple.am_ads;

import static com.files.zip.unzip.unrar.ultrapro.adsprosimple.GlobalVar.appData;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.files.zip.unzip.unrar.ultrapro.MyApp;
import com.files.zip.unzip.unrar.ultrapro.R;
import com.files.zip.unzip.unrar.ultrapro.adsprosimple.OnActivityResultLauncher1;
import com.files.zip.unzip.unrar.ultrapro.adsprosimple.RemoteAppDataModel;

public class AM_Interstitial {

    public static Dialog loadAdsDialog = null;
    public static InterstitialAd mInterstitialAd = null;

    public static long interstitial_timer;

    public static void loadInterstitialAdResultLaunch(Activity activity, Boolean countClicks, OnActivityResultLauncher1.OnActivityResultLauncher2 resultLauncher) {
        if (!MyApp.isNetworkConnected(activity)) {
            doNext(activity, resultLauncher);
            return;
        }
        if (appData.getshowInterstitial() != 1) {
            doNext(activity, resultLauncher);
            return;
        }

        if (interstitial_timer > 0) {
            doNext(activity, resultLauncher);
            return;
        }

        if (RemoteAppDataModel.adspersession == appData.getAds_per_session()) {
            doNext(activity, resultLauncher);
            return;
        }

        if (countClicks) {
            int count = appData.getinterclickcount();
            appData.setinterclickcount(appData.getinterclickcount() + 1);
            if (count % appData.getinterclickcount() != 0) {
                doNext(activity, resultLauncher);
                return;
            }
        }
        loadAdmobInterstitialAdResultLaunch(activity, countClicks, resultLauncher);

    }

    public static void loadAdmobInterstitialAdResultLaunch(Activity activity, Boolean countClicks, OnActivityResultLauncher1.OnActivityResultLauncher2 resultLauncher) {
        if (appData.getshowInterstitial() == 1) {
            String adUnitId = appData.getamInterstitialid();

            Log.e("PACK1_ADMOB_TAG", "Admob InterstitialAd ==>" + appData.getamInterstitialid());
            if (TextUtils.isEmpty(adUnitId)) {
                return;
            }
            loadAdsDialog = new Dialog(activity);
            loadAdsDialog.setContentView(R.layout.layout_loading);
            loadAdsDialog.setCanceledOnTouchOutside(false);
            loadAdsDialog.setCancelable(false);
            loadAdsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            loadAdsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadAdsDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            loadAdsDialog.show();

            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(activity, adUnitId, adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                    mInterstitialAd = interstitialAd;

                    Log.e("PACK1_ADMOB_TAG", "InterstitialAd ==> onAdLoaded");
                    if (loadAdsDialog != null && loadAdsDialog.isShowing()) {
                        loadAdsDialog.dismiss();
                    }

                    displayAdMobInterstitialAdResultLauncher(activity, countClicks, resultLauncher);

                    super.onAdLoaded(interstitialAd);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.e("PACK1_ADMOB_TAG", "InterstitialAd ==> " + loadAdError);
                    mInterstitialAd = null;
                    if (loadAdsDialog != null && loadAdsDialog.isShowing()) {
                        loadAdsDialog.dismiss();
                    }
                    doNext(activity, resultLauncher);
                    super.onAdFailedToLoad(loadAdError);
                }
            });
        } else {
            doNext(activity, resultLauncher);
        }
    }

    // Display InterstitialAds
    public static void displayAdMobInterstitialAdResultLauncher(Activity activity, Boolean countClicks, OnActivityResultLauncher1.OnActivityResultLauncher2 resultLauncher) {
        if (RemoteAppDataModel.adspersession != appData.getAds_per_session()) {
            if (mInterstitialAd != null) {
//                Log.e("PACK1_ADMOB_TAG", "Admob InterstitialAd ==> " + "Showed");
                mInterstitialAd.show(activity);
                RemoteAppDataModel.isShowingFullScreenAd = true;
                RemoteAppDataModel.adspersession++;
            } else {
                loadInterstitialAdResultLaunch(activity, countClicks, resultLauncher);
                RemoteAppDataModel.isShowingFullScreenAd = false;
                return;
            }
        } else {
            doNext(activity, resultLauncher);
        }

        if (mInterstitialAd != null) {

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    // Called when fullscreen content is dismissed.
                    Log.e("PACK1_ADMOB_TAG", "Admob InterstitialAd ==> The ad was dismissed.");

                    new CountDownTimer(appData.getintertime() * 1000L, 1000) {
                        public void onTick(long millisUntilFinished) {

                            interstitial_timer = millisUntilFinished / 1000;
                            Log.e("PACK1_ADMOB_TAG", "seconds remaining: " + interstitial_timer);

                        }

                        public void onFinish() {
                            interstitial_timer = 0;
                            Log.e("PACK1_ADMOB_TAG", "done!");
                        }
                    }.start();

                    doNext(activity, resultLauncher);
                    RemoteAppDataModel.isShowingFullScreenAd = false;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Log.e("PACK1_ADMOB_TAG", "Admob InterstitialAd ==> The ad failed to show.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    mInterstitialAd = null;
                    Log.e("PACK1_ADMOB_TAG", "Admob InterstitialAd ==> The ad was shown.");
                }
            });
        }
    }

    public static void doNext(Activity activity, OnActivityResultLauncher1.OnActivityResultLauncher2 resultLauncher) {
        resultLauncher.onLauncher();
    }
}