package com.files.zip.unzip.unrar.ultrapro.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.files.zip.unzip.unrar.ultrapro.R;
import com.files.zip.unzip.unrar.ultrapro.adapter.FolderAdapter;
import com.files.zip.unzip.unrar.ultrapro.databinding.ActivityFolderBinding;
import com.files.zip.unzip.unrar.ultrapro.databinding.CompressDialogLayoutBinding;
import com.files.zip.unzip.unrar.ultrapro.databinding.CustomProgressDialogBinding;
import com.files.zip.unzip.unrar.ultrapro.databinding.DeleteDialogLayoutBinding;
import com.files.zip.unzip.unrar.ultrapro.databinding.ExtractDialogLayoutBinding;
import com.files.zip.unzip.unrar.ultrapro.databinding.ExtractzipDialogBinding;
import com.files.zip.unzip.unrar.ultrapro.databinding.RenameDialogLayoutBinding;
import com.files.zip.unzip.unrar.ultrapro.interfaces.CommonInter;
import com.files.zip.unzip.unrar.ultrapro.model.DataModel;
import com.files.zip.unzip.unrar.ultrapro.utils.Common;
import com.files.zip.unzip.unrar.ultrapro.utils.Resizer;
import com.files.zip.unzip.unrar.ultrapro.utils.StorageUtils;
import com.files.zip.unzip.unrar.ultrapro.zip.IZipCallback;
import com.files.zip.unzip.unrar.ultrapro.zip.ZipManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class FolderActivity extends BaseActivity implements CommonInter {
    ActivityFolderBinding binding;
    boolean check = false;
    static String clickPath;
    FolderAdapter folderAdapter;
    String mainPath;
    Dialog progressDialog;
    int type;
    private long mLastClickTime = 0;

    public void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        ActivityFolderBinding inflate = ActivityFolderBinding.inflate(getLayoutInflater());
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
        int intExtra = getIntent().getIntExtra("Type", 0);
        this.type = intExtra;
        if (intExtra == 0) {
            str = Common.extractedPath;
        } else if (intExtra == 1) {
            str = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            str = intExtra == 2 ? Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() : Common.compressedPath;
        }
        this.mainPath = str;
        resize();
        clickPath = this.mainPath;

        binding.toolbar.setTitle(intExtra == 0 ? getResources().getString(R.string.extract) : intExtra == 1 ? getResources().getString(R.string.internal_storage) : intExtra == 2 ? getString(R.string.download) : getString(R.string.compress_files));


        new ProcessAsyncTask(this.clickPath).execute(new String[0]);

        this.binding.compressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompressedProcessActivity.compressedList.clear();
                Iterator<DataModel> it = Common.arrayListSelected.iterator();
                while (it.hasNext()) {
                    CompressedProcessActivity.compressedList.add(new File(it.next().getPath()));
                }
                compressDialog();
            }
        });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog();
            }
        });
    }


    class ProcessAsyncTask extends AsyncTask<String, Void, String> {
        ArrayList<File> list = new ArrayList<>();
        String path;

        public ProcessAsyncTask(String str) {
            this.path = str;
        }

        public void onPreExecute() {
            super.onPreExecute();
            FolderActivity.this.progressDialog.show();
            Common.arrayList.clear();
            Common.arrayListSelected.clear();
//            FolderActivity.this.binding.toolbar.setTitle(new File(this.path).getName().equals("0") ? getString(R.string.internal_storage) : new File(this.path).getName());


        }

        public String doInBackground(String[] strArr) {
            ArrayList<File> arrayList = StorageUtils.getfolderdata2(this.path);
            this.list = arrayList;
            Iterator<File> it = arrayList.iterator();
            while (it.hasNext()) {
                File next = it.next();
                if (next.exists()) {
                    Common.arrayList.add(new DataModel(next.getName(), next.getAbsolutePath(), StorageUtils.folderSize((double) (next.isDirectory() ? StorageUtils.dirSize(next) : next.length())), StorageUtils.fileDate(next.lastModified(), "dd/MM/yyyy - HH:mm"), false, false));
                }
            }
            return null;
        }

        public void onPostExecute(String str) {
            FolderActivity.this.progressDialog.dismiss();
            if (Common.arrayList.size() == 0) {
                FolderActivity.this.binding.llNoData.setVisibility(0);
            } else {
                FolderActivity.this.binding.llNoData.setVisibility(8);
            }
            FolderActivity.this.binding.recyclerView.setLayoutManager(new GridLayoutManager(FolderActivity.this, 1));
            FolderActivity.this.folderAdapter = new FolderAdapter(FolderActivity.this, Common.arrayList, FolderActivity.this);
            FolderActivity.this.binding.recyclerView.setAdapter(FolderActivity.this.folderAdapter);
        }
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

    public void clickItem(int i, int i2) {
        if (i2 == 0) {
            Common.arrayList.get(i).setCheck(!Common.arrayList.get(i).isCheck());
            this.folderAdapter.notifyAdapter(Common.arrayList);
            extractZipDialog(i);
            selected();

        } else if (i2 == 1) {
            Common.arrayList.get(i).setCheck(!Common.arrayList.get(i).isCheck());
            this.folderAdapter.notifyAdapter(Common.arrayList);
            selected();

            if (Common.arrayListSelected.size() > 0) {
                this.binding.footer.setVisibility(0);
            } else {
                this.binding.footer.setVisibility(8);
            }
            folderAdapter.notifyDataSetChanged();
        }
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
                if (!searchView.isIconified() && folderAdapter != null) {
                    folderAdapter.getFilter().filter(newText);
                    folderAdapter.notifyDataSetChanged();
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
                    folderAdapter.getFilter().filter("");
                    folderAdapter.notifyDataSetChanged();
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
        PopupWindow popupWindow = new PopupWindow(FolderActivity.this);
        View customView = LayoutInflater.from(FolderActivity.this).inflate(R.layout.filter_menu, null);
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
                    return item1.getTime().compareTo(item2.getTime());  // Sorting by time (date)
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
        folderAdapter.updateListnew(filteredList);
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

    public void extractZipDialog(int pos) {
        final ExtractzipDialogBinding inflate = ExtractzipDialogBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(FolderActivity.this);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(inflate.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        String strname = Common.arrayList.get(pos).getName();
        inflate.fileName.setText(strname);

        inflate.fileRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str;
                if (Common.arrayListSelected.size() == 1) {
                    if (new File(Common.arrayListSelected.get(0).getPath()).isDirectory()) {
                        str = Common.arrayListSelected.get(0).getName();
                    } else {
                        str = Common.arrayListSelected.get(0).getName().replace(StorageUtils.getExtension(Common.arrayListSelected.get(0).getPath()), "");
                    }
                    renameDialog(str);
                    dialog.dismiss();
                    return;
                }
                Toast.makeText(FolderActivity.this, getResources().getString(R.string.please_select_only_one), 0).show();

            }
        });
        inflate.fileExractto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.arrayListSelected.size() != 1) {
                    Toast.makeText(FolderActivity.this, getResources().getString(R.string.please_select_only_one), 0).show();
                } else if (Common.checkIsArchive(Common.arrayListSelected.get(0).getPath())) {
                    showExtractDialogIfSecured();
                } else {
                    Toast.makeText(FolderActivity.this, getResources().getString(R.string.this_is_not_archive_file), 0).show();
                }
                dialog.dismiss();
            }
        });

        inflate.fileExtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.arrayListSelected.size() != 1) {
                    Toast.makeText(FolderActivity.this, getResources().getString(R.string.please_select_only_one), 0).show();
                } else if (Common.checkIsArchive(Common.arrayListSelected.get(0).getPath())) {
                    extractHereDialog();
                } else {
                    Toast.makeText(FolderActivity.this, getResources().getString(R.string.this_is_not_archive_file), 0).show();
                }
                dialog.dismiss();
            }
        });

        inflate.fileDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog();
                dialog.dismiss();
            }
        });


        inflate.fileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Common.arrayList.get(pos).getPath());
                if (file.isDirectory()) {
                    clickPath = file.getAbsolutePath();
                    new ProcessAsyncTask(clickPath).execute(new String[0]);
                    dialog.dismiss();
                    return;
                }
                StorageUtils.openAllFile(FolderActivity.this, file.getAbsolutePath());
                dialog.dismiss();
            }

        });

        inflate.fileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND_MULTIPLE");
                intent.putExtra("android.intent.extra.SUBJECT", "Share Data");
                intent.setType("*/*");
                ArrayList arrayList = new ArrayList();
                Iterator<DataModel> it = Common.arrayListSelected.iterator();
                while (it.hasNext()) {
                    File file = new File(it.next().getPath());
                    arrayList.add(FileProvider.getUriForFile(FolderActivity.this, getApplicationContext().getPackageName() + ".fileProvider", file));
                }
                intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        try {
            dialog.show();
        } catch (Exception e) {
            dialog.dismiss();
        }

    }

    public void renameDialog(String str) {
        RenameDialogLayoutBinding inflate = RenameDialogLayoutBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(FolderActivity.this);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(inflate.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        inflate.name.setText(str);
        inflate.rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
                String obj = inflate.name.getText().toString();
                if (obj.equals(str)) {
                    Toast.makeText(FolderActivity.this, "Please Change Name", 0).show();
                } else if (!TextUtils.isEmpty(obj)) {
                    if (StorageUtils.renameFile(FolderActivity.this, Common.arrayListSelected.get(0).getPath(), obj)) {
                        Common.arrayListSelected.clear();
                        new ProcessAsyncTask(clickPath).execute(new String[0]);
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(FolderActivity.this, getResources().getString(R.string.please_enter_name), 0).show();
                }
            }
        });
        dialog.show();
    }

    Dialog dialog;

    public void compressDialog() {
        final CompressDialogLayoutBinding inflate = CompressDialogLayoutBinding.inflate(getLayoutInflater());
         dialog = new Dialog(this);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(inflate.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        inflate.usePassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            inflate.edttxtPassword.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        inflate.passwordIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!FolderActivity.this.check) {
                    inflate.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    FolderActivity.this.check = true;
                    return;
                }
                inflate.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                FolderActivity.this.check = false;
            }
        });
        inflate.zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflate.zip.setChecked(true);
                inflate.tar.setChecked(false);
                inflate.sevenz.setChecked(false);
            }
        });
        inflate.tar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflate.zip.setChecked(false);
                inflate.tar.setChecked(true);
                inflate.sevenz.setChecked(false);
            }
        });
        inflate.sevenz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflate.zip.setChecked(false);
                inflate.tar.setChecked(false);
                inflate.sevenz.setChecked(true);
            }
        });
        EditText editText = inflate.name;
        editText.setText(getResources().getString(R.string.save_name) + "_" + StorageUtils.getCurrentDateWithPatten("ddMMyyyyHHmmss"));
        inflate.compress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = inflate.zip.isChecked() ? ".zip" : inflate.tar.isChecked() ? ".tar" : ".7z";
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (!TextUtils.isEmpty(inflate.name.getText().toString())) {
                    Intent intent = new Intent(FolderActivity.this, CompressedProcessActivity.class);
                    intent.setFlags(536870912);
                    intent.putExtra("Password", inflate.password.getText().toString());
                    intent.putExtra("Name", inflate.name.getText().toString());
                    intent.putExtra("Extension", str);
                    dialog.dismiss();
                    startActivity(intent);
                    finish();
                    return;
                }
                inflate.name.setError(getResources().getString(R.string.please_enter_name));
            }
        });

        inflate.cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
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
                    Intent intent = new Intent(FolderActivity.this, ExtractedProcessActivity.class);
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


    public void showExtractDialogIfSecured() {
        if (Common.arrayListSelected.get(0).isPasswordProtected()) {
            extractDialog();
        } else {
            Intent intent = new Intent(this, ExtractedProcessActivity.class);
            intent.putExtra("Path", Common.arrayListSelected.get(0).getPath());
            intent.putExtra("Name", Common.arrayListSelected.get(0).getName());
            startActivity(intent);
            finish();
        }
    }

    private void resize() {
        Resizer.getheightandwidth(this);
    }

    @SuppressLint("SetTextI18n")
    public void extractHereDialog() {
        final ExtractDialogLayoutBinding inflate = ExtractDialogLayoutBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(inflate.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        inflate.msg.setVisibility(View.GONE);

        inflate.passwordIcon.setOnClickListener(view -> {
            if (!FolderActivity.this.check) {
                inflate.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                FolderActivity.this.check = true;
            } else {
                inflate.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                FolderActivity.this.check = false;
            }
        });

        String baseFolderName = Common.arrayListSelected.get(0).getName().replace(
                StorageUtils.getExtension(Common.arrayListSelected.get(0).getPath()), ""
        );

        String sourcePath = Common.arrayListSelected.get(0).getPath();
        File sourceFile = new File(sourcePath);
        File parentDir = sourceFile.getParentFile();


        String folderName = baseFolderName;
        File outputDir = new File(parentDir, folderName);
        int counter = 1;
        File finalOutputDir1 = outputDir;

        while (outputDir.exists()) {
            folderName = baseFolderName + "(" + counter + ")";
            outputDir = new File(parentDir, folderName);
            counter++;
        }

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        inflate.name.setText(folderName);

        String finalFolderName = folderName;
        File finalOutputDir = outputDir;

        if (!ZipManager.checkFileIsProtected(sourcePath)) {
            extractFile(sourcePath, finalOutputDir.getAbsolutePath(), null, new IZipCallback() {

                @Override
                public void onStartIZip() {
                    runOnUiThread(() -> Toast.makeText(FolderActivity.this, "Extraction started", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onProgress(int progress) {
                    runOnUiThread(() -> {
                    });
                }

                @Override
                public void onFinish(boolean success) {
                    runOnUiThread(() -> {
                        if (success) {
                            Toast.makeText(FolderActivity.this, "Extraction completed successfully!", Toast.LENGTH_SHORT).show();

                            File extractedFolder = new File(finalOutputDir1.getAbsolutePath());
                            File[] extractedFiles = extractedFolder.listFiles();

                            if (extractedFiles != null) {
                                Log.d("Extraction", "Files Count: " + extractedFiles.length);

                                ArrayList<DataModel> extractedDataModels = new ArrayList<>();

                                for (File file : extractedFiles) {
                                    String fileName = file.getName();
                                    String filePath = file.getAbsolutePath();
                                    String fileSize = getFileSize(file);
                                    String fileTime = getFileModificationTime(file);
                                    boolean isChecked = false;
                                    boolean isPasswordProtected = false;

                                    DataModel dataModel = new DataModel(fileName, filePath, fileSize, fileTime, isChecked, isPasswordProtected);
                                    extractedDataModels.add(dataModel);
                                }

                                folderAdapter.notifyAdapter(extractedDataModels);
                                folderAdapter.notifyDataSetChanged();

                                scanFileForVisibility(extractedFolder);
                            } else {
                                Log.d("Extraction", "No files found in the extracted directory.");
                            }
                        } else {
                            Toast.makeText(FolderActivity.this, "Failed to extract the file.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            return;
        }

        inflate.yes.setOnClickListener(view -> {
            dialog.dismiss();

            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);

            String password = inflate.password.getText().toString();

            if (TextUtils.isEmpty(finalFolderName)) {
                inflate.name.setError(getResources().getString(R.string.please_enter_name));
                return;
            }
            if (ZipManager.checkFileIsProtected(sourcePath)) {
                if (TextUtils.isEmpty(password)) {
                    inflate.msg.setText(R.string.file_is_protected_please_enter_a_password);
                    inflate.msg.setVisibility(View.VISIBLE);
                    return;
                }

                boolean isPasswordValid = ZipManager.checkFilePassword(sourcePath, password);
                Log.d("Password Validation", "Password valid: " + isPasswordValid); // Debugging
                if (!isPasswordValid) {
                    inflate.msg.setText(R.string.wrong_password_please_re_enter);
                    inflate.msg.setVisibility(View.VISIBLE);
                    return;
                }
            }

            extractFile(sourcePath, finalOutputDir.getAbsolutePath(), password, new IZipCallback() {

                @Override
                public void onStartIZip() {
                    runOnUiThread(() -> Toast.makeText(FolderActivity.this, R.string.extraction_started, Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onProgress(int progress) {
                    runOnUiThread(() -> {
                    });
                }

                @Override
                public void onFinish(boolean success) {
                    runOnUiThread(() -> {
                        if (success) {
                            Toast.makeText(FolderActivity.this, "Extraction completed successfully!", Toast.LENGTH_SHORT).show();

                            File extractedFolder = new File(finalOutputDir1.getAbsolutePath());
                            File[] extractedFiles = extractedFolder.listFiles();

                            if (extractedFiles != null) {
                                Log.d("Extraction", "Files Count: " + extractedFiles.length);

                                ArrayList<DataModel> extractedDataModels = new ArrayList<>();

                                for (File file : extractedFiles) {
                                    String fileName = file.getName();
                                    String filePath = file.getAbsolutePath();
                                    String fileSize = getFileSize(file);
                                    String fileTime = getFileModificationTime(file);
                                    boolean isChecked = false;
                                    boolean isPasswordProtected = false;

                                    DataModel dataModel = new DataModel(fileName, filePath, fileSize, fileTime, isChecked, isPasswordProtected);
                                    extractedDataModels.add(dataModel);
                                }


                                folderAdapter.notifyAdapter(extractedDataModels);
                                folderAdapter.notifyDataSetChanged();


                                scanFileForVisibility(extractedFolder);
                            } else {
                                Log.d("Extraction", "No files found in the extracted directory.");
                            }
                        } else {
                            Toast.makeText(FolderActivity.this, "Failed to extract the file.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        });

        dialog.show();
    }


    private void extractFile(String sourcePath, String outputPath, String password, IZipCallback callback) {
        if (callback != null) callback.onStartIZip();
        ZipManager.unzip(sourcePath, outputPath, password, callback);
    }

    public void scanFileForVisibility(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);  // Trigger a scan of the new file
    }

    private String getFileSize(File file) {
        long sizeInBytes = file.length();
        if (sizeInBytes < 1024) {
            return sizeInBytes + " bytes";
        } else if (sizeInBytes < 1048576) {
            return sizeInBytes / 1024 + " KB";
        } else {
            return sizeInBytes / 1048576 + " MB";
        }
    }


    private String getFileModificationTime(File file) {
        long timeInMillis = file.lastModified();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date(timeInMillis));
    }

    public void deleteDialog() {
        DeleteDialogLayoutBinding inflate = DeleteDialogLayoutBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(inflate.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        inflate.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteProcessActivity.deleteList.clear();
                Iterator<DataModel> it = Common.arrayListSelected.iterator();
                while (it.hasNext()) {
                    DeleteProcessActivity.deleteList.add(new File(it.next().getPath()));
                }
                Intent intent = new Intent(FolderActivity.this, DeleteProcessActivity.class);
                intent.setFlags(536870912);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    public void onBackPressed() {
        if (this.mainPath.equals(this.clickPath)) {
            HomeActivity.Home_Ads_Flag = 0;
            finish();
            return;
        }
        Common.arrayListSelected.clear();
        this.binding.footer.setVisibility(8);
        this.clickPath = new File(this.clickPath).getParent();
        new ProcessAsyncTask(this.clickPath).execute(new String[0]);
    }
}
