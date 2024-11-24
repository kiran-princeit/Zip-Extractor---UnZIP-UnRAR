package com.raktatech.winzip.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.raktatech.winzip.R;
import com.raktatech.winzip.adapter.FolderAdapter;

import com.raktatech.winzip.databinding.ActivityFolderBinding;
import com.raktatech.winzip.databinding.CompressDialogLayoutBinding;
import com.raktatech.winzip.databinding.CustomProgressDialogBinding;
import com.raktatech.winzip.databinding.DeleteDialogLayoutBinding;
import com.raktatech.winzip.databinding.ExtractDialogLayoutBinding;
import com.raktatech.winzip.databinding.ExtractzipDialogBinding;
import com.raktatech.winzip.databinding.RenameDialogLayoutBinding;
import com.raktatech.winzip.interfaces.CommonInter;
import com.raktatech.winzip.model.DataModel;
import com.raktatech.winzip.utils.Common;

import com.raktatech.winzip.utils.Resizer;
import com.raktatech.winzip.utils.StorageUtils;
import com.raktatech.winzip.zip.IZipCallback;
import com.raktatech.winzip.zip.ZipManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class FolderActivity extends AppCompatActivity implements CommonInter {
    ActivityFolderBinding binding;
    boolean check = false;
    static String clickPath;
    FolderAdapter folderAdapter;
    String mainPath;
    Dialog progressDialog;
    int type;

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
        this.clickPath = this.mainPath;
        Log.d("TAG", "onCreate:Data  " + this.type + " -- " + this.clickPath);
        new ProcessAsyncTask(this.clickPath).execute(new String[0]);

        this.binding.header.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


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

        inflate.delete.setOnClickListener(new View.OnClickListener() {
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
            FolderActivity.this.binding.header.title.setText(new File(this.path).getName().equals("0") ? "Internal Storage" : new File(this.path).getName());
        }

        public String doInBackground(String[] strArr) {
            ArrayList<File> arrayList = StorageUtils.getfolderdata2(this.path);
            this.list = arrayList;
            Iterator<File> it = arrayList.iterator();
            while (it.hasNext()) {
                File next = it.next();
                if (next.exists()) {
                    Common.arrayList.add(new DataModel(next.getName(), next.getAbsolutePath(), StorageUtils.folderSize((double) (next.isDirectory() ? StorageUtils.dirSize(next) : next.length())), StorageUtils.fileDate(next.lastModified(), "dd/MM/yyyy - HH:mm"), false));
                }
            }
            return null;
        }

        public void onPostExecute(String str) {
            FolderActivity.this.progressDialog.dismiss();
            if (Common.arrayList.size() == 0) {
                FolderActivity.this.binding.msg.setVisibility(0);
            } else {
                FolderActivity.this.binding.msg.setVisibility(8);
            }
            FolderActivity.this.binding.recyclerView.setLayoutManager(new GridLayoutManager(FolderActivity.this, 1));
            FolderActivity.this.folderAdapter = new FolderAdapter(FolderActivity.this, Common.arrayList, FolderActivity.this);
            FolderActivity.this.binding.recyclerView.setAdapter(FolderActivity.this.folderAdapter);
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
                Toast.makeText(FolderActivity.this, "Please Select only one item", 0).show();

            }
        });
        inflate.fileExractto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.arrayListSelected.size() != 1) {
                    Toast.makeText(FolderActivity.this, "Please Select only one item", 0).show();
                } else if (Common.checkIsArchive(Common.arrayListSelected.get(0).getPath())) {
                    extractDialog();
                } else {
                    Toast.makeText(FolderActivity.this, "This is not Archive file", 0).show();
                }
                dialog.dismiss();
            }
        });

        inflate.fileExtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.arrayListSelected.size() != 1) {
                    Toast.makeText(FolderActivity.this, "Please Select only one item", 0).show();
                } else if (Common.checkIsArchive(Common.arrayListSelected.get(0).getPath())) {
                    extractHereDialog();
                } else {
                    Toast.makeText(FolderActivity.this, "This is not Archive file", 0).show();
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

//    public void clickItem(int i, int i2) {
//        // Get the clicked item's path
//        String filePath = Common.arrayList.get(i).getPath();
//        File file = new File(filePath);
//
//        if (file.isFile() && (filePath.endsWith(".zip") || filePath.endsWith(".tar") || filePath.endsWith(".7z"))) {
//            Common.arrayList.get(i).setCheck(!Common.arrayList.get(i).isCheck());
//            extractZipDialog(i);
//            this.folderAdapter.notifyAdapter(Common.arrayList);
//            selected();
//
//        } else {
//            if (file.isDirectory()) {
//                this.clickPath = file.getAbsolutePath();
//                new ProcessAsyncTask(this.clickPath).execute();
//            } else {
//                StorageUtils.openAllFile(this, file.getAbsolutePath());
//            }
//        }
//    }


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
                    Toast.makeText(FolderActivity.this, "Please Enter Name", 0).show();
                }
            }
        });
        dialog.show();
    }


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
                    startActivity(intent);
                    dialog.dismiss();
                    finish();
                    return;
                }
                inflate.name.setError("Please Enter Name");
            }
        });

        inflate.cancel.setOnClickListener(view -> {
            dialog.dismiss();
            Toast.makeText(this, "Dialog dismissed", Toast.LENGTH_SHORT).show();
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
                inflate.name.setError("Please Enter Name");
            }
        });
        dialog.show();
    }

    private void resize() {
        Resizer.getheightandwidth(this);
        Resizer.setSize(this.binding.nofileLogo, 751, 606, true);
        Resizer.setSize(this.binding.header.header, 1080, 154, true);
//        Resizer.setSize(this.binding.delete, 110, 110, true);
//        Resizer.setSize(this.binding.rename, 110, 110, true);
//        Resizer.setSize(this.binding.compressed, 110, 110, true);
//        Resizer.setSize(this.binding.extracted, 110, 110, true);
//        Resizer.setSize(this.binding.share, 110, 110, true);
        Resizer.setMargin(this.binding.header.back, 30, 0, 0, 0);
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

//    public void extractHereDialog() {
//        final ExtractDialogLayoutBinding inflate = ExtractDialogLayoutBinding.inflate(getLayoutInflater());
//        Dialog dialog = new Dialog(this);
//        dialog.getWindow().requestFeature(1);
//        dialog.setContentView(inflate.getRoot());
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        inflate.msg.setVisibility(View.GONE);
//
//        // Toggle password visibility
//        inflate.passwordIcon.setOnClickListener(view -> {
//            if (!FolderActivity.this.check) {
//                inflate.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                FolderActivity.this.check = true;
//            } else {
//                inflate.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                FolderActivity.this.check = false;
//            }
//        });
//
//
//        String baseFolderName = Common.arrayListSelected.get(0).getName().replace(
//                StorageUtils.getExtension(Common.arrayListSelected.get(0).getPath()), ""
//        );
//
//        // Output path
//        String sourcePath = Common.arrayListSelected.get(0).getPath();
//        File sourceFile = new File(sourcePath);
//        File parentDir = sourceFile.getParentFile();
//
//        // Find a unique folder name by appending (1), (2), etc.
//        String folderName = baseFolderName;
//        File outputDir = new File(parentDir, folderName);
//        int counter = 1;
//
//        while (outputDir.exists()) {
//            folderName = baseFolderName + "(" + counter + ")";
//            outputDir = new File(parentDir, folderName);
//            counter++;
//        }
//
//        // Now outputDir is unique
//        if (!outputDir.exists()) {
//            outputDir.mkdirs();  // Create the unique folder
//        }
//
//        inflate.name.setText(folderName);
//
//        String finalFolderName = folderName;
//        File finalOutputDir = outputDir;
//        File finalOutputDir1 = outputDir;
//        inflate.yes.setOnClickListener(view -> {
//            dialog.dismiss();
//
//            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
//
//            String password = inflate.password.getText().toString();
//
//            if (TextUtils.isEmpty(finalFolderName)) {
//                inflate.name.setError("Please enter a name");
//                return;
//            }
//
//            // Check if file is protected
//            if (ZipManager.checkFileIsProtected(sourcePath)) {
//                if (TextUtils.isEmpty(password)) {
//                    inflate.msg.setText("File is protected. Please enter a password.");
//                    inflate.msg.setVisibility(View.VISIBLE);
//                    return;
//                }
//
//                // Verify password
//                if (!ZipManager.checkFilePassword(sourcePath, password)) {
//                    inflate.msg.setText("Wrong password. Please re-enter.");
//                    inflate.msg.setVisibility(View.VISIBLE);
//                    return;
//                }
//            }
//
//            // Start extraction
//            extractFile(sourcePath, finalOutputDir.getAbsolutePath(), password, new IZipCallback() {
//
//                @Override
//                public void onStartIZip() {
//                    runOnUiThread(() -> Toast.makeText(FolderActivity.this, "Extraction started", Toast.LENGTH_SHORT).show());
//                }
//
//                @Override
//                public void onProgress(int progress) {
//                    runOnUiThread(() -> {
//                        // Optionally show progress
////                        inflate.progressBar.setProgress(progress);
//                    });
//                }
//
//                @Override
//                public void onFinish(boolean success) {
//                    runOnUiThread(() -> {
//                        if (success) {
//                            Toast.makeText(FolderActivity.this, "Extraction completed successfully!", Toast.LENGTH_SHORT).show();
//
//                            // Extract files from the output directory
//                            File extractedFolder = new File(finalOutputDir1.getAbsolutePath());
//                            File[] extractedFiles = extractedFolder.listFiles();
//                            if (extractedFiles != null) {
//                                Log.d("Extraction", "Files Count: " + extractedFiles.length);
//                            } else {
//                                Log.d("Extraction", "No files found in the extracted directory.");
//                            }
//
//                            if (extractedFiles != null) {
//                                ArrayList<DataModel> extractedDataModels = new ArrayList<>();
//
//                                // Loop through the extracted files and create DataModel objects
//                                for (File file : extractedFiles) {
//                                    String fileName = file.getName();
//                                    String filePath = file.getAbsolutePath();
//                                    String fileSize = getFileSize(file);
//                                    String fileTime = getFileModificationTime(file);
//                                    boolean isChecked = false; // Initially unchecked
//
//                                    // Create DataModel for each file
//                                    DataModel dataModel = new DataModel(fileName, filePath, fileSize, fileTime, isChecked);
//                                    extractedDataModels.add(dataModel);
//                                }
//
//                                // Add the new data to the adapter
//                                folderAdapter.notifyAdapter(extractedDataModels);  // Add new files to the existing list
//                                scanFileForVisibility(extractedFolder);
//                                Log.d("Extracted Files", "Files count: " + extractedFiles.length);
//
//                            }
//                        } else {
//                            Toast.makeText(FolderActivity.this, "Failed to extract the file.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }
//
//            });
//        });
//
//        dialog.show();
//    }


    public void extractHereDialog() {
        final ExtractDialogLayoutBinding inflate = ExtractDialogLayoutBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(inflate.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        inflate.msg.setVisibility(View.GONE);

        // Toggle password visibility
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

        // Output path
        String sourcePath = Common.arrayListSelected.get(0).getPath();
        File sourceFile = new File(sourcePath);
        File parentDir = sourceFile.getParentFile();


        // Find a unique folder name by appending (1), (2), etc.
        String folderName = baseFolderName;
        File outputDir = new File(parentDir, folderName);
        int counter = 1;
        File finalOutputDir1 = outputDir;

        while (outputDir.exists()) {
            folderName = baseFolderName + "(" + counter + ")";
            outputDir = new File(parentDir, folderName);
            counter++;
        }

        // Now outputDir is unique
        if (!outputDir.exists()) {
            outputDir.mkdirs(); // Create the unique folder
        }

        inflate.name.setText(folderName);

        String finalFolderName = folderName;
        File finalOutputDir = outputDir;

        // Check if the file is protected
        if (!ZipManager.checkFileIsProtected(sourcePath)) {
            // File is not password-protected, extract directly
            extractFile(sourcePath, finalOutputDir.getAbsolutePath(), null, new IZipCallback() {

                @Override
                public void onStartIZip() {
                    runOnUiThread(() -> Toast.makeText(FolderActivity.this, "Extraction started", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onProgress(int progress) {
                    runOnUiThread(() -> {
                        // Optionally show progress
                        // inflate.progressBar.setProgress(progress);
                    });
                }

                @Override
                public void onFinish(boolean success) {
                    runOnUiThread(() -> {
                        if (success) {
                            Toast.makeText(FolderActivity.this, "Extraction completed successfully!", Toast.LENGTH_SHORT).show();

                            // Extract files from the output directory
                            File extractedFolder = new File(finalOutputDir1.getAbsolutePath());
                            File[] extractedFiles = extractedFolder.listFiles();

                            if (extractedFiles != null) {
                                Log.d("Extraction", "Files Count: " + extractedFiles.length);

                                // Update the adapter with new data
                                ArrayList<DataModel> extractedDataModels = new ArrayList<>();

                                for (File file : extractedFiles) {
                                    String fileName = file.getName();
                                    String filePath = file.getAbsolutePath();
                                    String fileSize = getFileSize(file);
                                    String fileTime = getFileModificationTime(file);
                                    boolean isChecked = false; // Initially unchecked

                                    // Create DataModel for each file
                                    DataModel dataModel = new DataModel(fileName, filePath, fileSize, fileTime, isChecked);
                                    extractedDataModels.add(dataModel);
                                }

                                // Notify the adapter with the new files
                                folderAdapter.notifyAdapter(extractedDataModels); // Add new files to the existing list
                                folderAdapter.notifyDataSetChanged(); // Refresh RecyclerView UI

                                // Ensure extracted files are scanned for visibility
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

            return; // Skip showing the dialog for password
        }

        // If the file is protected, show the dialog
        inflate.yes.setOnClickListener(view -> {
            dialog.dismiss();

            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);

            String password = inflate.password.getText().toString();

            if (TextUtils.isEmpty(finalFolderName)) {
                inflate.name.setError("Please enter a name");
                return;
            }
//
//            if (TextUtils.isEmpty(password)) {
//                inflate.msg.setText("File is protected. Please enter a password.");
//                inflate.msg.setVisibility(View.VISIBLE);
//                return;
//            }
//
//            // Verify password
//            if (!ZipManager.checkFilePassword(sourcePath, password)) {
//                inflate.msg.setText("Wrong password. Please re-enter.");
//                inflate.msg.setVisibility(View.VISIBLE);
//                return;
//            }

            if (ZipManager.checkFileIsProtected(sourcePath)) {
                if (TextUtils.isEmpty(password)) {
                    inflate.msg.setText("File is protected. Please enter a password.");
                    inflate.msg.setVisibility(View.VISIBLE);
                    return;
                }

                // Verify password
                boolean isPasswordValid = ZipManager.checkFilePassword(sourcePath, password);
                Log.d("Password Validation", "Password valid: " + isPasswordValid); // Debugging
                if (!isPasswordValid) {
                    inflate.msg.setText("Wrong password. Please re-enter.");
                    inflate.msg.setVisibility(View.VISIBLE);
                    return;
                }
            }

            // Start extraction with password
            extractFile(sourcePath, finalOutputDir.getAbsolutePath(), password, new IZipCallback() {

                @Override
                public void onStartIZip() {
                    runOnUiThread(() -> Toast.makeText(FolderActivity.this, "Extraction started", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onProgress(int progress) {
                    runOnUiThread(() -> {
                        // Optionally show progress
                    });
                }

                @Override
                public void onFinish(boolean success) {
                    runOnUiThread(() -> {
                        if (success) {
                            Toast.makeText(FolderActivity.this, "Extraction completed successfully!", Toast.LENGTH_SHORT).show();

                            // Extract files from the output directory
                            File extractedFolder = new File(finalOutputDir1.getAbsolutePath());
                            File[] extractedFiles = extractedFolder.listFiles();

                            if (extractedFiles != null) {
                                Log.d("Extraction", "Files Count: " + extractedFiles.length);

                                // Update the adapter with new data
                                ArrayList<DataModel> extractedDataModels = new ArrayList<>();

                                for (File file : extractedFiles) {
                                    String fileName = file.getName();
                                    String filePath = file.getAbsolutePath();
                                    String fileSize = getFileSize(file);
                                    String fileTime = getFileModificationTime(file);
                                    boolean isChecked = false; // Initially unchecked

                                    // Create DataModel for each file
                                    DataModel dataModel = new DataModel(fileName, filePath, fileSize, fileTime, isChecked);
                                    extractedDataModels.add(dataModel);
                                }

                                // Notify the adapter with the new files
                                folderAdapter.notifyAdapter(extractedDataModels); // Add new files to the existing list
                                folderAdapter.notifyDataSetChanged(); // Refresh RecyclerView UI

                                // Ensure extracted files are scanned for visibility
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


    /*
     * Report
     * create dialog extract
     * - check if password match or not
     * if password provided then show password enter password
     * get Archive File and this file archived extract,delete,share
     *
     *
     * */
}
