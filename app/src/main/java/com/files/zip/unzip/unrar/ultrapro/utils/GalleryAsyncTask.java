package com.files.zip.unzip.unrar.ultrapro.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import com.files.zip.unzip.unrar.ultrapro.enums.TYPE;
import com.files.zip.unzip.unrar.ultrapro.interfaces.OnProgressUpdate;
import com.files.zip.unzip.unrar.ultrapro.model.DataModel;

import java.io.File;
import java.util.ArrayList;

public class GalleryAsyncTask extends AsyncTask<Void, Void, Void> {
    public ArrayList<DataModel> arrayList = new ArrayList<>();
    public static final String S_DURATION = "duration";
    Context context;
    OnProgressUpdate listener;
    TYPE type;

    public GalleryAsyncTask(TYPE type2, Context context2, OnProgressUpdate onProgressUpdate) {
        this.type = type2;
        this.context = context2;
        this.listener = onProgressUpdate;
    }


    public void onPreExecute() {
        super.onPreExecute();
        this.listener.onTaskStart();
    }


    public Void doInBackground(Void... voidArr) {
        this.arrayList = getFolderList();
        return null;
    }


    public void onPostExecute(Void voidR) {
        super.onPostExecute(voidR);
        this.listener.onComplete(this.arrayList);
    }

    public ArrayList<DataModel> getFolderList() {
        ArrayList<DataModel> arrayList2 = new ArrayList<>();
        if (this.type.equals(TYPE.IMAGES)) {
            Cursor query = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data"}, "mime_type=? OR mime_type=?", new String[]{"image/jpeg", "image/png"}, "date_added DESC");
            if (query != null && query.moveToFirst()) {
                query.getColumnIndex("_id");
                int columnIndex = query.getColumnIndex("_data");
                do {
                    String string = query.getString(columnIndex);
                    StorageUtils.scanFile(this.context, string);
                    File file = new File(string);
                    if (file.exists()) {
                        arrayList2.add(new DataModel(file.getName(), file.getAbsolutePath(), StorageUtils.folderSize((double) file.length()), StorageUtils.fileDate(file.lastModified(), "dd/MM/yyyy - HH:mm"), false,false));
                    }
                } while (query.moveToNext());
                query.close();
            }
        } else if (this.type.equals(TYPE.VIDEOS)) {
            Cursor query2 = this.context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name", S_DURATION}, (String) null, (String[]) null, "date_added DESC");
            int columnIndexOrThrow = query2.getColumnIndexOrThrow("_data");
            int columnIndex2 = query2.getColumnIndex(S_DURATION);
            while (query2.moveToNext()) {
                String string2 = query2.getString(columnIndexOrThrow);
                long j = query2.getLong(columnIndex2);
                StorageUtils.scanFile(this.context, string2);
                File file2 = new File(string2);
                if (file2.exists()) {
                    arrayList2.add(new DataModel(file2.getName(), file2.getAbsolutePath(), StorageUtils.folderSize((double) file2.length()), StorageUtils.timeConversion(j), false,false));
                }
            }
            query2.close();
        } else if (this.type.equals(TYPE.AUDIO)) {
            Cursor query3 = this.context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data", S_DURATION}, (String) null, (String[]) null, "date_added DESC");
            int columnIndex3 = query3.getColumnIndex("_data");
            int columnIndex4 = query3.getColumnIndex(S_DURATION);
            while (query3.moveToNext()) {
                String string3 = query3.getString(columnIndex3);
                long j2 = query3.getLong(columnIndex4);
                StorageUtils.scanFile(this.context, string3);
                File file3 = new File(string3);
                if (file3.exists()) {
                    arrayList2.add(new DataModel(file3.getName(), file3.getAbsolutePath(), StorageUtils.folderSize((double) file3.length()), StorageUtils.timeConversion(j2), false,false));
                }
            }
            query3.close();
        } else if (this.type == TYPE.APP) {
            String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String[] strArr = {"%" + absolutePath + "%"};
            Cursor query4 = this.context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id", "_data"}, "_data LIKE '%.apk%' AND _data like ? ", strArr, (String) null);
            if (query4 != null && query4.moveToFirst()) {
                do {
                    String string4 = query4.getString(query4.getColumnIndexOrThrow("_data"));
                    StorageUtils.scanFile(this.context, string4);
                    File file4 = new File(string4);
                    if (file4.exists()) {
                        arrayList2.add(new DataModel(file4.getName(), file4.getAbsolutePath(), StorageUtils.folderSize((double) file4.length()), StorageUtils.fileDate(file4.lastModified(), "dd/MM/yyyy - HH:mm"), false,false));
                    }
                } while (query4.moveToNext());
            }
        } else if (this.type == TYPE.DOCUMENT) {
            String absolutePath2 = Environment.getExternalStorageDirectory().getAbsolutePath();
            String[] strArr2 = {"%" + absolutePath2 + "%"};
            Cursor query5 = this.context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id", "_data"}, "_data like ? ", strArr2, (String) null);
            while (query5.moveToNext()) {
                File file5 = new File(query5.getString(query5.getColumnIndexOrThrow("_data")));
                if (file5.exists() && (file5.getName().endsWith(".xls") || file5.getName().endsWith(".pdf") || file5.getName().endsWith(".txt") || file5.getName().endsWith(".docx") || file5.getName().endsWith(".doc") || file5.getName().endsWith(".xml") || file5.getName().endsWith(".csv") || file5.getName().endsWith(".ppt") || file5.getName().endsWith(".pptx"))) {
                    arrayList2.add(new DataModel(file5.getName(), file5.getAbsolutePath(), StorageUtils.folderSize((double) file5.length()), StorageUtils.fileDate(file5.lastModified(), "dd/MM/yyyy - HH:mm"), false,false));
                }
            }
        }else if (this.type == TYPE.ARCHIVE) {
            String absolutePath2 = Environment.getExternalStorageDirectory().getAbsolutePath();
            String[] strArr2 = {"%" + absolutePath2 + "%"};
            Cursor query6 = this.context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id", "_data"}, "_data like ? ", strArr2, (String) null);
            while (query6.moveToNext()) {
                File file6 = new File(query6.getString(query6.getColumnIndexOrThrow("_data")));
                if (file6.exists() && (file6.getName().endsWith(".zip") || file6.getName().endsWith(".tar") || file6.getName().endsWith(".7z")))
                {
                    arrayList2.add(new DataModel(file6.getName(), file6.getAbsolutePath(), StorageUtils.folderSize((double) file6.length()), StorageUtils.fileDate(file6.lastModified(), "dd/MM/yyyy - HH:mm"), false,false));
                }
            }

        }
        return arrayList2;
    }
}


//public class GalleryAsyncTask extends AsyncTask<Void, ArrayList<DataModel>, Void> {
//    public ArrayList<DataModel> arrayList = new ArrayList<>();
//    public static final String S_DURATION = "duration";
//    private static final int CHUNK_SIZE = 50; // Number of items per batch
//    private Context context;
//    private OnProgressUpdate listener;
//    private TYPE type;
//
//    public GalleryAsyncTask(TYPE type, Context context, OnProgressUpdate listener) {
//        this.type = type;
//        this.context = context;
//        this.listener = listener;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        listener.onTaskStart();
//    }
//
//    @Override
//    protected Void doInBackground(Void... voids) {
//        ArrayList<DataModel> tempArrayList = getFolderListInChunks();
//        return null;
//    }
//
//    @Override
//    protected void onProgressUpdate(ArrayList<DataModel>... values) {
//        super.onProgressUpdate(values);
//        if (values.length > 0) {
//            listener.onComplete(values[0]); // Pass each batch to the listener
//        }
//    }
//
//    @Override
//    protected void onPostExecute(Void unused) {
//        super.onPostExecute(unused);
//        listener.onComplete(arrayList); // Final update if needed
//    }
//
//    public ArrayList<DataModel> getFolderListInChunks() {
//        ArrayList<DataModel> tempArrayList = new ArrayList<>();
//        Cursor query = null;
//
//        if (type.equals(TYPE.IMAGES)) {
//            query = context.getContentResolver().query(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    new String[]{"_id", "_data"},
//                    "mime_type=? OR mime_type=?",
//                    new String[]{"image/jpeg", "image/png"},
//                    "date_added DESC"
//            );
//        } else if (type.equals(TYPE.VIDEOS)) {
//            query = context.getContentResolver().query(
//                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                    new String[]{"_data", "bucket_display_name", S_DURATION},
//                    null,
//                    null,
//                    "date_added DESC"
//            );
//
//        } else if (type.equals(TYPE.ARCHIVE)) {
//            // For archive files like ZIP, RAR, etc.
//            query = context.getContentResolver().query(
//                    MediaStore.Files.getContentUri("external"),
//                    new String[]{"_data", "_display_name"},
//                    "_data LIKE ?",
//                    new String[]{"%.zip"},
//                    "date_added DESC"
//            );
//        }
//        // Add other type queries as needed...
//
//        if (query != null) {
//            int dataIndex = query.getColumnIndex("_data");
//            int count = 0;
//
//            while (query.moveToNext()) {
//                String filePath = query.getString(dataIndex);
//                File file = new File(filePath);
//
//                if (file.exists()) {
//                    DataModel model = new DataModel(
//                            file.getName(),
//                            file.getAbsolutePath(),
//                            StorageUtils.folderSize((double) file.length()),
//                            StorageUtils.fileDate(file.lastModified(), "dd/MM/yyyy - HH:mm"),
//                            false, false
//                    );
//                    tempArrayList.add(model);
//
//                    count++;
//
//                    // Publish updates for each CHUNK_SIZE
//                    if (count % CHUNK_SIZE == 0) {
//                        publishProgress(new ArrayList<>(tempArrayList));
//                        tempArrayList.clear();
//                    }
//                }
//            }
//            query.close();
//        }
//
//        // Publish remaining data
//        if (!tempArrayList.isEmpty()) {
//            publishProgress(new ArrayList<>(tempArrayList));
//        }
//
//        return tempArrayList;
//    }
//}
