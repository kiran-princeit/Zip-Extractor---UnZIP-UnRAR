package com.files.zip.unzip.unrar.ultrapro.adsprosimple.am_ads;

import static com.files.zip.unzip.unrar.ultrapro.adsprosimple.GlobalVar.appData;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.files.zip.unzip.unrar.ultrapro.MyApp;
import com.files.zip.unzip.unrar.ultrapro.R;

import java.util.Objects;

public class AM_NativeSmall {

    static NativeAd mPack1NativeAdsSmall = null;

    public static void showNativeAdsSmallMain(RelativeLayout fl_adplaceholder, ShimmerFrameLayout shimmerFrameLayout, Activity activity) {
        if (appData.getshowNative() == 1) {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            fl_adplaceholder.setVisibility(View.GONE);
            if (!shimmerFrameLayout.isShimmerStarted()) {
                shimmerFrameLayout.startShimmer();
            }

            if (!MyApp.isNetworkConnected(activity)) {
                shimmerFrameLayout.setVisibility(View.GONE);
                fl_adplaceholder.setVisibility(View.GONE);
                return;
            }

            loadAdmobNativeAdBig(fl_adplaceholder, shimmerFrameLayout, activity);

        } else {
            shimmerFrameLayout.setVisibility(View.GONE);
            fl_adplaceholder.setVisibility(View.GONE);
        }

    }

    public static void loadAdmobNativeAdBig(RelativeLayout fl_adplaceholder, ShimmerFrameLayout shimmerFrameLayout, Activity activity) {
        String adUnitId = appData.getamNativeid();
        if (TextUtils.isEmpty(adUnitId)) {
            return;
        }

        AdLoader.Builder builder = new AdLoader.Builder(activity, adUnitId);
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                mPack1NativeAdsSmall = nativeAd;
                showNativeOptionAdSmall(fl_adplaceholder, activity);
                if (shimmerFrameLayout.isShimmerStarted()) {
                    shimmerFrameLayout.stopShimmer();
                }
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });


        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                fl_adplaceholder.removeAllViews();
                showNativeAdsSmallMain(fl_adplaceholder, shimmerFrameLayout, activity);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                if (shimmerFrameLayout.isShimmerStarted()) {
                    shimmerFrameLayout.stopShimmer();
                }
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public static void showNativeOptionAdSmall(RelativeLayout fl_adplaceholder, Activity activity) {
        try {
            if (mPack1NativeAdsSmall != null) {

                NativeAdView adView = (NativeAdView) LayoutInflater.from(activity).inflate(R.layout.ad_unifiled_small, null);

                populateUnifiedNativeRegularAdView(mPack1NativeAdsSmall, adView);

                fl_adplaceholder.setVisibility(View.VISIBLE);
                fl_adplaceholder.removeAllViews();
                fl_adplaceholder.addView(adView);
            } else {
                fl_adplaceholder.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fl_adplaceholder.setVisibility(View.GONE);
        }
    }

    public static void populateUnifiedNativeRegularAdView(NativeAd nativeAd, NativeAdView adView) {

        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        TextView headlineView = adView.findViewById(R.id.ad_headline);
        headlineView.setText(nativeAd.getHeadline());
        adView.setHeadlineView(headlineView);


        TextView bodyView = adView.findViewById(R.id.ad_body);
        TextView callToActionView = adView.findViewById(R.id.ad_call_to_action);
        ImageView iconView = adView.findViewById(R.id.ad_app_icon);
        TextView priceView = adView.findViewById(R.id.ad_price);
        RatingBar starRatingView = adView.findViewById(R.id.ad_stars);
        TextView storeView = adView.findViewById(R.id.ad_store);
        TextView advertiserView = adView.findViewById(R.id.ad_advertiser);


        if (nativeAd.getBody() == null) {
            bodyView.setVisibility(View.INVISIBLE);
        } else {
            bodyView.setVisibility(View.VISIBLE);
            bodyView.setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            callToActionView.setVisibility(View.INVISIBLE);
        } else {
            callToActionView.setVisibility(View.VISIBLE);
            callToActionView.setText(nativeAd.getCallToAction());
            adView.setCallToActionView(callToActionView);
        }

        if (nativeAd.getIcon() == null) {
            iconView.setVisibility(View.INVISIBLE);
        } else {
            iconView.setVisibility(View.VISIBLE);
            iconView.setImageDrawable(nativeAd.getIcon().getDrawable());
        }

        if (nativeAd.getPrice() == null) {
            priceView.setVisibility(View.INVISIBLE);
        } else {
            priceView.setVisibility(View.VISIBLE);
            priceView.setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            storeView.setVisibility(View.INVISIBLE);
        } else {
            storeView.setVisibility(View.VISIBLE);
            storeView.setText(nativeAd.getStore());
        }

        if (nativeAd.getStore() == null) {
            starRatingView.setVisibility(View.INVISIBLE);
        } else {
            starRatingView.setVisibility(View.VISIBLE);
            starRatingView.setRating(Objects.requireNonNull(nativeAd.getStarRating()).floatValue());
        }


        if (nativeAd.getAdvertiser() == null) {
            advertiserView.setVisibility(View.INVISIBLE);
        } else {
            advertiserView.setVisibility(View.VISIBLE);
            advertiserView.setText(nativeAd.getAdvertiser());
        }

        storeView.setVisibility(View.GONE);
        priceView.setVisibility(View.GONE);

        adView.setNativeAd(nativeAd);

        VideoController vc = Objects.requireNonNull(nativeAd.getMediaContent()).getVideoController();

        if (vc.hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }

    }
}