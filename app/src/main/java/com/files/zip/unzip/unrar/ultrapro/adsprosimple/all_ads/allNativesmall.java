package com.files.zip.unzip.unrar.ultrapro.adsprosimple.all_ads;

import static com.files.zip.unzip.unrar.ultrapro.adsprosimple.GlobalVar.appData;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdListener;
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

public class allNativesmall {

    public static void showNativeSmall(final ViewGroup nativeContainer, ShimmerFrameLayout shimmerFrameLayout, Activity activity) {

        if (!MyApp.isNetworkConnected(activity)) {
            nativeContainer.setVisibility(View.GONE);
            return;
        }

        if (appData.getshowNative() == 1) {
            switch (appData.getadstype()) {
                case "facebook":
                    fbNativeAdsSmall(activity,shimmerFrameLayout, nativeContainer);
                    break;

                case "admob":
                    amNativeAdsSmall(activity, shimmerFrameLayout, nativeContainer);
                    break;
                default:
                    nativeContainer.setVisibility(View.GONE);
                    break;
            }
        } else {
            nativeContainer.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }

    public static void fbNativeAdsSmall(final Activity context, ShimmerFrameLayout shimmerFrameLayout, final ViewGroup NativeContainer) {
        try {
            final com.facebook.ads.NativeAd nativeAd = new com.facebook.ads.NativeAd(context, appData.getfbNativeid());
            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {

                }
                @Override
                public void onError(Ad ad, AdError adError) {
                    try {

                        NativeContainer.removeAllViews();
                        NativeContainer.setVisibility(View.INVISIBLE);
                        if (shimmerFrameLayout.isShimmerStarted()) {
                            shimmerFrameLayout.stopShimmer();
                        }
                        shimmerFrameLayout.setVisibility(View.GONE);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onAdLoaded(Ad ad) {
                    try {
                        if (shimmerFrameLayout.isShimmerStarted()) {
                            shimmerFrameLayout.stopShimmer();
                        }
                        shimmerFrameLayout.setVisibility(View.GONE);
                        View adView = com.facebook.ads.NativeAdView.render(context, nativeAd, com.facebook.ads.NativeAdView.Type.HEIGHT_300);
                        NativeContainer.removeAllViews();
                        NativeContainer.setPadding(3, 3, 3, 3);
                        NativeContainer.addView(adView);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            };

            // Request an ad
            nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //****************************************************************************************************

    static NativeAd mPack1NativeAdsSmall = null;

    public static void amNativeAdsSmall(Activity activity, ShimmerFrameLayout shimmerFrameLayout, ViewGroup fl_adplaceholder) {
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
                showNativeSmall(fl_adplaceholder, shimmerFrameLayout, activity);
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


    public static void showNativeOptionAdSmall(ViewGroup fl_adplaceholder, Activity activity) {
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