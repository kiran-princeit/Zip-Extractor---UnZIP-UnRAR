package com.files.zip.unzip.unrar.ultrapro.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.files.zip.unzip.unrar.ultrapro.R;
import com.files.zip.unzip.unrar.ultrapro.adsprosimple.MobileAds;
import com.files.zip.unzip.unrar.ultrapro.databinding.ActivityHomeBinding;
import com.files.zip.unzip.unrar.ultrapro.utils.Common;
import com.files.zip.unzip.unrar.ultrapro.utils.StorageUtils;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.apache.commons.io.FileUtils;

public class HomeActivity extends BaseActivity {
    public static int Home_Ads_Flag = 1;
    ActivityHomeBinding binding;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private long mLastClickTime = 0;
    int onbtnClick;
    Dialog dialogExit;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityHomeBinding inflate = ActivityHomeBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        com.google.android.gms.ads.MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(
                            @NonNull InitializationStatus initializationStatus) {
                    }
                });

        MobileAds.init(this, new MobileAds.firebaseonloadcomplete() {
            @Override
            public void onloadcomplete() {

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        MobileAds.showBanner(binding.adContainerBanner, binding.shimmerContainerBanner, HomeActivity.this);

        setSupportActionBar(binding.mtoolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.mtoolbar, // Pass the toolbar here
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.menu);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        binding.mtoolbar.setNavigationOnClickListener(v -> {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        binding.ivMenu.setOnClickListener(view -> {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        binding.menuShare.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            String shareText = "Check out this amazing wallpaper I found on " +
                    getString(R.string.app_name) + "! Download the app and explore more beautiful wallpapers for your phone! " +
                    "https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent ishare = new Intent(Intent.ACTION_SEND);
            ishare.setType("text/plain");
//            ishare.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(ishare);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });
        binding.menuPrivacy.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://wallpaperia-hdwallpaper.blogspot.com/2024/11/zip-extractor-unzip-unrar.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });
        binding.menuRate.setOnClickListener(view -> {
            final String appName = getPackageName();//your application package name i.e play store application url
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id="
                                + appName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id="
                                + appName)));
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START);

        });

        binding.menuLanguage.setOnClickListener(view -> {
            MobileAds.showInterstitial(HomeActivity.this, () -> {
                startActivity(new Intent(HomeActivity.this, LanguageSelectionActivity.class));
                binding.drawerLayout.openDrawer(GravityCompat.START);

            });
        });

        Common.compressedPath = StorageUtils.create_folder_with_sub_folder("Zip Extractor", "Compressed");
        Common.extractedPath = StorageUtils.create_folder_with_sub_folder("Zip Extractor", "Extracted");

        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long blockSize = (((long) statFs.getBlockSize()) * ((long) statFs.getBlockCount())) / FileUtils.ONE_MB;
        StatFs statFs2 = new StatFs(Environment.getDataDirectory().getPath());
        String formatFileSize = Formatter.formatFileSize(this, ((long) statFs2.getAvailableBlocks()) * ((long) statFs2.getBlockSize()));
        String folderSize = StorageUtils.folderSize(getAvailableInternalMemorySize());
        String folderSize2 = StorageUtils.folderSize(getTotalInternalMemorySize());
        TextView textView = this.binding.internalStorageSize;
        TextView textView2 = this.binding.internalStorageSizeTotel;
        textView.setText(folderSize);
        textView2.setText(folderSize2);

        double rawValue = 100.0 - ((getAvailableInternalMemorySize() * 100.0) / getTotalInternalMemorySize());
        double formattedValue = Math.round(rawValue * 100.0) / 100.0;
        this.binding.progressBar.setCurrentProgress(formattedValue);
        binding.progressBar.setProgressTextAdapter(value -> value + "%");
        onClick();
    }
    public void rate(View view) {
        StorageUtils.rate_app(this);
    }

    public static double getAvailableInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return (double) (statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong());
    }

    public static double getTotalInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return (double) (statFs.getBlockCountLong() * statFs.getBlockSizeLong());
    }

    private void onClick() {
        this.binding.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onbtnClick = 1;
                if (!checkPermission()) {
                    callPermission();
                } else if (Build.VERSION.SDK_INT < 30) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, GalleryActivity.class).putExtra("Type", 0).setFlags(536870912));
                    });
                } else if (Environment.isExternalStorageManager()) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, GalleryActivity.class).putExtra("Type", 0).setFlags(536870912));
                    });
                } else {
                    showPermissionDialog();
                }
            }
        });


        this.binding.videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission()) {
                    callPermission();
                } else if (Build.VERSION.SDK_INT < 30) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, GalleryActivity.class).putExtra("Type", 1).setFlags(536870912));
                    });
                } else if (Environment.isExternalStorageManager()) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, GalleryActivity.class).putExtra("Type", 1).setFlags(536870912));
                    });
                } else {
                    showPermissionDialog();
                }
            }
        });
        this.binding.extracted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission()) {
                    callPermission();
                } else if (Build.VERSION.SDK_INT < 30) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, FolderActivity.class).putExtra("Type", 0).setFlags(536870912));
                    });
                } else if (Environment.isExternalStorageManager()) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, FolderActivity.class).putExtra("Type", 0).setFlags(536870912));
                    });
                } else {
                    showPermissionDialog();
                }
            }
        });
        this.binding.internalStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission()) {
                    callPermission();
                } else if (Build.VERSION.SDK_INT < 30) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, FolderActivity.class).putExtra("Type", 1).setFlags(536870912));
                    });
                } else if (Environment.isExternalStorageManager()) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, FolderActivity.class).putExtra("Type", 1).setFlags(536870912));
                    });
                } else {
                    showPermissionDialog();
                }
            }
        });
        this.binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission()) {
                    callPermission();
                } else if (Build.VERSION.SDK_INT < 30) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, FolderActivity.class).putExtra("Type", 2).setFlags(536870912));
                    });
                } else if (Environment.isExternalStorageManager()) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, FolderActivity.class).putExtra("Type", 2).setFlags(536870912));
                    });
                } else {
                    showPermissionDialog();
                }
            }
        });
        this.binding.compressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission()) {
                    callPermission();
                } else if (Build.VERSION.SDK_INT < 30) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, FolderActivity.class).putExtra("Type", 3).setFlags(536870912));
                    });
                } else if (Environment.isExternalStorageManager()) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, FolderActivity.class).putExtra("Type", 3).setFlags(536870912));
                    });
                } else {
                    showPermissionDialog();
                }
            }
        });
        this.binding.archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission()) {
                    callPermission();
                } else if (Build.VERSION.SDK_INT < 30) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, ArchiveActivity.class).putExtra("Type", 5).setFlags(536870912));
                    });
                } else if (Environment.isExternalStorageManager()) {
                    MobileAds.showInterstitial(HomeActivity.this, () -> {
                        startActivity(new Intent(HomeActivity.this, ArchiveActivity.class).putExtra("Type", 5).setFlags(536870912));
                    });
                } else {
                    showPermissionDialog();
                }
            }
        });
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
        } else if (Build.VERSION.SDK_INT >= 30 && !Environment.isExternalStorageManager()) {
            showPermissionDialog();
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


    private void showPermissionDialog() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_permission);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Width match parent
        dialog.getWindow().setAttributes(layoutParams);


        AppCompatButton btnCancel = dialog.findViewById(R.id.btnClose);
        AppCompatButton btnallow = dialog.findViewById(R.id.btnallow);

        btnallow.setOnClickListener(v -> {
            requestPermission();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (isFinishing()) {
            if (dialogExit != null && dialogExit.isShowing()) {
                dialogExit.dismiss();
            }
        } else {
            showExitDialog();
        }
    }


    private void showExitDialog() {
        dialogExit = new Dialog(HomeActivity.this);
        dialogExit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogExit.setCancelable(false);
        dialogExit.setContentView(R.layout.dialog_exit);
        dialogExit.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogExit.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Width match parent
        dialogExit.getWindow().setAttributes(layoutParams);
        AppCompatButton btnCancel = dialogExit.findViewById(R.id.btnCancel);
        AppCompatButton btnExit = dialogExit.findViewById(R.id.btnExit);
        RelativeLayout adContainerBanner = dialogExit.findViewById(R.id.adContainerBanner);
        ShimmerFrameLayout shimmerContainerBanner = dialogExit.findViewById(R.id.shimmer_container_banner);
        MobileAds.showNativeBig(adContainerBanner, shimmerContainerBanner, HomeActivity.this);

        btnExit.setOnClickListener(v -> {
            finishAffinity();
        });

        btnCancel.setOnClickListener(v -> {
            dialogExit.dismiss();
        });

        dialogExit.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogExit != null && dialogExit.isShowing()) {
            dialogExit.dismiss();
        }
    }
}
