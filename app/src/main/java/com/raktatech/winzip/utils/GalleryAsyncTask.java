package com.raktatech.winzip.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import com.raktatech.winzip.enums.TYPE;
import com.raktatech.winzip.interfaces.OnProgressUpdate;
import com.raktatech.winzip.model.DataModel;
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
                        arrayList2.add(new DataModel(file.getName(), file.getAbsolutePath(), StorageUtils.folderSize((double) file.length()), StorageUtils.fileDate(file.lastModified(), "dd/MM/yyyy - HH:mm"), false));
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
                    arrayList2.add(new DataModel(file2.getName(), file2.getAbsolutePath(), StorageUtils.folderSize((double) file2.length()), StorageUtils.timeConversion(j), false));
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
                    arrayList2.add(new DataModel(file3.getName(), file3.getAbsolutePath(), StorageUtils.folderSize((double) file3.length()), StorageUtils.timeConversion(j2), false));
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
                        arrayList2.add(new DataModel(file4.getName(), file4.getAbsolutePath(), StorageUtils.folderSize((double) file4.length()), StorageUtils.fileDate(file4.lastModified(), "dd/MM/yyyy - HH:mm"), false));
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
                    arrayList2.add(new DataModel(file5.getName(), file5.getAbsolutePath(), StorageUtils.folderSize((double) file5.length()), StorageUtils.fileDate(file5.lastModified(), "dd/MM/yyyy - HH:mm"), false));
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
                    arrayList2.add(new DataModel(file6.getName(), file6.getAbsolutePath(), StorageUtils.folderSize((double) file6.length()), StorageUtils.fileDate(file6.lastModified(), "dd/MM/yyyy - HH:mm"), false));
                }
            }

        }
        return arrayList2;
    }
}


//public class GalleryAsyncTask extends AsyncTask<Void, ArrayList<DataModel>, Void> {
//    private ArrayList<DataModel> arrayList = new ArrayList<>();
//    private static final String S_DURATION = "duration";
//    private Context context;
//    private OnProgressUpdate listener;
//    private TYPE type;
//
//    private static final int BATCH_SIZE = 20; // Load 20 items at a time
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
//        this.listener.onTaskStart();
//    }
//
//    @Override
//    protected Void doInBackground(Void... voidArr) {
//        ArrayList<DataModel> dataBatch = getFolderList();
//        publishProgress(dataBatch);  // Publish the first batch
//        return null;
//    }
//
//    @Override
//    protected void onProgressUpdate(ArrayList<DataModel>... values) {
//        super.onProgressUpdate(values);
//        ArrayList<DataModel> batch = values[0];
//
//        arrayList.addAll(batch);  // Add the newly loaded batch to the main list
//
//        // Notify the listener (RecyclerView adapter) to update the data
//        this.listener.onComplete(arrayList);
//    }
//
//    @Override
//    protected void onPostExecute(Void result) {
//        super.onPostExecute(result);
//        this.listener.onComplete(this.arrayList);  // Final update
//    }
//
//    private ArrayList<DataModel> getFolderList() {
//        ArrayList<DataModel> batchList = new ArrayList<>();
//        int counter = 0;
//
//        if (this.type.equals(TYPE.IMAGES)) {
//            Cursor query = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    new String[]{"_id", "_data"}, "mime_type=? OR mime_type=?", new String[]{"image/jpeg", "image/png"}, "date_added DESC");
//
//            if (query != null && query.moveToFirst()) {
//                int columnIndex = query.getColumnIndex("_data");
//
//                // Loop through the results and load in batches
//                do {
//                    String path = query.getString(columnIndex);
//                    File file = new File(path);
//                    if (file.exists()) {
//                        batchList.add(new DataModel(file.getName(), file.getAbsolutePath(), StorageUtils.folderSize(file.length()), StorageUtils.fileDate(file.lastModified(), "dd/MM/yyyy - HH:mm"), false));
//                    }
//                    counter++;
//
//                    // Publish the data after every batch (BATCH_SIZE)
//                    if (counter % BATCH_SIZE == 0) {
//                        publishProgress(new ArrayList<>(batchList));
//                        batchList.clear();  // Clear the batch to load the next set of items
//                    }
//                } while (query.moveToNext());
//
//                query.close();
//            }
//        }
//        // Repeat the same logic for other types (VIDEOS, AUDIO, DOCUMENT, etc.)
//
//        return batchList;
//    }
//}
