package com.files.zip.unzip.unrar.ultrapro.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import androidx.core.os.EnvironmentCompat;
import com.files.zip.unzip.unrar.ultrapro.model.DataModel;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class Common {
    private static final String TAG = "Common";
    public static ArrayList<DataModel> arrayList = new ArrayList<>();
    public static ArrayList<DataModel> arrayListSelected = new ArrayList<>();
    public static String compressedPath;
    public static String extractedPath;

    public static String compressedFilePassword = null;

    public static String[] getExternalStorageDirectories(Context context) {
        boolean z;
        ArrayList arrayList2 = new ArrayList();
        File[] externalFilesDirs = context.getExternalFilesDirs((String) null);
        String lowerCase = Environment.getExternalStorageDirectory().getAbsolutePath().toLowerCase();
        for (File file : externalFilesDirs) {
            if (file != null) {
                String str = file.getPath().split("/Android")[0];
                if (!str.toLowerCase().startsWith(lowerCase)) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        z = Environment.isExternalStorageRemovable(file);
                    } else {
                        z = "mounted".equals(EnvironmentCompat.getStorageState(file));
                    }
                    if (z) {
                        arrayList2.add(str);
                    }
                }
            }
        }
        if (arrayList2.isEmpty()) {
            String str2 = "";
            try {
                Process start = new ProcessBuilder(new String[0]).command(new String[]{"mount | grep /dev/block/vold"}).redirectErrorStream(true).start();
                start.waitFor();
                InputStream inputStream = start.getInputStream();
                byte[] bArr = new byte[1024];
                while (inputStream.read(bArr) != -1) {
                    str2 = str2 + new String(bArr);
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!str2.trim().isEmpty()) {
                for (String split : str2.split("\n")) {
                    arrayList2.add(split.split(" ")[2]);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int i = 0;
            while (i < arrayList2.size()) {
                if (!((String) arrayList2.get(i)).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
                    Log.d("LOG_TAG", ((String) arrayList2.get(i)) + " might not be extSDcard");
                    arrayList2.remove(i);
                    i += -1;
                }
                i++;
            }
        } else {
            int i2 = 0;
            while (i2 < arrayList2.size()) {
                if (!((String) arrayList2.get(i2)).toLowerCase().contains("ext") && !((String) arrayList2.get(i2)).toLowerCase().contains("sdcard")) {
                    Log.d("LOG_TAG", ((String) arrayList2.get(i2)) + " might not be extSDcard");
                    arrayList2.remove(i2);
                    i2 += -1;
                }
                i2++;
            }
        }
        String[] strArr = new String[arrayList2.size()];
        Log.d(TAG, "getExternalStorageDirectories: " + arrayList2.size());
        for (int i3 = 0; i3 < arrayList2.size(); i3++) {
            strArr[i3] = (String) arrayList2.get(i3);
            Log.d(TAG, "getExternalStorageDirectories: " + ((String) arrayList2.get(i3)));
        }
        return strArr;
    }

    public static boolean checkIsArchive(String str) {
        if (new File(str).isDirectory()) {
            return false;
        }
        String extension = StorageUtils.getExtension(str);
        if (extension.equalsIgnoreCase(".tar") || extension.equalsIgnoreCase(".zip") || extension.equalsIgnoreCase(".7z")) {
            return true;
        }
        return false;
    }
}
