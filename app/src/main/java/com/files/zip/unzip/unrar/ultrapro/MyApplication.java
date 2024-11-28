package com.files.zip.unzip.unrar.ultrapro;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
//import androidx.lifecycle.ProcessLifecycleOwner;
//import com.ayoubfletcher.consentsdk.ConsentSDK;
//import com.google.android.gms.ads.AdError;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.FullScreenContentCallback;
//import com.google.android.gms.ads.LoadAdError;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.appopen.AppOpenAd;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
//import com.raktatech.winzip.activity.PrivacyPolicyActivity;
//import com.raktatech.winzip.activity.SplashActivity;
//import com.raktatech.winzip.ads.AdsVariable;


public class MyApplication extends Application  {
//public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
//    public static AppOpenAdManager appOpenAdManager = null;
    public static MyApplication instance = null;
    public static boolean needToShow = false;
    public Activity currentActivity;
    /* access modifiers changed from: private */
//    public AppOpenAd.AppOpenAdLoadCallback loadCallback;
    FirebaseAnalytics mFirebaseAnalytics;

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onCreate() {
        super.onCreate();
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        instance = this;
//        try {
//            MobileAds.initialize(this, new OnInitializationCompleteListener() {
//                public void onInitializationComplete(InitializationStatus initializationStatus) {
//                }
//            });
//        } catch (Exception unused) {
//        }
//        registerActivityLifecycleCallbacks(this);
//        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
//        appOpenAdManager = new AppOpenAdManager();
    }

//    /* access modifiers changed from: protected */
//    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    public void onMoveToForeground() {
//        Activity activity = this.currentActivity;
//        if (!(activity instanceof SplashActivity) && !(activity instanceof PrivacyPolicyActivity) && !needToShow) {
//            appOpenAdManager.showAdIfAvailable(activity);
//        }
//    }
//
//    public void onActivityStarted(Activity activity) {
//        if (!appOpenAdManager.isShowingAd) {
//            this.currentActivity = activity;
//        }
//    }
//
//    public void showAdIfAvailable(Activity activity, OnShowAdCompleteListener onShowAdCompleteListener) {
//        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
//    }

//    public class AppOpenAdManager {
//        private static final String LOG_TAG = "AppOpenAdManager";
//        /* access modifiers changed from: private */
//        public AppOpenAd appOpenAd = null;
//        public boolean isFaildLoadingAd = false;
//        /* access modifiers changed from: private */
//        public boolean isLoadingAd = false;
//        /* access modifiers changed from: private */
//        public boolean isShowingAd = false;
//        /* access modifiers changed from: private */
//        public long loadTime = 0;
//
//        public AppOpenAdManager() {
//        }
//
//        public void loadAd(Context context) {
//            if (!this.isLoadingAd && !isAdAvailable()) {
//                this.isFaildLoadingAd = false;
//                this.isLoadingAd = true;
//                AppOpenAd.AppOpenAdLoadCallback unused = MyApplication.this.loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
//                    public void onAdLoaded(AppOpenAd appOpenAd) {
//                        super.onAdLoaded(AppOpenAdManager.this.appOpenAd);
//                        AppOpenAd unused = AppOpenAdManager.this.appOpenAd = appOpenAd;
//                        boolean unused2 = AppOpenAdManager.this.isLoadingAd = false;
//                        long unused3 = AppOpenAdManager.this.loadTime = new Date().getTime();
//                        Log.d(AppOpenAdManager.LOG_TAG, "onAdLoaded.");
//                    }
//
//                    public void onAdFailedToLoad(LoadAdError loadAdError) {
//                        super.onAdFailedToLoad(loadAdError);
//                        boolean unused = AppOpenAdManager.this.isLoadingAd = false;
//                        AppOpenAdManager.this.isFaildLoadingAd = true;
//                        Log.d(AppOpenAdManager.LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
//                    }
//                };
//                AppOpenAd.load(context, AdsVariable.appOpen, getAdRequest(), 1, MyApplication.this.loadCallback);
//            }
//        }
//
//        private AdRequest getAdRequest() {
//            try {
//                return ConsentSDK.getAdRequest(MyApplication.this.currentActivity);
//            } catch (Exception unused) {
//                return new AdRequest.Builder().build();
//            }
//        }
//
//        private boolean wasLoadTimeLessThanNHoursAgo(long j) {
//            return new Date().getTime() - this.loadTime < j * 3600000;
//        }
//
//        public boolean isAdAvailable() {
//            return this.appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
//        }
//
//        public boolean isFaildLoadingAd() {
//            return this.isFaildLoadingAd;
//        }
//
//        /* access modifiers changed from: private */
//        public void showAdIfAvailable(Activity activity) {
//            showAdIfAvailable(activity, new OnShowAdCompleteListener() {
//                public void onShowAdComplete() {
//                }
//            });
//        }
//
//        /* access modifiers changed from: private */
//        public void showAdIfAvailable(final Activity activity, final OnShowAdCompleteListener onShowAdCompleteListener) {
//            if (this.isShowingAd) {
//                Log.d(LOG_TAG, "The app open ad is already showing.");
//            } else if (!isAdAvailable()) {
//                Log.d(LOG_TAG, "The app open ad is not ready yet.");
//                onShowAdCompleteListener.onShowAdComplete();
//                loadAd(activity);
//            } else {
//                Log.d(LOG_TAG, "Will show ad.");
//                this.appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                    public void onAdDismissedFullScreenContent() {
//                        AppOpenAd unused = AppOpenAdManager.this.appOpenAd = null;
//                        boolean unused2 = AppOpenAdManager.this.isShowingAd = false;
//                        Log.d(AppOpenAdManager.LOG_TAG, "onAdDismissedFullScreenContent.");
//                        onShowAdCompleteListener.onShowAdComplete();
//                        AppOpenAdManager.this.loadAd(activity);
//                    }
//
//                    public void onAdFailedToShowFullScreenContent(AdError adError) {
//                        AppOpenAd unused = AppOpenAdManager.this.appOpenAd = null;
//                        boolean unused2 = AppOpenAdManager.this.isShowingAd = false;
//                        Log.d(AppOpenAdManager.LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
//                        onShowAdCompleteListener.onShowAdComplete();
//                        AppOpenAdManager.this.loadAd(activity);
//                    }
//
//                    public void onAdShowedFullScreenContent() {
//                        boolean unused = AppOpenAdManager.this.isShowingAd = true;
//                        Log.d(AppOpenAdManager.LOG_TAG, "onAdShowedFullScreenContent.");
//                    }
//                });
//                this.appOpenAd.show(MyApplication.this.currentActivity);
//            }
//        }
//    }
}
