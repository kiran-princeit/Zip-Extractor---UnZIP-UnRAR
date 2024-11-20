package com.raktatech.winzip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.raktatech.winzip.R;

import com.raktatech.winzip.databinding.ActivityCompleteProcessBinding;
import com.raktatech.winzip.utils.Resizer;


public class CompleteProcessActivity extends AppCompatActivity {
    ActivityCompleteProcessBinding binding;
    int type;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityCompleteProcessBinding inflate = ActivityCompleteProcessBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());

        this.type = getIntent().getIntExtra("Type", 0);
        resize();
        TextView textView = this.binding.msg;
        int i = this.type;
        textView.setText(i == 2 ? "File Deleted Successfully." : i == 0 ? "You've Completely Compressed your File, Share file now!" : "You've Completely Extracted your File!");
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
            this.binding.openFile.setImageResource(R.drawable.effect_gotofile);
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
        Resizer.getheightandwidth(this);
        Resizer.setSize(this.binding.header.header, 1080, 154, true);
        Resizer.setSize(this.binding.header.back, 60, 53, true);
        Resizer.setSize(this.binding.completeLogo, 800, 721, true);
        Resizer.setSize(this.binding.openFile, 620, 130, true);
        Resizer.setMargin(this.binding.header.back, 30, 0, 0, 0);
        Resizer.setMargin(this.binding.openFile, 0, 100, 0, 100);
        this.binding.header.title.setText("Complete");
    }
}
