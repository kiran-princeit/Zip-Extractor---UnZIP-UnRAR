package com.raktatech.winzip.activity;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.raktatech.winzip.databinding.ActivityPrivacyPolicyBinding;


public class PrivacyPolicyActivity extends AppCompatActivity {
    ActivityPrivacyPolicyBinding binding;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityPrivacyPolicyBinding inflate = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        this.binding.webview.getSettings().setAllowFileAccess(true);
        this.binding.webview.loadUrl("file:///android_res/raw/privacy_policy.html");
        this.binding.header.title.setText("Privacy Policy");
        this.binding.header.filter.setVisibility(8);
        this.binding.header.back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PrivacyPolicyActivity.this.finish();
            }
        });
        resize();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void resize() {
//        Resizer.getheightandwidth(this);
//        Resizer.setSize(this.binding.header.header, 1080, 182, true);
//        Resizer.setSize(this.binding.header.back, 60, 53, true);
    }
}
