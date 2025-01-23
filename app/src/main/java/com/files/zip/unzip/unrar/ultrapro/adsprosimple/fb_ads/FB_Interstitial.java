package com.files.zip.unzip.unrar.ultrapro.adsprosimple.fb_ads;

import static com.files.zip.unzip.unrar.ultrapro.adsprosimple.GlobalVar.appData;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.WindowManager;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.files.zip.unzip.unrar.ultrapro.MyApp;
import com.files.zip.unzip.unrar.ultrapro.R;
import com.files.zip.unzip.unrar.ultrapro.adsprosimple.OnActivityResultLauncher1;
import com.files.zip.unzip.unrar.ultrapro.adsprosimple.RemoteAppDataModel;

public class FB_Interstitial {

    public static Dialog loadAdsDialog = null;

    public static long interstitial_timer;

    static com.facebook.ads.InterstitialAd FBInterstitialAd;

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

        loadFBInterstitialAdResultLaunch(activity, countClicks, resultLauncher);
    }

    public static void loadFBInterstitialAdResultLaunch(Activity activity, Boolean countClicks, OnActivityResultLauncher1.OnActivityResultLauncher2 resultLauncher) {
        if (appData.getshowInterstitial() == 1) {
            String adUnitId = appData.getfbInterstitialid();

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

//            if (fbfullarraycount > strFBFullscreen.length - 1)
//                fbfullarraycount = 0;

//            FBInterstitialAd = new com.facebook.ads.InterstitialAd(activity, strFBFullscreen[fbfullarraycount]);
            FBInterstitialAd = new com.facebook.ads.InterstitialAd(activity, adUnitId);

            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {

                @Override
                public void onInterstitialDisplayed(Ad ad) {
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
//                    fbfullarraycount++;

                    new CountDownTimer(appData.getintertime() * 1000L, 1000) {
                        public void onTick(long millisUntilFinished) {

                            interstitial_timer = millisUntilFinished / 1000;
//                            Log.e("PACK1_ADMOB_TAG", "seconds remaining: " + interstitial_timer);

                        }

                        public void onFinish() {
                            interstitial_timer = 0;
//                            Log.e("PACK1_ADMOB_TAG", "done!");
                        }
                    }.start();

                    doNext(activity, resultLauncher);
                    RemoteAppDataModel.isShowingFullScreenAd = false;
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Handle the error
                    FBInterstitialAd = null;
                    if (loadAdsDialog != null && loadAdsDialog.isShowing()) {
                        loadAdsDialog.dismiss();
                    }
                    doNext(activity, resultLauncher);

                    new CountDownTimer(appData.getintertime() * 1000L, 1000) {
                        public void onTick(long millisUntilFinished) {

                            interstitial_timer = millisUntilFinished / 1000;
//                            Log.e("PACK1_ADMOB_TAG", "seconds remaining: " + interstitial_timer);

                        }

                        public void onFinish() {
                            interstitial_timer = 0;
//                            Log.e("PACK1_ADMOB_TAG", "done!");
                        }
                    }.start();
                }

                @Override
                public void onAdLoaded(Ad ad) {

                    if (loadAdsDialog != null && loadAdsDialog.isShowing()) {
                        loadAdsDialog.dismiss();
                    }

                    if (RemoteAppDataModel.adspersession != appData.getAds_per_session()) {
                        if (FBInterstitialAd != null) {
                            FBInterstitialAd.show();
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
                }

                @Override
                public void onAdClicked(Ad ad) {
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }
            };

//            AdSettings.addTestDevice("HASHED ID");
            FBInterstitialAd.loadAd(FBInterstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());

        } else {
            doNext(activity, resultLauncher);
        }
    }

    public static void doNext(Activity activity, OnActivityResultLauncher1.OnActivityResultLauncher2 resultLauncher) {
        resultLauncher.onLauncher();
    }
}