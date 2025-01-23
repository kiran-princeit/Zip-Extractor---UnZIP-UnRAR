package com.files.zip.unzip.unrar.ultrapro.activity;

import static com.files.zip.unzip.unrar.ultrapro.adsprosimple.GlobalVar.appData;

import android.app.Application;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.widget.AppCompatButton;

import com.files.zip.unzip.unrar.ultrapro.MyApp;
import com.files.zip.unzip.unrar.ultrapro.R;
import com.files.zip.unzip.unrar.ultrapro.adsprosimple.MobileAds;
import com.files.zip.unzip.unrar.ultrapro.utils.GoogleMobileAdsConsentManager;
import com.files.zip.unzip.unrar.ultrapro.utils.LanguagePreference;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SplashActivity extends BaseActivity {
    private static final String LOG_TAG = "SplashActivity";
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    private final AtomicBoolean gatherConsentFinished = new AtomicBoolean(false);
    private GoogleMobileAdsConsentManager googleMobileAdsConsentManager;
    private static final long COUNTER_TIME_MILLISECONDS = 5000;
    private VideoView videoView;
    private long secondsRemaining;
    private Dialog dialog;
    private boolean isNetworkConnected = false;
    private NetworkChangeReceiver networkChangeReceiver;
    private boolean isFirebaseDataLoaded = false;  // Flag to track if Firebase data is loaded

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        videoView = findViewById(R.id.videoView);
        MobileAds.init(this, () -> {});
        MobileAds.init(this, new MobileAds.firebaseonloadcomplete() {
            @Override
            public void onloadcomplete() {
            }
        });
        createTimer();
        playVideo();
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);

        checkNetworkStatus();
    }

    private void checkNetworkStatus() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isNetworkConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isNetworkConnected) {
            loadFirebaseData();
        } else {
            showNoInternetDialog();
        }
    }

    private void loadFirebaseData() {
        MobileAds.init(this, new MobileAds.firebaseonloadcomplete() {
            @Override
            public void onloadcomplete() {
                if (appData != null) {
                    Log.e("data", "SplashActivity firebase data complete!");
                    isFirebaseDataLoaded = true;
                    if (appData.getshowOpenad() == 1) {
                        Log.e("data", "App should show open ad.");
                        initializeAds();
                    } else {
                        Log.e("data", "App should skip the ad.");
                        moveNext();
                    }
                } else {
                    Log.e("data", "appData is null, retrying...");
                    retryLoadData();
                }
            }
        });
    }

    private void retryLoadData() {
        new Handler().postDelayed(() -> {
            Log.e("data", "Retrying Firebase data load...");
            loadFirebaseData();
        }, 2000);
    }

    private void moveNext() {
//        if (!isFirebaseDataLoaded) return;
        new Handler().postDelayed(() -> {
            if (LanguagePreference.isFirstTime(this)) {
                Intent intent = new Intent(this, LanguageSelectionActivity.class);
                startActivity(intent);
                LanguagePreference.setFirstTime(this, false);
            } else {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }
            finish();
        }, 2000);
    }

    private void playVideo() {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
        videoView.setVideoURI(videoUri);
        videoView.start();
        videoView.setOnCompletionListener(mp -> {

        });
    }

    public void showNoInternetDialog() {
        dialog = new Dialog(SplashActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_nointernet);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);

        AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
        AppCompatButton btnPositive = dialog.findViewById(R.id.btnTyrAgain);

        btnPositive.setOnClickListener(v -> {
            if (isNetworkConnected) {
                dialog.dismiss();
                loadFirebaseData();
            } else {
                Toast.makeText(SplashActivity.this, "Still no internet", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void createTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(COUNTER_TIME_MILLISECONDS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1;
            }
            @Override
            public void onFinish() {
                secondsRemaining = 0;
                Application application = getApplication();
                ((MyApp) application)
                        .showAdIfAvailable(
                                SplashActivity.this,
                                new MyApp.OnShowAdCompleteListener() {
                                    @Override
                                    public void onShowAdComplete() {
                                        moveNext();
                                    }
                                });
            }
        };
        countDownTimer.start();
    }

    private void initializeAds() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        com.google.android.gms.ads.MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder()
                        .setTestDeviceIds(Arrays.asList(MyApp.TEST_DEVICE_HASHED_ID))
                        .build());

        new Thread(() -> {
            com.google.android.gms.ads.MobileAds.initialize(this, initializationStatus -> {});
            runOnUiThread(() -> {
                Application application = getApplication();
                ((MyApp) application).loadAd(this);  // Load and show the ad
            });
        }).start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

//            if (isConnected) {
//                Log.d("NetworkChangeReceiver", "Network connected");
//                if (!isNetworkConnected) {
//                    isNetworkConnected = true;
////                    loadFirebaseData();
//                    moveNext();
//                }
//            } else {
//                Log.d("NetworkChangeReceiver", "No internet connection");
//                if (isNetworkConnected) {
//                    isNetworkConnected = false;
//                    showNoInternetDialog();
//                }
//            }
        }
    }
}
