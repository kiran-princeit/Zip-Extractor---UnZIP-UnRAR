package com.raktatech.winzip.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raktatech.winzip.R;
import com.raktatech.winzip.adapter.GalleryAdapter;

import com.raktatech.winzip.databinding.ActivityGalleryBinding;
import com.raktatech.winzip.databinding.CompressDialogLayoutBinding;
import com.raktatech.winzip.databinding.CustomProgressDialogBinding;
import com.raktatech.winzip.databinding.DeleteDialogLayoutBinding;
import com.raktatech.winzip.databinding.ExtractDialogLayoutBinding;
import com.raktatech.winzip.databinding.RenameDialogLayoutBinding;
import com.raktatech.winzip.enums.TYPE;
import com.raktatech.winzip.interfaces.CommonInter;
import com.raktatech.winzip.interfaces.OnProgressUpdate;
import com.raktatech.winzip.model.DataModel;
import com.raktatech.winzip.utils.Common;
import com.raktatech.winzip.utils.GalleryAsyncTask;

import com.raktatech.winzip.utils.Resizer;
import com.raktatech.winzip.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class GalleryActivity extends AppCompatActivity implements CommonInter {

    public static final int TYPE_ANIMATE_CIRCLEANGLE_TO = 606;
    public static final int TYPE_INT = 900;
    private static final String TAG = "GalleryActivity";
    ActivityGalleryBinding binding;
    public boolean check = false;
    ArrayList<DataModel> documentList = new ArrayList<>();
    GalleryAdapter galleryAdapter;
    int passType;
    PopupWindow popupWindow;
    Dialog progressDialog;
    int spanCount = 3;
    TYPE type;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityGalleryBinding inflate = ActivityGalleryBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.progressDialog = new Dialog(this);
        CustomProgressDialogBinding inflate2 = CustomProgressDialogBinding.inflate(getLayoutInflater());
        this.progressDialog.setContentView(inflate2.getRoot());
        this.progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

//        Resizer.getheightandwidth(this);
        int i = 1;

        int i2 = 0;
        int intExtra = getIntent().getIntExtra("Type", 0);
        this.passType = intExtra;
        this.type = intExtra == 0 ? TYPE.IMAGES : intExtra == 1 ? TYPE.VIDEOS : intExtra == 2 ? TYPE.AUDIO : intExtra == 3 ? TYPE.APP : intExtra == 4 ? TYPE.DOCUMENT : TYPE.ARCHIVE;
        int i3 = this.passType;
        if (i3 == 0) {
            i = 3;
        } else if (i3 == 1 || i3 == 2) {
            i = 2;
        }
        this.spanCount = i;
//        ImageView imageView = this.binding.header.filter;
//        if (this.passType != 4) {
//            i2 = 8;
//        }
//        imageView.setVisibility(i2);
//        this.binding.header.filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                openPopupDialog();
//            }
//        });
        resize();
        getData();
//        this.binding.header.back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });


        this.binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intExtra == 5) {
                    CompressedProcessActivity.compressedList.clear();
                    Iterator<DataModel> it = Common.arrayListSelected.iterator();
                    while (it.hasNext()) {
                        CompressedProcessActivity.compressedList.add(new File(it.next().getPath()));
                    }
                    extractDialog();

                } else {
                    CompressedProcessActivity.compressedList.clear();
                    Iterator<DataModel> it = Common.arrayListSelected.iterator();
                    while (it.hasNext()) {
                        CompressedProcessActivity.compressedList.add(new File(it.next().getPath()));
                    }
                    compressDialog();
                }
            }

        });
    }




    public void getData() {
        new GalleryAsyncTask(this.type, this, new OnProgressUpdate() {
            public void onTaskStart() {
                GalleryActivity.this.progressDialog.show();
                Common.arrayListSelected.clear();
                Common.arrayList.clear();
            }

            public void onComplete(ArrayList<DataModel> arrayList) {
                GalleryActivity.this.documentList.addAll(arrayList);
                Common.arrayList = arrayList;
                if (Common.arrayList.size() == 0) {
                    GalleryActivity.this.binding.msg.setVisibility(0);
                } else {
                    GalleryActivity.this.binding.msg.setVisibility(8);
                }
                GalleryActivity.this.galleryAdapter = new GalleryAdapter(GalleryActivity.this, Common.arrayList, GalleryActivity.this.passType == 2 ? 3 : GalleryActivity.this.passType, GalleryActivity.this);
                RecyclerView recyclerView = GalleryActivity.this.binding.recyclerView;
                GalleryActivity galleryActivity = GalleryActivity.this;
                recyclerView.setLayoutManager(new GridLayoutManager(galleryActivity, galleryActivity.passType == 2 ? 1 : GalleryActivity.this.spanCount));
                GalleryActivity.this.binding.recyclerView.setAdapter(GalleryActivity.this.galleryAdapter);
                GalleryActivity.this.progressDialog.dismiss();
            }
        }).execute(new Void[0]);
    }

    public void filterData(int i) {
        Common.arrayList.clear();
        if (i == 1) {
            Common.arrayList.addAll(this.documentList);
            this.galleryAdapter.notifyAdapter(this.documentList);
        } else if (i == 2) {
            Log.d(TAG, "filterData: " + this.documentList.size());
            Iterator<DataModel> it = this.documentList.iterator();
            while (it.hasNext()) {
                DataModel next = it.next();
                if (next.getName().endsWith(".pdf")) {
                    Common.arrayList.add(next);
                }
            }
            this.galleryAdapter.notifyAdapter(Common.arrayList);
        } else if (i == 3) {
            Iterator<DataModel> it2 = this.documentList.iterator();
            while (it2.hasNext()) {
                DataModel next2 = it2.next();
                if (next2.getName().endsWith(".xls")) {
                    Common.arrayList.add(next2);
                }
            }
            this.galleryAdapter.notifyAdapter(Common.arrayList);
        } else if (i == 4) {
            Iterator<DataModel> it3 = this.documentList.iterator();
            while (it3.hasNext()) {
                DataModel next3 = it3.next();
                if (next3.getName().endsWith(".docx") || next3.getName().endsWith(".doc")) {
                    Common.arrayList.add(next3);
                }
            }
            this.galleryAdapter.notifyAdapter(Common.arrayList);
        } else if (i == 5) {
            Iterator<DataModel> it4 = this.documentList.iterator();
            while (it4.hasNext()) {
                DataModel next4 = it4.next();
                if (next4.getName().endsWith(".pptx") || next4.getName().endsWith(".ppt")) {
                    Common.arrayList.add(next4);
                }
            }
            this.galleryAdapter.notifyAdapter(Common.arrayList);
        } else if (i == 6) {
            Iterator<DataModel> it5 = this.documentList.iterator();
            while (it5.hasNext()) {
                DataModel next5 = it5.next();
                if (next5.getName().endsWith(".txt")) {
                    Common.arrayList.add(next5);
                }
            }
            this.galleryAdapter.notifyAdapter(Common.arrayList);
        }
        if (Common.arrayList.size() == 0) {
            this.binding.msg.setVisibility(0);
        } else {
            this.binding.msg.setVisibility(8);
        }
        this.popupWindow.dismiss();
        this.popupWindow = null;
    }

    private void resize() {
        Resizer.getheightandwidth(this);
//        Resizer.setSize(this.binding.header.header, 1080, 182, true);
//        Resizer.setSize(this.binding.header.back, 60, 53, true);
//        Resizer.setSize(this.binding.header.filter, 74, 52, true);
        Resizer.setSize(this.binding.footer, 1030, 260, true);
        Resizer.setSize(this.binding.nofileLogo, 751, TYPE_ANIMATE_CIRCLEANGLE_TO, true);
//        Resizer.setMargin(this.binding.header.back, 50, 0, 0, 0);
//        Resizer.setMargin(this.binding.header.filter, 0, 0, 80, 0);
//        TextView textView = this.binding.header.title;
        int i = this.passType;

        binding.toolbar.setTitle(i == 0 ? "Images" : i == 1 ? "Videos" : i == 2 ? "Audios" : i == 3 ? "Apks" : i == 4 ? "Documents" : "Archive");
    }

    public void clickItem(int i, int i2) {
        if (i2 == 1) {
            Common.arrayList.get(i).setCheck(!Common.arrayList.get(i).isCheck());
            this.galleryAdapter.notifyAdapter(Common.arrayList);
            selected();

            Log.d("Selected Count", "Number of selected items: " + Common.arrayListSelected.size());
            try {
                if (Common.arrayListSelected.size() > 0) {
                    this.binding.footer.setVisibility(0);
                } else {
                    this.binding.footer.setVisibility(8);
                }
            } catch (Exception unused) {
                this.binding.footer.setVisibility(8);
            }

            String count = String.valueOf(Common.arrayListSelected.size());
            this.binding.countSelected.setText("Selected (" + count + ")");


        } else {
            StorageUtils.openAllFile(this, new File(Common.arrayList.get(i).getPath()).getAbsolutePath());
        }
    }

    public void selected() {
        Common.arrayListSelected.clear();
        Iterator<DataModel> it = Common.arrayList.iterator();
        while (it.hasNext()) {
            DataModel next = it.next();
            if (next.isCheck()) {
                Common.arrayListSelected.add(next);
            }
        }
    }

    public void compressDialog() {
        final CompressDialogLayoutBinding inflate = CompressDialogLayoutBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(inflate.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));


        inflate.passwordIcon.setBackgroundResource(R.drawable.effect_password);

        inflate.usePassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            inflate.edttxtPassword.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        inflate.passwordIcon.setOnClickListener(view -> {
            if (check) {
                inflate.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                inflate.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            check = !check;
        });

        inflate.zip.setOnClickListener(view -> handleFormatSelection("zip", inflate));
        inflate.tar.setOnClickListener(view -> handleFormatSelection("tar", inflate));
        inflate.sevenz.setOnClickListener(view -> handleFormatSelection("7z", inflate));

        inflate.name.setText(getResources().getString(R.string.save_name) + "_" +
                StorageUtils.getCurrentDateWithPatten("ddMMyyyyHHmmss"));

        inflate.compress.setOnClickListener(new CompressClick(this, inflate, dialog));

        inflate.cancel.setOnClickListener(view -> {
            dialog.dismiss();
            Toast.makeText(this, "Dialog dismissed", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private void handleFormatSelection(String format, CompressDialogLayoutBinding inflate) {
        Toast.makeText(this, "Selected format: " + format, Toast.LENGTH_SHORT).show();
    }


    public void extractDialog() {
        final ExtractDialogLayoutBinding inflate = ExtractDialogLayoutBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(inflate.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        inflate.msg.setVisibility(4);
        inflate.passwordIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!GalleryActivity.this.check) {
                    inflate.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    GalleryActivity.this.check = true;
                    return;
                }
                inflate.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                GalleryActivity.this.check = false;
            }
        });
        inflate.name.setText(Common.arrayListSelected.get(0).getName().replace(StorageUtils.getExtension(Common.arrayListSelected.get(0).getPath()), ""));
        inflate.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (!TextUtils.isEmpty(inflate.name.getText().toString())) {
                    Intent intent = new Intent(GalleryActivity.this, ExtractedProcessActivity.class);
                    intent.setFlags(536870912);
                    intent.putExtra("Path", Common.arrayListSelected.get(0).getPath());
                    intent.putExtra("Password", inflate.password.getText().toString());
                    intent.putExtra("Name", inflate.name.getText().toString());
                    startActivity(intent);
                    dialog.dismiss();
                    finish();
                    return;
                }
                inflate.name.setError("Please Enter Name");
            }
        });
        dialog.show();
    }

    public final class CompressClick implements View.OnClickListener {
        public GalleryActivity galleryActivity;
        public CompressDialogLayoutBinding compressDialogLayoutBinding;
        public Dialog dialog;

        public CompressClick(GalleryActivity galleryActivity, CompressDialogLayoutBinding compressDialogLayoutBinding, Dialog dialog) {
            this.galleryActivity = galleryActivity;
            this.compressDialogLayoutBinding = compressDialogLayoutBinding;
            this.dialog = dialog;
        }

        public final void onClick(View view) {
            String str = compressDialogLayoutBinding.zip.isChecked() ? ".zip" : compressDialogLayoutBinding.tar.isChecked() ? ".tar" : ".7z";
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
            if (!TextUtils.isEmpty(compressDialogLayoutBinding.name.getText().toString())) {
                Intent intent = new Intent(galleryActivity, CompressedProcessActivity.class);
                intent.setFlags(536870912);
                intent.putExtra("Password", compressDialogLayoutBinding.password.getText().toString());
                intent.putExtra("Name", compressDialogLayoutBinding.name.getText().toString());
                intent.putExtra("Extension", str);
                startActivity(intent);
                dialog.dismiss();
                finish();
                return;
            }
            compressDialogLayoutBinding.name.setError("Please Enter Name");
        }
    }

    public void onBackPressed() {
        HomeActivity.Home_Ads_Flag = 0;
        finish();
    }

    //    public void openPopupDialog() {
//        this.popupWindow = null;
//        View inflate = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.filter_layout, (ViewGroup) null);
//        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.mainLl);
//        ImageView imageView = (ImageView) inflate.findViewById(R.id.all);
//        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.pdf);
//        ImageView imageView3 = (ImageView) inflate.findViewById(R.id.excel);
//        ImageView imageView4 = (ImageView) inflate.findViewById(R.id.doc);
//        ImageView imageView5 = (ImageView) inflate.findViewById(R.id.ppt);
//        ImageView imageView6 = (ImageView) inflate.findViewById(R.id.txt);
//        PopupWindow popupWindow2 = new PopupWindow(inflate, 460, 775);
//        this.popupWindow = popupWindow2;
//        popupWindow2.setBackgroundDrawable(new ColorDrawable());
//        this.popupWindow.setOutsideTouchable(true);
//        this.popupWindow.setFocusable(true);
//        this.popupWindow.showAsDropDown(this.binding.header.filter, -300, 30);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filterData(1);
//            }
//        });
//
//        imageView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filterData(2);
//            }
//        });
//        imageView3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filterData(3);
//            }
//        });
//        imageView4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filterData(4);
//            }
//        });
//
//        imageView5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filterData(5);
//            }
//        });
//        imageView6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filterData(6);
//            }
//        });
//    }
}
