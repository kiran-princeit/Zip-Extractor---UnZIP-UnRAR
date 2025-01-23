package com.files.zip.unzip.unrar.ultrapro.utils;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

public class StorageUtils {
    public static String TAG = "STORAGE_TAG";

    public static String create_folder(String str) {
        if (Build.VERSION.SDK_INT <= 29) {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + str);
            String str2 = TAG;
            StringBuilder sb = new StringBuilder("createDir:");
            sb.append(file);
            Log.d(str2, sb.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        File file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "/" + str)));
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder("createDir:");
        sb2.append(file2);
        Log.d(str3, sb2.toString());
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return file2.getAbsolutePath();
    }

    public static String create_hidden_folder(String str) {
        if (Build.VERSION.SDK_INT <= 29) {
            File file = new File(Environment.getExternalStorageDirectory() + "/." + str);
            String str2 = TAG;
            StringBuilder sb = new StringBuilder("createDir:");
            sb.append(file);
            Log.d(str2, sb.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        File file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "/." + str)));
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder("createDir:");
        sb2.append(file2);
        Log.d(str3, sb2.toString());
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return file2.getAbsolutePath();
    }

    public static String create_folder_with_sub_folder(String str, String str2) {
        if (Build.VERSION.SDK_INT <= 29) {
            File file = new File(Environment.getExternalStorageDirectory() +  "/" + str +  "/" + str2);
            String str3 = TAG;
            StringBuilder sb = new StringBuilder("createDir:");
            sb.append(file);
            Log.d(str3, sb.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        File file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory( File.separator + str + File.separator + str2)));
//        File file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + str + File.separator + str2)));
        String str4 = TAG;
        StringBuilder sb2 = new StringBuilder("createDir:");
        sb2.append(file2);
        Log.d(str4, sb2.toString());
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return file2.getAbsolutePath();
    }

    public static void saveTextContent(String str, String str2) {
        String str3 = TAG;
        Log.i(str3, "Saving text : " + str2);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            fileOutputStream.write(str.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            String str4 = TAG;

        }
    }

    public static void scanFile(Context context, String str) {
        MediaScannerConnection.scanFile(context, new String[]{str}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {

            }
        });
    }

    public static String saveimage(Context context, Bitmap bitmap, String str, String str2) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, str2);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            String absolutePath = file2.getAbsolutePath();
            MediaScannerConnection.scanFile(context, new String[]{file2.getPath()}, (String[]) null, (MediaScannerConnection.OnScanCompletedListener) null);
            return absolutePath;
        } catch (Exception e) {
            String str3 = TAG;
            Log.d(str3, "saveimage: " + e);
            Toast(context, "Failed to Save");
            return null;
        }
    }



    public static void Toast(Context context, String str) {
        Toast(context, str, 0);
    }

    public static void Toast(Context context, String str, int i) {
        Toast.makeText(context, str, i).show();
    }

    public static ArrayList<String> getfolderdata(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (!new File(str).exists()) {
            return null;
        }
        File file = new File(str);
        Log.d(TAG, "onResume: " + file);
        File[] listFiles = file.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return null;
        }
        for (File absolutePath : listFiles) {
            arrayList.add(absolutePath.getAbsolutePath());
        }
        return arrayList;
    }

    public static ArrayList<File> getfolderdata2(String str) {
        ArrayList<File> arrayList = new ArrayList<>();
        if (new File(str).exists()) {
            File file = new File(str);
            Log.d(TAG, "onResume: " + file);
            File[] listFiles = file.listFiles();
            if (!(listFiles == null || listFiles.length == 0)) {
                for (File add : listFiles) {
                    arrayList.add(add);
                }
            }
        }
        return arrayList;
    }

    public static void shareAllFile(Context context, File file) {
        Uri uriForFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileProvider", file);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setFlags(268435456);
        intent.addFlags(1);
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.setType("*/*");
        context.startActivity(Intent.createChooser(intent, "Share with"));
    }

    public static void openAllFile(Context context, String str) {
        String mimeType = getMimeType(str);
        if (!new File(str).getName().endsWith(".apk")) {
            try {
                Uri uriForFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileProvider", new File(str));
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addFlags(1);
                intent.setDataAndType(uriForFile, mimeType);
                context.startActivity(intent);
            } catch (Exception e) {
                String str2 = TAG;
                Log.d(str2, "openAllFile: " + e.getMessage());
                Toast.makeText(context, "File not exist", 1).show();
            }
        } else if (new File(str).exists()) {
            Intent intent2 = new Intent("android.intent.action.VIEW");
            intent2.setDataAndType(uriFromFile(context, new File(str)), "application/vnd.android.package-archive");
            intent2.addFlags(268435456);
            intent2.addFlags(1);
            try {
                context.startActivity(intent2);
            } catch (ActivityNotFoundException e2) {
                e2.printStackTrace();
            }
        } else {
            Toast.makeText(context, "File not exist", 1).show();
        }
    }

    public static String getMimeType(String str) {
        String[] split = str.split("\\.");
        String str2 = split[split.length - 1];
        if (str2 != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(str2);
        }
        return null;
    }

    public static Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT < 24) {
            return Uri.fromFile(file);
        }
        return FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
    }

    public static void shareWebLink(Context context, String str, String str2) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", str2);
        intent.putExtra("android.intent.extra.TITLE", str);
        intent.setFlags(1);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, "Share Weblink"));
    }

    public static Bitmap getBitmapFromFile(String str) {
        File file = new File(str);
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return null;
    }

    public static boolean deletefile(Context context, String str) {
        File file = new File(str);
        if (!file.exists() || !file.delete()) {
            return false;
        }
        MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, (String[]) null, (MediaScannerConnection.OnScanCompletedListener) null);
        return true;
    }



    public static ArrayList<String> getasset_folder_data(Context context, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.clear();
        try {
            for (String add : context.getAssets().list(str)) {
                arrayList.add(add);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static void share_app(Context context) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + context.getPackageName());
        context.startActivity(Intent.createChooser(intent, "Share via"));
    }

    public static void rate_app(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (ActivityNotFoundException unused) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static String getTimeString(long j, String str) {
        return new SimpleDateFormat(str).format(Long.valueOf(j));
    }

    public static void shareImage(Context context, Uri uri) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addFlags(524288);
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.STREAM", uri);
        context.startActivity(Intent.createChooser(intent, "Share Image Using"));
    }

    public static String create_folder_in_app_package_dir(Context context, String str) {
        File file = new File(context.getFilesDir(), str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String timeConversion(long j) {
        return String.format("%02d:%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(j) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(j))), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(j))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j)))});
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd-MMM-yyyy-HH-mm-ss-SSS", Locale.getDefault()).format(Calendar.getInstance().getTime()).replace(" ", "");
    }

    public static String getCurrentDateWithPatten(String str) {
        return new SimpleDateFormat(str, Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public static long dirSize(File file) {
        File[] listFiles;
        long j;
        long j2 = 0;
        if (file.exists() && (listFiles = file.listFiles()) != null) {
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isDirectory()) {
                    j = dirSize(listFiles[i]);
                } else {
                    j = listFiles[i].length();
                }
                j2 += j;
            }
        }
        return j2;
    }

    public static String folderSize(double sizeInBytes) {
        if (sizeInBytes < 1024) {
            return new DecimalFormat("##.##").format(sizeInBytes) + " B";
        } else if (sizeInBytes < 1024 * 1024) {
            return new DecimalFormat("##.##").format(sizeInBytes / 1024) + " KB";
        } else if (sizeInBytes < 1024 * 1024 * 1024) {
            return new DecimalFormat("##.##").format(sizeInBytes / (1024 * 1024)) + " MB";
        } else {
            return new DecimalFormat("##.##").format(sizeInBytes / (1024 * 1024 * 1024)) + " GB";
        }
    }


//    public static String folderSize(double d) {
//        if (d < 1024.0d) {
//            return new DecimalFormat("##.##").format(d) + " B";
//        } else if (d >= 1024.0d && d < 1048576.0d) {
//            return new DecimalFormat("##.##").format(d / 1024.0d) + " KB";
//        } else if (d < 1048576.0d || d >= 1.073741824E9d) {
//            return new DecimalFormat("##.##").format(((d / 1024.0d) / 1024.0d) / 1024.0d) + " GB";
//        } else {
//            return new DecimalFormat("##.##").format((d / 1024.0d) / 1024.0d) + " MB";
//        }
//    }

    public static String fileDate(long j, String str) {
        return new SimpleDateFormat(str).format(Long.valueOf(j));
    }

    public static String copyFile(Context context, String str, String str2) {
        File file = new File(str);
        File file2 = new File(str2 + ( "/" + file.getName()));
        try {
            FileUtils.copyFile(file, file2);
        } catch (IOException e) {
            Log.e(TAG, "copyFile: " + e);
        }
        return file2.toString();
    }

    public static boolean renameFile(Context context, String str, String str2) {
        File file;
        scanFile(context, str);
        File file2 = new File(str);
        if (file2.isDirectory()) {
            file = new File(file2.getParentFile(), str2);
        } else {
            File parentFile = file2.getParentFile();
            file = new File(parentFile, str2 + getExtension(file2.getAbsolutePath()));
        }
        if (file2.renameTo(file)) {
            scanFile(context, file.getAbsolutePath());
            Toast.makeText(context, "Rename Successfully", 0).show();
            return true;
        }
        Toast.makeText(context, "Something is wrong Please try again", 0).show();
        return false;
    }

    public static String getExtension(String str) {
        return str.substring(str.lastIndexOf("."));
    }

    public static Intent setAsOption(Context context, File file) {
        Intent intent = new Intent("android.intent.action.ATTACH_DATA");
        if (file.exists()) {
            intent.setDataAndType(FileProvider.getUriForFile(context, "com.intuit.sdp.fileProvider", file), "image/jpg");
            intent.putExtra("mimeType", "image/jpg");
            intent.addFlags(1);
        } else {
            Toast.makeText(context, "Not a wallpaper", 0).show();
        }
        return intent;
    }

    public static boolean isVideoFile(String str) {
        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(str));
        return mimeTypeFromExtension != null && mimeTypeFromExtension.startsWith("video/");
    }

    public static boolean isImageFile(String str) {
        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(str));
        return mimeTypeFromExtension != null && mimeTypeFromExtension.startsWith("image/");
    }

    public static boolean isAudioFile(String str) {
        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(str));
        return mimeTypeFromExtension != null && mimeTypeFromExtension.startsWith("audio/");
    }
}
