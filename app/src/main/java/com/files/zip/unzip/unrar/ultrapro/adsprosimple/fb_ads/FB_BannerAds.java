package com.files.zip.unzip.unrar.ultrapro.adsprosimple.fb_ads;

import static com.files.zip.unzip.unrar.ultrapro.adsprosimple.GlobalVar.appData;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

public class FB_BannerAds {

    public static void setBannerAds(final Activity context, final ViewGroup BannerContainer) {

        if (appData.getshowBanner() == 1) {
            try {
                BannerContainer.setVisibility(View.VISIBLE);

                final AdView adView = new AdView(context, appData.getfbBannerid(), AdSize.BANNER_HEIGHT_50);

                com.facebook.ads.AdListener adlistner = new com.facebook.ads.AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {
                        BannerContainer.removeAllViews();
                        BannerContainer.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        try {
                            BannerContainer.setPadding(3, 3, 3, 3);
//                            BannerContainer.setBackgroundColor(Color.parseColor("#FFC107"));
                            BannerContainer.removeAllViews();
                            BannerContainer.addView(adView);
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
                adView.loadAd(adView.buildLoadAdConfig().withAdListener(adlistner).build());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            BannerContainer.setVisibility(View.INVISIBLE);
        }
    }

}