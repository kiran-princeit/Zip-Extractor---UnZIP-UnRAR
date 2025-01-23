package com.files.zip.unzip.unrar.ultrapro.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.files.zip.unzip.unrar.ultrapro.R;
import com.files.zip.unzip.unrar.ultrapro.adsprosimple.MobileAds;
import com.files.zip.unzip.unrar.ultrapro.databinding.ActivityCompleteProcessBinding;


public class CompleteProcessActivity extends BaseActivity {
    ActivityCompleteProcessBinding binding;
    int type;

    @SuppressLint("SetTextI18n")
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityCompleteProcessBinding inflate = ActivityCompleteProcessBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        MobileAds.showBanner(binding.adContainerBanner, binding.shimmerContainerBanner, CompleteProcessActivity.this);

        this.type = getIntent().getIntExtra("Type", 0);
        resize();
        TextView textView = this.binding.msg;
        int i = this.type;
        textView.setText(i == 2 ? getResources().getString(R.string.file_delete) : i == 0 ? getResources().getString(R.string.file_complete_compressed) : getResources().getString(R.string.file_complete_extracted));
        this.binding.header.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.binding.openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (this.type == 2) {
            this.binding.openFile.setText(getResources().getString(R.string.done));
        }
    }

    public void onBackPressed() {

        if (CompleteProcessActivity.this.type == 2) {
            CompleteProcessActivity.this.finish();
            return;
        }
        Intent intent = new Intent(CompleteProcessActivity.this, FolderActivity.class);
        intent.setFlags(536870912);
        intent.putExtra("Type", CompleteProcessActivity.this.type == 0 ? 3 : 0);
        CompleteProcessActivity.this.startActivity(intent);
        CompleteProcessActivity.this.finish();

    }

    private void resize() {
        this.binding.header.title.setText(getResources().getString(R.string.complete));
    }
}
