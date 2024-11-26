package com.raktatech.winzip.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.raktatech.winzip.R;
import com.raktatech.winzip.databinding.ActivityCompressedProcessBinding;
import com.raktatech.winzip.utils.Common;
import com.raktatech.winzip.zip.IZipCallback;
import com.raktatech.winzip.zip.ZipManager;

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

        ui();
    }

    private void ui() {
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
        Log.d("TAG", "CompressedProcessActivity onStart: ");
        this.binding.inBackground.setEnabled(true);
    }

    public void onProgress(int i) {
        Log.d("TAG", "CompressedProcessActivity onProgress: " + i);
        this.binding.progress.setProgress(i);
    }

    public void onFinish(boolean z) {
        Log.d("TAG", "CompressedProcessActivity onFinish: ");
        if (z) {
            this.binding.progress.setProgress(100);
            ZipManager.setCommandForCustom(this.outPathname, password);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(CompressedProcessActivity.this, CompleteProcessActivity.class);
                    intent.setFlags(536870912);
                    intent.putExtra("Type", 0);
                    startActivity(intent);
                    finish();
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



//public class CompressedProcessActivity extends AppCompatActivity implements IZipCallback {
//    public static ArrayList<File> compressedList = new ArrayList<>();
//    public static String extension = ".zip";
//    public static String name = "demo";
//    public static String password = null;
//    ActivityCompressedProcessBinding binding;
//    String outPathname;
//    private Dialog compressionDialog;
//
//    public void onBackPressed() {
//        if (compressionDialog != null && compressionDialog.isShowing()) {
//            compressionDialog.dismiss();
//        }
//    }
//
//    public void onCreate(Bundle bundle) {
//        super.onCreate(bundle);
//        ActivityCompressedProcessBinding inflate = ActivityCompressedProcessBinding.inflate(getLayoutInflater());
//        this.binding = inflate;
//        setContentView((View) inflate.getRoot());
//        setupCompressionDialog(); // Initialize the dialog
//        ui();
//    }
//
//    private void setupCompressionDialog() {
//        compressionDialog = new Dialog(this);
//        compressionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        compressionDialog.setCancelable(false);
//        compressionDialog.setContentView(R.layout.compression_progress_dialog);
//        compressionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        Button closeButton = compressionDialog.findViewById(R.id.btn_close);
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                compressionDialog.dismiss();
//                finish();
//            }
//        });
//    }
//
//    private void ui() {
//        password = getIntent().getStringExtra("Password");
//        name = getIntent().getStringExtra("Name");
//        extension = getIntent().getStringExtra("Extension");
//        String str = Common.compressedPath;
//        this.outPathname = new File(str, name + extension).getAbsolutePath();
//        if (password == null) {
//            password = "";
//        }
//        this.binding.inBackground.setEnabled(false);
//        this.binding.inBackground.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent("android.intent.action.MAIN");
//                intent.addCategory("android.intent.category.HOME");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });
//        compressionDialog.show(); // Show the dialog when compression starts
//        ZipManager.zip(compressedList, this.outPathname, password, this);
//    }
//
//    public void onStartIZip() {
//        Log.d("TAG", "CompressedProcessActivity onStart: ");
//        this.binding.inBackground.setEnabled(true);
//    }
//
//    public void onProgress(int i) {
//        Log.d("TAG", "CompressedProcessActivity onProgress: " + i);
//        this.binding.progress.setProgress(i);
//
//        // Update progress in dialog
//        if (compressionDialog.isShowing()) {
//            TextView progressText = compressionDialog.findViewById(R.id.dialog_progress_text);
//            progressText.setText("Progress: " + i + "%");
//        }
//    }
//
//    public void onFinish(boolean z) {
//        Log.d("TAG", "CompressedProcessActivity onFinish: ");
//        if (z) {
//            this.binding.progress.setProgress(100);
//            if (compressionDialog.isShowing()) {
//                TextView progressText = compressionDialog.findViewById(R.id.dialog_progress_text);
//                progressText.setText("Compression Complete!");
//            }
//            ZipManager.setCommandForCustom(this.outPathname, password);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(CompressedProcessActivity.this, CompleteProcessActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("Type", 0);
//                    startActivity(intent);
//                    finish();
//                }
//            }, 1000);
//        } else {
//            if (compressionDialog.isShowing()) {
//                TextView progressText = compressionDialog.findViewById(R.id.dialog_progress_text);
//                progressText.setText("Compression Failed!");
//            }
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            }, 1000);
//        }
//    }
//}

