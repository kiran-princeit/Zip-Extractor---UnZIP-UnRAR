package com.files.zip.unzip.unrar.ultrapro.adsprosimple.fb_ads;

import static com.files.zip.unzip.unrar.ultrapro.adsprosimple.GlobalVar.appData;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;

public class FB_NativeAds {

    public static void setNativeAds(final Activity context, final ViewGroup NativeContainer) {
        if (appData.getshowNative() == 1) {
            try {
//                if (fbnativearraycount > strFBNative.length - 1)
//                    fbnativearraycount = 0;
//                final com.facebook.ads.NativeAd nativeAd = new com.facebook.ads.NativeAd(context, strFBNative[fbnativearraycount]);

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

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
//                        Log.e("xxx", "FB Loaded ! " + fbnativearraycount);
//                        fbnativearraycount++;

                        try {

//                            Log.e("xxx", "Loaded FB");

                            View adView = NativeAdView.render(context, nativeAd, NativeAdView.Type.HEIGHT_300);
                            NativeContainer.removeAllViews();
                            NativeContainer.setPadding(3, 3, 3, 3);
//                            NativeContainer.setBackgroundColor(Color.parseColor("#FFC107"));
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

        } else {
            NativeContainer.setVisibility(View.INVISIBLE);
        }
    }

}