package com.raktatech.winzip.activity;

import static com.raktatech.winzip.activity.GalleryActivity.TYPE_INT;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.ServiceStarter;
import com.raktatech.winzip.R;
import com.raktatech.winzip.databinding.ActivityExtractedProcessBinding;
import com.raktatech.winzip.databinding.ExtractDialogLayoutBinding;
import com.raktatech.winzip.utils.Common;

import com.raktatech.winzip.zip.IZipCallback;
import com.raktatech.winzip.zip.ZipManager;

import java.io.File;

public class ExtractedProcessActivity extends AppCompatActivity implements IZipCallback {
    public static String name = "demo";
    public static String outPath = null;
    public static String password = null;
    public static String path = "";
    ActivityExtractedProcessBinding binding;

    public void onBackPressed() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityExtractedProcessBinding inflate = ActivityExtractedProcessBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        ui();
    }

    private void ui() {
        Glide.with(this).load(R.raw.zip_folder_new).into(binding.lottie);
        path = getIntent().getStringExtra("Path");
        name = getIntent().getStringExtra("Name");
        password = getIntent().getStringExtra("Password");
        outPath = new File(Common.extractedPath, name).getAbsolutePath();
        Log.d("TAG", "ExtractedProcessActivity ui: " + ZipManager.checkFileIsProtected(path));
        this.binding.inBackground.setEnabled(false);
        if (ZipManager.checkFileIsProtected(path)) {
            if (password == null || password.isEmpty()) {
                extractDialog("File is protected, please enter password.");
            } else if (ZipManager.checkFilePassword(path, password)) {
                extractFile(path, outPath, password);
            } else {
                extractDialog("Wrong password. Please re-enter");
            }
        } else {
            extractFile(path, outPath, "");
        }
        this.binding.inBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.setFlags(268435456);
                startActivity(intent);
            }
        });
    }

    public void extractFile(String str, String str2, String str3) {
        this.binding.inBackground.setEnabled(true);
        ZipManager.unzip(str, str2, str3, this);
    }

    public void onStartIZip() {
        Log.d("TAG", "ExtractedProcessActivity onStart: ");
    }

    public void onProgress(int i) {
        Log.d("TAG", "ExtractedProcessActivity onProgress: " + i);
        this.binding.progress.setProgress(i);
    }

    public void onFinish(boolean z) {
        Log.d("TAG", "ExtractedProcessActivity onFinish: " + z);
        if (z) {
            this.binding.progress.setProgress(99);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ExtractedProcessActivity.this, CompleteProcessActivity.class);
                    intent.setFlags(536870912);
                    intent.putExtra("Type", 1);
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

    public void extractDialog(String str) {
        ExtractDialogLayoutBinding inflate = ExtractDialogLayoutBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(inflate.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        inflate.nameLl.setVisibility(8);
        inflate.msg.setVisibility(0);
        inflate.msg.setText(str);

        inflate.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (!ZipManager.checkFileIsProtected(path)) {
                    dialog.dismiss();
                    extractFile(path, outPath, "");
                } else {
                    String enteredPassword = inflate.password.getText().toString();
                    if (TextUtils.isEmpty(enteredPassword)) {
                        inflate.msg.setText(R.string.password_not_entered_please_enter_the_password);
                    } else if (ZipManager.checkFilePassword(path, enteredPassword)) {
                        dialog.dismiss();
                        extractFile(path, outPath, enteredPassword);
                    } else {
                        inflate.msg.setText(R.string.wrong_password_please_re_enter);
                    }
                }
            }
        });
        dialog.show();
    }

}
