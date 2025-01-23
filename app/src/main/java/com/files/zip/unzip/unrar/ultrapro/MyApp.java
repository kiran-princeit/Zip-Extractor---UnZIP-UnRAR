package com.files.zip.unzip.unrar.ultrapro;


import static com.files.zip.unzip.unrar.ultrapro.adsprosimple.GlobalVar.appData;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.files.zip.unzip.unrar.ultrapro.adsprosimple.MobileAds;
import com.files.zip.unzip.unrar.ultrapro.adsprosimple.OnActivityResultLauncher1;
import com.files.zip.unzip.unrar.ultrapro.adsprosimple.RemoteAppDataModel;
import com.files.zip.unzip.unrar.ultrapro.utils.GoogleMobileAdsConsentManager;
import com.files.zip.unzip.unrar.ultrapro.utils.LanguagePreference;
import com.files.zip.unzip.unrar.ultrapro.utils.LocaleHelper;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;


import java.util.Date;

public class MyApp extends Application implements Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {
    FirebaseAnalytics mFirebaseAnalytics;
    private static MyApp mInstance;
    public static final String TEST_DEVICE_HASHED_ID = "ABCDEF012345";
    private static AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;
    String adUnitId;

    public void onCreate() {
        super.onCreate();

        String languageCode = LanguagePreference.getLanguage(this);
        LocaleHelper.setLocale(this, languageCode);

        mInstance = this;
        this.registerActivityLifecycleCallbacks(this);

        com.google.android.gms.ads.MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(
                            @NonNull InitializationStatus initializationStatus) {
                    }
                });
        appData = new RemoteAppDataModel();
           MobileAds.init(this, new MobileAds.firebaseonloadcomplete() {
            @Override
            public void onloadcomplete() {

            }
        });

        if (BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false);
        } else {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        }

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        appOpenAdManager = new AppOpenAdManager();
        FirebaseApp.initializeApp(mInstance);
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        MultiDex.install(this);
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
        if (appOpenAdManager != null && currentActivity != null) {
            appOpenAdManager.showAdIfAvailable(currentActivity);
        } else {
            // Handle the null scenario, possibly by logging a warning or initializing the objects
            Log.w("MyApp", "appOpenAdManager or currentActivity is null in onStart");
        }

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    public void loadAd(@NonNull Activity activity) {
        appOpenAdManager.loadAd(activity);
    }

    public void showAdIfAvailable(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
    }

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }


    public static void amappopen(Activity act, OnActivityResultLauncher1.OnActivityResultLauncher2 resultLauncher) {
        appOpenAdManager.loadAd(act);
    }


    private class AppOpenAdManager {

        private static final String LOG_TAG = "AppOpenAdManager";
        private final GoogleMobileAdsConsentManager googleMobileAdsConsentManager =
                GoogleMobileAdsConsentManager.getInstance(getApplicationContext());
        private AppOpenAd appOpenAd = null;
        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;

        private long loadTime = 0;


        public AppOpenAdManager() {
        }


        private void loadAd(Context context) {
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            if (appData == null) {
                Log.e(LOG_TAG, "appData is null");
                return;
            }

            if ("adx".equals(appData.getadstype())) {
                adUnitId = appData.getAdxopenadid();
            } else if ("admob".equals(appData.getadstype())) {
                adUnitId = appData.getamOpenadid();
            }
            Log.e("adUnitId", "loadAd: " + adUnitId);

            if (adUnitId == null || adUnitId.isEmpty()) {
                Log.e(LOG_TAG, "Ad unit ID is empty or null");
                return;
            }

            isLoadingAd = true;
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    context,
                    adUnitId,
                    request,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            appOpenAd = ad;
                            isLoadingAd = false;
                            loadTime = (new Date()).getTime();

                            Log.d(LOG_TAG, "onAdLoaded.");
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            isLoadingAd = false;
                            Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                        }
                    });
        }


        private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * numHours));
        }

        private boolean isAdAvailable() {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
        }


        private void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(activity, new OnShowAdCompleteListener() {
                @Override
                public void onShowAdComplete() {
                    // Empty because the user will go back to the activity that shows the ad.
                }
            });
        }


        private void showAdIfAvailable(
                @NonNull final Activity activity,
                @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.");
                return;
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.");
                onShowAdCompleteListener.onShowAdComplete();
                if (googleMobileAdsConsentManager.canRequestAds()) {
                    loadAd(currentActivity);
                }
                return;
            }

            Log.d(LOG_TAG, "Will show ad.");

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");
//                            Toast.makeText(activity, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT).show();

                            onShowAdCompleteListener.onShowAdComplete();
                            if (googleMobileAdsConsentManager.canRequestAds()) {
                                loadAd(activity);
                            }
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
//                            Toast.makeText(activity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
//                                    .show();

                            onShowAdCompleteListener.onShowAdComplete();
                            if (googleMobileAdsConsentManager.canRequestAds()) {
                                loadAd(activity);
                            }
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(LOG_TAG, "onAdShowedFullScreenContent.");
//                            Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT).show();
                        }
                    });

            isShowingAd = true;
            appOpenAd.show(activity);
        }
    }
}
