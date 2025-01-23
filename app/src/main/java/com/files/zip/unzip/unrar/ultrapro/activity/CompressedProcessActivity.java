package com.files.zip.unzip.unrar.ultrapro.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.bumptech.glide.Glide;
import com.files.zip.unzip.unrar.ultrapro.R;
import com.files.zip.unzip.unrar.ultrapro.adsprosimple.MobileAds;
import com.files.zip.unzip.unrar.ultrapro.databinding.ActivityCompressedProcessBinding;
import com.files.zip.unzip.unrar.ultrapro.utils.Common;
import com.files.zip.unzip.unrar.ultrapro.zip.IZipCallback;
import com.files.zip.unzip.unrar.ultrapro.zip.ZipManager;

import java.io.File;
import java.util.ArrayList;

public class CompressedProcessActivity extends BaseActivity implements IZipCallback {
    public static ArrayList<File> compressedList = new ArrayList<>();
    public static String extension = ".zip";
    public static String name = "demo";
    public static String password = null;
    ActivityCompressedProcessBinding binding;
    String outPathname;

    public void onBackPressed() {
        finish();
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityCompressedProcessBinding inflate = ActivityCompressedProcessBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
//        MobileAds.showBanner(binding.adContainerBanner, binding.shimmerContainerBanner, FolderActivity.this);
        ui();
    }

    private void ui() {
        Glide.with(this).load(R.raw.zip_folder_new).into(binding.lottie);
        password = getIntent().getStringExtra("Password");
        name = getIntent().getStringExtra("Name");
        extension = getIntent().getStringExtra("Extension");
        String str = Common.compressedPath;
        this.outPathname = new File(str, name + extension).getAbsolutePath();
        if (password == null) {
            password = "";
        }
        this.binding.inBackground.setEnabled(false);
        this.binding.inBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.setFlags(268435456);
                startActivity(intent);
            }
        });
        ZipManager.zip(compressedList, this.outPathname, password, (IZipCallback) this);
    }

    public void onStartIZip() {
        this.binding.inBackground.setEnabled(true);
    }

    public void onProgress(int i) {
        this.binding.progress.setProgress(i);
    }

    public void onFinish(boolean z) {
        if (z) {
            this.binding.progress.setProgress(100);
            ZipManager.setCommandForCustom(this.outPathname, password);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MobileAds.showInterstitial(CompressedProcessActivity.this, () -> {
                        Intent intent = new Intent(CompressedProcessActivity.this, CompleteProcessActivity.class);
                        intent.setFlags(536870912);
                        intent.putExtra("Type", 0);
                        startActivity(intent);
                        finish();
                    });
                }
            }, 1000);
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

}


