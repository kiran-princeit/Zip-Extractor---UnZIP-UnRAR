package com.files.zip.unzip.unrar.ultrapro.adsprosimple.am_ads;

import static com.files.zip.unzip.unrar.ultrapro.adsprosimple.GlobalVar.appData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class AM_Banner {
    public static void loadAdMobBanner(RelativeLayout adContainerView, ShimmerFrameLayout shimmerFrameLayout, Activity activity) {
        if (appData.getshowBanner() == 1) {
            String adUnitId = appData.getamBannerid();

            if (TextUtils.isEmpty(adUnitId)) {
                return;
            }

            //BannerAd
            AdView admobManagerAdView = new AdView(activity);
            admobManagerAdView.setAdUnitId(adUnitId);
            adContainerView.addView(admobManagerAdView);
            AdRequest adRequest = new AdRequest.Builder().build();
            AdSize adSize = getAdSize(adContainerView, activity);
            admobManagerAdView.setAdSize(adSize);
            admobManagerAdView.loadAd(adRequest);

            admobManagerAdView.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                    adContainerView.setVisibility(View.VISIBLE);
                    if (shimmerFrameLayout.isShimmerStarted()) {
                        shimmerFrameLayout.stopShimmer();
                    }
                    shimmerFrameLayout.setVisibility(View.GONE);
                    Log.e("BannerAd", "BannerAd ==> onAdLoaded");
                    super.onAdLoaded();

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    adContainerView.setVisibility(View.GONE);
                    Log.e("BannerAd", "BannerAd ==> onAdFailedToLoad " + loadAdError);
                    if (shimmerFrameLayout.isShimmerStarted()) {
                        shimmerFrameLayout.stopShimmer();
                    }
                    shimmerFrameLayout.setVisibility(View.GONE);
                    super.onAdFailedToLoad(loadAdError);
                }
            });
        } else {
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NewApi")
    public static AdSize getAdSize(RelativeLayout adContainerView, Activity activity) {
        WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
        Rect bounds = windowMetrics.getBounds();

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0f) {
            adWidthPixels = bounds.width();
        }

        float density = activity.getResources().getDisplayMetrics().density;
        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

}