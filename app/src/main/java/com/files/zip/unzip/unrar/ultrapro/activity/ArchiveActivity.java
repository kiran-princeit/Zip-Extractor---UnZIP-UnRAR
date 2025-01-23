package com.files.zip.unzip.unrar.ultrapro.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.files.zip.unzip.unrar.ultrapro.R;
import com.files.zip.unzip.unrar.ultrapro.adapter.ArchiveNewAdapter;
import com.files.zip.unzip.unrar.ultrapro.adapter.GalleryAdapter;
import com.files.zip.unzip.unrar.ultrapro.adsprosimple.MobileAds;
import com.files.zip.unzip.unrar.ultrapro.databinding.ActivityArchiveBinding;
import com.files.zip.unzip.unrar.ultrapro.databinding.CustomProgressDialogBinding;
import com.files.zip.unzip.unrar.ultrapro.databinding.ExtractDialogLayoutBinding;
import com.files.zip.unzip.unrar.ultrapro.enums.TYPE;
import com.files.zip.unzip.unrar.ultrapro.interfaces.CommonInter;
import com.files.zip.unzip.unrar.ultrapro.interfaces.OnProgressUpdate;
import com.files.zip.unzip.unrar.ultrapro.model.DataModel;
import com.files.zip.unzip.unrar.ultrapro.utils.Common;
import com.files.zip.unzip.unrar.ultrapro.utils.GalleryAsyncTask;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class ArchiveActivity extends AppCompatActivity implements CommonInter {
    ActivityArchiveBinding binding;
    ArrayList<DataModel> documentList = new ArrayList<>();
    ArchiveNewAdapter archiveNewAdapter;
    int passType;
    Dialog progressDialog;
    int spanCount = 3;
    TYPE type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityArchiveBinding inflate = ActivityArchiveBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        MobileAds.showBanner(binding.adContainerBanner, binding.shimmerContainerBanner, ArchiveActivity.this);
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

        int i = 1;
        int intExtra = getIntent().getIntExtra("Type", 0);
        this.passType = intExtra;
        this.type = TYPE.ARCHIVE;
        int i3 = this.passType;
        if (i3 == 0) {
            i = 3;
        } else if (i3 == 1 || i3 == 2) {
            i = 2;
        }
        this.spanCount = i;
        resize();
        getData();


        this.binding.extarcted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompressedProcessActivity.compressedList.clear();
                Iterator<DataModel> it = Common.arrayListSelected.iterator();
                while (it.hasNext()) {
                    com.files.zip.unzip.unrar.ultrapro.activity.CompressedProcessActivity.compressedList.add(new File(it.next().getPath()));
                }
                showExtractDialogIfSecured();

            }
        });
    }

    public void getData() {
        new GalleryAsyncTask(this.type, this, new OnProgressUpdate() {
            public void onTaskStart() {
                ArchiveActivity.this.progressDialog.show();
                Common.arrayListSelected.clear();
                Common.arrayList.clear();
            }

            public void onComplete(ArrayList<DataModel> arrayList) {
                ArchiveActivity.this.documentList.addAll(arrayList);
                Common.arrayList = arrayList;
                if (Common.arrayList.size() == 0) {
                    ArchiveActivity.this.binding.llNoData.setVisibility(0);
                } else {
                    ArchiveActivity.this.binding.llNoData.setVisibility(8);
                }
                ArchiveActivity.this.archiveNewAdapter = new ArchiveNewAdapter(ArchiveActivity.this, Common.arrayList, ArchiveActivity.this.passType == 2 ? 3 : ArchiveActivity.this.passType, ArchiveActivity.this);
                RecyclerView recyclerView = ArchiveActivity.this.binding.recyclerView;
                ArchiveActivity ArchiveActivity = ArchiveActivity.this;
                recyclerView.setLayoutManager(new GridLayoutManager(ArchiveActivity, ArchiveActivity.passType == 2 ? 1 : ArchiveActivity.this.spanCount));
                ArchiveActivity.this.binding.recyclerView.setAdapter(ArchiveActivity.this.archiveNewAdapter);
                ArchiveActivity.this.progressDialog.dismiss();
            }
        }).execute(new Void[0]);
//        new GalleryAsyncTask(this.type, this, new OnProgressUpdate() {
//            @Override
//            public void onTaskStart() {
//                progressDialog.show();
//                Common.arrayListSelected.clear();
//                Common.arrayList.clear();
//            }
//
//            @Override
//            public void onComplete(ArrayList<DataModel> batch) {
//                if (batch != null && !batch.isEmpty()) {
//                    Common.arrayList.addAll(batch);
//
//                    if (archiveNewAdapter == null) {
//                        archiveNewAdapter = new ArchiveNewAdapter(
//                                ArchiveActivity.this,
//                                Common.arrayList,
//                                passType == 2 ? 3 : passType,
//                                ArchiveActivity.this
//                        );
//                        binding.recyclerView.setLayoutManager(new GridLayoutManager(
//                                ArchiveActivity.this,
//                                passType == 2 ? 1 : spanCount
//                        ));
//                        binding.recyclerView.setAdapter(archiveNewAdapter);
//                    } else {
//                        archiveNewAdapter.notifyDataSetChanged(); // Update RecyclerView
//                    }
//                }
//
//                if (Common.arrayList.isEmpty()) {
//                    binding.llNoData.setVisibility(View.VISIBLE);
//                } else {
//                    binding.llNoData.setVisibility(View.GONE);
//                }
//                progressDialog.dismiss();
//            }
//        }).execute();

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
                if (!searchView.isIconified() && archiveNewAdapter != null) {
                    archiveNewAdapter.getFilter().filter(newText);
                    archiveNewAdapter.notifyDataSetChanged();
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
                    archiveNewAdapter.getFilter().filter("");
                    archiveNewAdapter.notifyDataSetChanged();
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
        PopupWindow popupWindow = new PopupWindow(ArchiveActivity.this);
        View customView = LayoutInflater.from(ArchiveActivity.this).inflate(R.layout.filter_menu, null);
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
                    return item1.getName().compareToIgnoreCase(item2.getName());
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

        archiveNewAdapter.notifyAdapter(filteredList);
    }

    private void resize() {
        int i = this.passType;

        binding.toolbar.setTitle(getResources().getString(R.string.archive));
    }

    public void updateFooterVisibility() {
        boolean isAnyChecked = false;
        for (DataModel item : Common.arrayList) {
            if (item.isCheck()) {
                isAnyChecked = true;
                break;
            }
        }
        if (isAnyChecked) {
            this.binding.footer.setVisibility(View.VISIBLE);
        } else {
            this.binding.footer.setVisibility(View.GONE);
        }
    }

//    public void clickItem(int position, int actionType) {
//        if (actionType == 1) { // Checkbox and footer should be visible
//            Common.arrayList.get(position).setCheck(true);
//            this.archiveNewAdapter.notifyAdapter(Common.arrayList);
//            selected();
//            try {
//                if (Common.arrayListSelected.size() > 0) {
//                    this.binding.footer.setVisibility(View.VISIBLE);
//                } else {
//                    this.binding.footer.setVisibility(View.GONE);
//                }
//            } catch (Exception unused) {
//                this.binding.footer.setVisibility(View.GONE);
//            }
//        } else {
//            Common.arrayList.get(position).setCheck(false);
//            this.archiveNewAdapter.notifyAdapter(Common.arrayList);
//
//            this.binding.footer.setVisibility(View.GONE);
//        }
//    }


    public void clickItem(int i, int i2) {
        if (i2 == 0) {
            Common.arrayList.get(i).setCheck(!Common.arrayList.get(i).isCheck());
            this.archiveNewAdapter.notifyAdapter(Common.arrayList);
//            extractZipDialog(i);
            selected();

        } else if (i2 == 1) {
            Common.arrayList.get(i).setCheck(!Common.arrayList.get(i).isCheck());
            this.archiveNewAdapter.notifyAdapter(Common.arrayList);
            selected();

            if (Common.arrayListSelected.size() > 0) {
                this.binding.footer.setVisibility(0);
            } else {
                this.binding.footer.setVisibility(8);
            }
            archiveNewAdapter.notifyDataSetChanged();
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


        inflate.txtName.setText(Common.arrayListSelected.get(0).getName());
        inflate.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
                MobileAds.showInterstitial(ArchiveActivity.this, () -> {
                    if (!TextUtils.isEmpty(inflate.txtName.getText().toString())) {
                        Intent intent = new Intent(ArchiveActivity.this, ExtractedProcessActivity.class);
                        intent.setFlags(536870912);
                        intent.putExtra("Path", Common.arrayListSelected.get(0).getPath());
                        intent.putExtra("Password", inflate.password.getText().toString());
                        intent.putExtra("Name", inflate.name.getText().toString());
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                        return;
                    }
                });
            }
        });
        dialog.show();
    }


    public void showExtractDialogIfSecured() {
        if (Common.arrayListSelected.get(0).isPasswordProtected()) {
            extractDialog();
        } else {
//            Toast.makeText(this, "finish", Toast.LENGTH_SHORT).show();
            MobileAds.showInterstitial(ArchiveActivity.this, () -> {
                Intent intent = new Intent(this, ExtractedProcessActivity.class);
                intent.putExtra("Path", Common.arrayListSelected.get(0).getPath());
                intent.putExtra("Name", Common.arrayListSelected.get(0).getName());
                startActivity(intent);
                finish();
            });
        }
    }

    public void onBackPressed() {
        HomeActivity.Home_Ads_Flag = 0;
        finish();
        Common.arrayListSelected.clear();
        this.binding.footer.setVisibility(8);
    }
}