package com.raktatech.winzip.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.raktatech.winzip.adapter.ManagementAdapter;
import com.raktatech.winzip.databinding.ActivityManagementBinding;
import com.raktatech.winzip.interfaces.CommonInter;
import com.raktatech.winzip.model.DataModel;
import com.raktatech.winzip.utils.Common;

import java.util.Iterator;

public class ManagementActivity extends AppCompatActivity implements CommonInter {
    ActivityManagementBinding binding;
    ManagementAdapter managementAdapter;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityManagementBinding inflate = ActivityManagementBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        resize();
        ui();
    }

    private void ui() {
        TextView textView = this.binding.count;
        textView.setText("Selected " + Common.arrayListSelected.size() + " items");
        this.managementAdapter = new ManagementAdapter(this, Common.arrayListSelected, this);
        this.binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        this.binding.recyclerView.setAdapter(this.managementAdapter);
        this.binding.deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.arrayListSelected.clear();
                Iterator<DataModel> it = Common.arrayList.iterator();
                while (it.hasNext()) {
                    it.next().setCheck(false);
                }
                TextView textView = binding.count;
                textView.setText("Selected " + Common.arrayListSelected.size() + " items");
                managementAdapter.notifyAdapter(Common.arrayListSelected);
            }
        });
    }

    private void resize() {
//        Resizer.getheightandwidth(this);
//        Resizer.setSize(this.binding.header.header, 1080, 150, true);
//        Resizer.setSize(this.binding.header.back, 90, 90, true);
//        Resizer.setMargin(this.binding.header.back, 30, 0, 0, 0);
        this.binding.header.title.setText("Items selected management");
        this.binding.header.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void clickItem(int i, int i2) {
        setData(Common.arrayListSelected.get(i).getPath());
        Common.arrayListSelected.remove(i);
        this.managementAdapter.notifyAdapter(Common.arrayListSelected);
        TextView textView = this.binding.count;
        textView.setText("Selected " + Common.arrayListSelected.size() + " items");
    }

    public void setData(String str) {
        Iterator<DataModel> it = Common.arrayList.iterator();
        while (it.hasNext()) {
            DataModel next = it.next();
            if (next.getPath().equals(str)) {
                next.setCheck(false);
            }
        }
    }
}
