package com.raktatech.winzip.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.raktatech.winzip.R;

import com.raktatech.winzip.databinding.ActivityStartBinding;
import com.raktatech.winzip.utils.Common;

import com.raktatech.winzip.utils.StorageUtils;

public class StartActivity extends AppCompatActivity {
    ActivityStartBinding binding;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityStartBinding inflate = ActivityStartBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
//        new AdsPreloadUtils(this).callPreloadSmallNative(AdsVariable.full_Home_Activity_Small_Native);
        this.binding.start.setOnClickListener(new StartActivity$$ExternalSyntheticLambda0(this));
        resize();
//        AdsNativeSmallUtils adsNativeSmallUtils = new AdsNativeSmallUtils(this);
//        this.binding.mainNative.constraintAds.setBackground(adsNativeSmallUtils.getRoundRectForBg());
//        this.binding.mainNative.shimmerEffect.startShimmer();
//        adsNativeSmallUtils.callAdMobNative(this.binding.mainNative.nativeView1.flNativeAds, AdsVariable.full_Start_Activity_Big_Native, this, new AdsNativeSmallUtils.AdsInterface() {
//            public void nextActivity() {
//                StartActivity.this.binding.mainNative.getRoot().setVisibility(0);
//                StartActivity.this.binding.mainNative.nativeView1.flNativeAds.setVisibility(0);
//                StartActivity.this.binding.mainNative.shimmerEffect.setVisibility(8);
//            }
//
//            public void loadToFail() {
//                StartActivity.this.binding.mainNative.getRoot().setVisibility(8);
//            }
//        });
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$com-raktatech-winzip-easyrarfileextractor-activity-StartActivity  reason: not valid java name */
    public /* synthetic */ void m1204lambda$onCreate$0$comraktatechwinzipeasyrarfileextractoractivityStartActivity(View view) {
        if (!checkPermission()) {
            callPermission();
        } else if (Build.VERSION.SDK_INT < 30) {
            nextActivity();
        } else if (Environment.isExternalStorageManager()) {
            nextActivity();
        } else {
            requestPermission();
        }
    }

    public boolean checkPermission() {
        return Build.VERSION.SDK_INT >= 33 ? (ContextCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_VIDEO") + ContextCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_IMAGES")) + ContextCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_AUDIO") == 0 : ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") + ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public void callPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO", "android.permission.READ_MEDIA_AUDIO"}, 123);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 123);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 123) {
            return;
        }
        if (iArr.length <= 0 || iArr[0] != 0) {
            Toast.makeText(this, "Please Allow Permission", 0).show();
        } else if (Build.VERSION.SDK_INT < 30) {
            nextActivity();
        } else if (Environment.isExternalStorageManager()) {
            nextActivity();
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            try {
                Intent intent = new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                startActivityForResult(intent, 2296);
            } catch (Exception unused) {
                Intent intent2 = new Intent();
                intent2.setAction("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION");
                startActivityForResult(intent2, 2296);
            }
        }
    }

    public void nextActivity() {
        Common.compressedPath = StorageUtils.create_folder_with_sub_folder(getString(R.string.app_name), "Compressed");
        Common.extractedPath = StorageUtils.create_folder_with_sub_folder(getString(R.string.app_name), "Extracted");
        startActivity(new Intent(this, HomeActivity.class).setFlags(536870912));
    }

    private void resize() {
//        Resizer.getheightandwidth(this);
//        Resizer.setSize(this.binding.zipTextlogo, 598, 88, true);
//        Resizer.setSize(this.binding.zipImglogo, 750, 694, true);
//        Resizer.setSize(this.binding.start, 832, 374, true);
//        Resizer.setMargin(this.binding.zipTextlogo, 0, 150, 0, 0);
    }

    public void onBackPressed() {
        startActivity(new Intent(this, ExitActivity.class));
    }
}
