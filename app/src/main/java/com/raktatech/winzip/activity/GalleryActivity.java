package com.raktatech.winzip.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
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
import androidx.appcompat.widget.SearchView;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class GalleryActivity extends BaseActivity implements CommonInter {


    public static final int TYPE_INT = 900;
    private static final String TAG = "GalleryActivity";
    ActivityGalleryBinding binding;
    public boolean check = false;
    ArrayList<DataModel> documentList = new ArrayList<>();
    GalleryAdapter galleryAdapter;
    int passType;
    Dialog progressDialog;
    int spanCount = 3;
    TYPE type;
    private long mLastClickTime = 0;


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
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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
        this.type = intExtra == 0 ? TYPE.IMAGES : intExtra == 1 ? TYPE.VIDEOS : intExtra == 2 ? TYPE.AUDIO : intExtra == 3 ? TYPE.APP : TYPE.DOCUMENT;
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

//                if (intExtra == 5) {
//                    CompressedProcessActivity.compressedList.clear();
//                    Iterator<DataModel> it = Common.arrayListSelected.iterator();
//                    while (it.hasNext()) {
//                        CompressedProcessActivity.compressedList.add(new File(it.next().getPath()));
//                    }
//                    extractDialog();

//                } else {
                CompressedProcessActivity.compressedList.clear();
                Iterator<DataModel> it = Common.arrayListSelected.iterator();
                while (it.hasNext()) {
                    CompressedProcessActivity.compressedList.add(new File(it.next().getPath()));
                }
                compressDialog();
            }
//            }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_filter, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        MenuItem filterItem = menu.findItem(R.id.actionFilter);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchView.SearchAutoComplete searchAutoComplete =
                searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextSize(16);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.grey));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.black));

        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!searchView.isIconified() && galleryAdapter != null) {
                    galleryAdapter.getFilter().filter(newText);
                    galleryAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        ImageView closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        if (closeButton != null) {
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery("", false);
                    galleryAdapter.getFilter().filter("");
                    galleryAdapter.notifyDataSetChanged();
                }
            });
        }
        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                openFilterMenu();
                return true;
            }
        });
        return true;
    }

    private void openFilterMenu() {
        PopupWindow popupWindow = new PopupWindow(GalleryActivity.this);
        View customView = LayoutInflater.from(GalleryActivity.this).inflate(R.layout.filter_menu, null);
        popupWindow.setContentView(customView);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout llDate = customView.findViewById(R.id.llDate);
        LinearLayout llName = customView.findViewById(R.id.llName);
        LinearLayout llSize = customView.findViewById(R.id.llSize);

        llDate.setOnClickListener(view -> {
            applyFilters(true, false, false);
            popupWindow.dismiss();
        });

        llName.setOnClickListener(view -> {
            applyFilters(false, true, false);
            popupWindow.dismiss();
        });

        llSize.setOnClickListener(view -> {
            applyFilters(false, false, true);
            popupWindow.dismiss();
        });

        popupWindow.showAsDropDown(findViewById(R.id.actionFilter));
    }

    private void applyFilters(boolean filterByDate, boolean filterByName, boolean filterBySize) {
        ArrayList<DataModel> filteredList = new ArrayList<>(Common.arrayList);

        if (filterByDate) {
            Collections.sort(filteredList, new Comparator<DataModel>() {
                @Override
                public int compare(DataModel item1, DataModel item2) {
                    return item1.getTime().compareTo(item2.getTime());
                }
            });
        }

        if (filterByName) {
            Collections.sort(filteredList, new Comparator<DataModel>() {
                @Override
                public int compare(DataModel item1, DataModel item2) {
                    return item1.getName().compareTo(item2.getName());
                }
            });
        }


        if (filterBySize) {
            Collections.sort(filteredList, new Comparator<DataModel>() {
                @Override
                public int compare(DataModel lhs, DataModel rhs) {
                    return lhs.getSize().compareTo(rhs.getSize());
                }
            });
        }

        galleryAdapter.notifyAdapter(filteredList);
    }

    private void resize() {
        int i = this.passType;

        binding.toolbar.setTitle(i == 0 ? "Images" : i == 1 ? "Videos" : i == 2 ? "Audios" : i == 3 ? "Apks" : "Documents");
    }

    public void clickItem(int i, int i2) {
        if (i2 == 1) {
            Common.arrayList.get(i).setCheck(!Common.arrayList.get(i).isCheck());
            this.galleryAdapter.notifyAdapter(Common.arrayList);
            selected();

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
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

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


    boolean isPasswordVisible = false;

    public void compressDialog() {
        final CompressDialogLayoutBinding inflate = CompressDialogLayoutBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(inflate.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        inflate.usePassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            inflate.edttxtPassword.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        inflate.passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    inflate.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    inflate.passwordIcon.setImageResource(R.drawable.show_password_press);
                    isPasswordVisible = false;
                } else {
                    inflate.password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    inflate.passwordIcon.setImageResource(R.drawable.show_password);
                    isPasswordVisible = true;
                }
                inflate.password.setSelection(inflate.password.getText().length());
            }
        });

        inflate.passwordIcon.setOnClickListener(view -> {
            if (check) {
                inflate.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                inflate.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            check = !check;
        });

        inflate.name.setText(getResources().getString(R.string.save_name) + "_" +
                StorageUtils.getCurrentDateWithPatten("ddMMyyyyHHmmss"));

        inflate.compress.setOnClickListener(new CompressClick(this, inflate, dialog));

        inflate.cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    public void extractDialog() {
        final ExtractDialogLayoutBinding inflate = ExtractDialogLayoutBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(inflate.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        inflate.msg.setVisibility(4);

        inflate.passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    inflate.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    inflate.passwordIcon.setImageResource(R.drawable.show_password_press); // Update icon
                    isPasswordVisible = false;
                } else {
                    inflate.password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    inflate.passwordIcon.setImageResource(R.drawable.show_password); // Update icon
                    isPasswordVisible = true;
                }
                inflate.password.setSelection(inflate.password.getText().length());
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
                inflate.name.setError(getResources().getString(R.string.please_enter_name));
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
            compressDialogLayoutBinding.name.setError(getResources().getString(R.string.please_enter_name));
        }
    }

    public void onBackPressed() {
        HomeActivity.Home_Ads_Flag = 0;
        finish();
    }
}
