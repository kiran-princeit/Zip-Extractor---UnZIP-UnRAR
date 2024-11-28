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

    public static void downloadvideo(Context context, String str, String str2, String str3) {
        new DownloadFileFromURL(context, str2, str3).execute(new String[]{str});
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

    private static class DownloadFileFromURL extends AsyncTask<String, Integer, String> {
        Context context;
        String nameoffile;
        String pathFile = "";
        String pathFolder;
        ProgressDialog pd;

        public DownloadFileFromURL(Context context2, String str, String str2) {
            this.context = context2;
            this.pathFolder = str;
            this.nameoffile = str2;
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            r6.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
            r8.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x00b6, code lost:
            if (r6 == null) goto L_0x00bd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x00b8, code lost:
            r6.close();
         */
        /* JADX WARNING: Removed duplicated region for block: B:101:0x015f  */
        /* JADX WARNING: Removed duplicated region for block: B:83:0x013e A[SYNTHETIC, Splitter:B:83:0x013e] */
        /* JADX WARNING: Removed duplicated region for block: B:88:0x0146 A[Catch:{ IOException -> 0x0142 }] */
        /* JADX WARNING: Removed duplicated region for block: B:90:0x014b  */
        /* JADX WARNING: Removed duplicated region for block: B:94:0x0152 A[SYNTHETIC, Splitter:B:94:0x0152] */
        /* JADX WARNING: Removed duplicated region for block: B:99:0x015a A[Catch:{ IOException -> 0x0156 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public String doInBackground(String... r17) {
            /*
                r16 = this;
                r1 = r16
                java.lang.String r0 = "doInBackground: "
                java.lang.String r2 = "doInBackground: connectioncode "
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r4 = r1.pathFolder
                r3.append(r4)
                java.lang.String r4 = "/"
                r3.append(r4)
                java.lang.String r4 = r1.nameoffile
                r3.append(r4)
                java.lang.String r3 = r3.toString()
                r1.pathFile = r3
                android.content.Context r4 = r1.context
                r5 = 1
                java.lang.String[] r6 = new java.lang.String[r5]
                r7 = 0
                r6[r7] = r3
                r3 = 0
                android.media.MediaScannerConnection.scanFile(r4, r6, r3, r3)
                java.io.File r4 = new java.io.File
                java.lang.String r6 = r1.pathFolder
                r4.<init>(r6)
                boolean r6 = r4.exists()
                if (r6 != 0) goto L_0x003c
                r4.mkdirs()
            L_0x003c:
                java.net.URL r4 = new java.net.URL     // Catch:{ Exception -> 0x0134, all -> 0x012f }
                r6 = r17[r7]     // Catch:{ Exception -> 0x0134, all -> 0x012f }
                r4.<init>(r6)     // Catch:{ Exception -> 0x0134, all -> 0x012f }
                java.net.URLConnection r4 = r4.openConnection()     // Catch:{ Exception -> 0x0134, all -> 0x012f }
                java.net.HttpURLConnection r4 = (java.net.HttpURLConnection) r4     // Catch:{ Exception -> 0x0134, all -> 0x012f }
                r4.connect()     // Catch:{ Exception -> 0x0127, all -> 0x011f }
                java.lang.String r6 = com.raktatech.winzip.utils.StorageUtils.TAG     // Catch:{ Exception -> 0x0127, all -> 0x011f }
                java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0127, all -> 0x011f }
                r8.<init>(r2)     // Catch:{ Exception -> 0x0127, all -> 0x011f }
                int r2 = r4.getResponseCode()     // Catch:{ Exception -> 0x0127, all -> 0x011f }
                r8.append(r2)     // Catch:{ Exception -> 0x0127, all -> 0x011f }
                java.lang.String r2 = r8.toString()     // Catch:{ Exception -> 0x0127, all -> 0x011f }
                android.util.Log.d(r6, r2)     // Catch:{ Exception -> 0x0127, all -> 0x011f }
                int r2 = r4.getResponseCode()     // Catch:{ Exception -> 0x0127, all -> 0x011f }
                r6 = 200(0xc8, float:2.8E-43)
                if (r2 == r6) goto L_0x007b
                java.lang.String r0 = "Please Enter Valid Name"
                if (r4 == 0) goto L_0x0070
                r4.disconnect()
            L_0x0070:
                return r0
            L_0x0071:
                r0 = move-exception
                r6 = r3
                r15 = r4
                goto L_0x0150
            L_0x0076:
                r0 = move-exception
                r6 = r3
                r15 = r4
                goto L_0x0138
            L_0x007b:
                int r2 = r4.getContentLength()     // Catch:{ Exception -> 0x0127, all -> 0x011f }
                java.io.InputStream r6 = r4.getInputStream()     // Catch:{ Exception -> 0x0127, all -> 0x011f }
                java.lang.String r8 = com.raktatech.winzip.utils.StorageUtils.TAG     // Catch:{ Exception -> 0x0118, all -> 0x0111 }
                java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0118, all -> 0x0111 }
                r9.<init>(r0)     // Catch:{ Exception -> 0x0118, all -> 0x0111 }
                java.lang.String r0 = r1.pathFile     // Catch:{ Exception -> 0x0118, all -> 0x0111 }
                r9.append(r0)     // Catch:{ Exception -> 0x0118, all -> 0x0111 }
                java.lang.String r0 = r9.toString()     // Catch:{ Exception -> 0x0118, all -> 0x0111 }
                android.util.Log.e(r8, r0)     // Catch:{ Exception -> 0x0118, all -> 0x0111 }
                java.io.FileOutputStream r8 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0118, all -> 0x0111 }
                java.lang.String r0 = r1.pathFile     // Catch:{ Exception -> 0x0118, all -> 0x0111 }
                r8.<init>(r0)     // Catch:{ Exception -> 0x0118, all -> 0x0111 }
                r0 = 4096(0x1000, float:5.74E-42)
                byte[] r0 = new byte[r0]     // Catch:{ Exception -> 0x010a, all -> 0x0103 }
                r9 = 0
            L_0x00a3:
                int r11 = r6.read(r0)     // Catch:{ Exception -> 0x010a, all -> 0x0103 }
                r12 = -1
                if (r11 == r12) goto L_0x00ef
                boolean r12 = r16.isCancelled()     // Catch:{ Exception -> 0x010a, all -> 0x0103 }
                if (r12 == 0) goto L_0x00c9
                r6.close()     // Catch:{ Exception -> 0x00c6, all -> 0x00c3 }
                r8.close()     // Catch:{ IOException -> 0x00bc }
                if (r6 == 0) goto L_0x00bd
                r6.close()     // Catch:{ IOException -> 0x00bc }
                goto L_0x00bd
            L_0x00bc:
            L_0x00bd:
                if (r4 == 0) goto L_0x00c2
                r4.disconnect()
            L_0x00c2:
                return r3
            L_0x00c3:
                r0 = move-exception
                r15 = r4
                goto L_0x0108
            L_0x00c6:
                r0 = move-exception
                r15 = r4
                goto L_0x010f
            L_0x00c9:
                long r12 = (long) r11
                long r9 = r9 + r12
                if (r2 <= 0) goto L_0x00e2
                java.lang.Integer[] r12 = new java.lang.Integer[r5]     // Catch:{ Exception -> 0x010a, all -> 0x0103 }
                r13 = 100
                long r13 = r13 * r9
                r17 = r4
                long r3 = (long) r2
                long r13 = r13 / r3
                int r3 = (int) r13     // Catch:{ Exception -> 0x00ed, all -> 0x00eb }
                java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Exception -> 0x00ed, all -> 0x00eb }
                r12[r7] = r3     // Catch:{ Exception -> 0x00ed, all -> 0x00eb }
                r1.publishProgress(r12)     // Catch:{ Exception -> 0x00ed, all -> 0x00eb }
                goto L_0x00e4
            L_0x00e2:
                r17 = r4
            L_0x00e4:
                r8.write(r0, r7, r11)     // Catch:{ Exception -> 0x00ed, all -> 0x00eb }
                r4 = r17
                r3 = 0
                goto L_0x00a3
            L_0x00eb:
                r0 = move-exception
                goto L_0x0106
            L_0x00ed:
                r0 = move-exception
                goto L_0x010d
            L_0x00ef:
                r17 = r4
                r8.close()     // Catch:{ IOException -> 0x00fa }
                if (r6 == 0) goto L_0x00fb
                r6.close()     // Catch:{ IOException -> 0x00fa }
                goto L_0x00fb
            L_0x00fa:
            L_0x00fb:
                if (r17 == 0) goto L_0x0100
                r17.disconnect()
            L_0x0100:
                java.lang.String r0 = r1.pathFile
                return r0
            L_0x0103:
                r0 = move-exception
                r17 = r4
            L_0x0106:
                r15 = r17
            L_0x0108:
                r3 = r8
                goto L_0x0150
            L_0x010a:
                r0 = move-exception
                r17 = r4
            L_0x010d:
                r15 = r17
            L_0x010f:
                r3 = r8
                goto L_0x0138
            L_0x0111:
                r0 = move-exception
                r17 = r4
                r15 = r17
                r3 = 0
                goto L_0x0150
            L_0x0118:
                r0 = move-exception
                r17 = r4
                r15 = r17
                r3 = 0
                goto L_0x0138
            L_0x011f:
                r0 = move-exception
                r17 = r4
                r15 = r17
                r3 = 0
                r6 = 0
                goto L_0x0150
            L_0x0127:
                r0 = move-exception
                r17 = r4
                r15 = r17
                r3 = 0
                r6 = 0
                goto L_0x0138
            L_0x012f:
                r0 = move-exception
                r3 = 0
                r6 = 0
                r15 = 0
                goto L_0x0150
            L_0x0134:
                r0 = move-exception
                r3 = 0
                r6 = 0
                r15 = 0
            L_0x0138:
                java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x014f }
                if (r3 == 0) goto L_0x0144
                r3.close()     // Catch:{ IOException -> 0x0142 }
                goto L_0x0144
            L_0x0142:
                goto L_0x0149
            L_0x0144:
                if (r6 == 0) goto L_0x0149
                r6.close()     // Catch:{ IOException -> 0x0142 }
            L_0x0149:
                if (r15 == 0) goto L_0x014e
                r15.disconnect()
            L_0x014e:
                return r0
            L_0x014f:
                r0 = move-exception
            L_0x0150:
                if (r3 == 0) goto L_0x0158
                r3.close()     // Catch:{ IOException -> 0x0156 }
                goto L_0x0158
            L_0x0156:
                goto L_0x015d
            L_0x0158:
                if (r6 == 0) goto L_0x015d
                r6.close()     // Catch:{ IOException -> 0x0156 }
            L_0x015d:
                if (r15 == 0) goto L_0x0162
                r15.disconnect()
            L_0x0162:
                goto L_0x0164
            L_0x0163:
                throw r0
            L_0x0164:
                goto L_0x0163
            */
            throw new UnsupportedOperationException("Method not decompiled: com.raktatech.winzip.utils.StorageUtils.DownloadFileFromURL.doInBackground(java.lang.String[]):java.lang.String");
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            ProgressDialog progressDialog = new ProgressDialog(this.context);
            this.pd = progressDialog;
            progressDialog.setTitle("Processing...");
            this.pd.setMessage("Please wait.");
            this.pd.setMax(100);
            this.pd.setProgressStyle(1);
            this.pd.setCancelable(false);
            this.pd.show();
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
            this.pd.setProgress(Integer.parseInt(String.valueOf(numArr[0])));
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            ProgressDialog progressDialog = this.pd;
            if (progressDialog != null) {
                progressDialog.dismiss();
                if (str != null) {
                    MediaScannerConnection.scanFile(this.context, new String[]{str}, (String[]) null, (MediaScannerConnection.OnScanCompletedListener) null);
                }
                Context context2 = this.context;
                Toast.makeText(context2, "Video Downloaded" + str, 0).show();
            }
        }
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
