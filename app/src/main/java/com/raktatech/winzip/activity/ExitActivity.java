package com.raktatech.winzip.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.raktatech.winzip.R;

public class ExitActivity extends AppCompatActivity {

    int star = 0;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_exit);
//        ActivityExitBinding inflate = ActivityExitBinding.inflate(getLayoutInflater());
//        this.binding = inflate;
//        setContentView((View) inflate.getRoot());


        findViewById(R.id.exit_text).setOnClickListener(view -> {
            ExitActivity.this.finishAffinity();
        });



//        this.binding.one.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                ExitActivity.this.star = 1;
//                ExitActivity.this.binding.one.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.two.setImageResource(R.drawable.star_unselected);
//                ExitActivity.this.binding.three.setImageResource(R.drawable.star_unselected);
//                ExitActivity.this.binding.four.setImageResource(R.drawable.star_unselected);
//                ExitActivity.this.binding.five.setImageResource(R.drawable.star_unselected);
//            }
//        });
//        this.binding.two.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                ExitActivity.this.star = 2;
//                ExitActivity.this.binding.one.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.two.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.three.setImageResource(R.drawable.star_unselected);
//                ExitActivity.this.binding.four.setImageResource(R.drawable.star_unselected);
//                ExitActivity.this.binding.five.setImageResource(R.drawable.star_unselected);
//            }
//        });
//        this.binding.three.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                ExitActivity.this.star = 3;
//                ExitActivity.this.binding.one.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.two.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.three.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.four.setImageResource(R.drawable.star_unselected);
//                ExitActivity.this.binding.five.setImageResource(R.drawable.star_unselected);
//            }
//        });
//        this.binding.four.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                ExitActivity.this.star = 4;
//                ExitActivity.this.binding.one.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.two.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.three.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.four.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.five.setImageResource(R.drawable.star_unselected);
//            }
//        });
//        this.binding.five.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                ExitActivity.this.star = 5;
//                ExitActivity.this.binding.one.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.two.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.three.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.four.setImageResource(R.drawable.star_selected);
//                ExitActivity.this.binding.five.setImageResource(R.drawable.star_selected);
//            }
//        });
    }


    public void onBackPressed() {
        startActivity(new Intent(this, StartActivity.class));
    }
}
