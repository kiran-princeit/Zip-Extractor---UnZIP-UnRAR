package com.raktatech.winzip.activity;

import static com.raktatech.winzip.activity.GalleryActivity.TYPE_INT;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.ServiceStarter;
import com.raktatech.winzip.databinding.ActivityDeleteProcessBinding;
import com.raktatech.winzip.utils.Resizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class DeleteProcessActivity extends AppCompatActivity {
    public static ArrayList<File> deleteList = new ArrayList<>();
    public static Handler handler1 = new Handler();
    public static Runnable runnable1;
    ActivityDeleteProcessBinding binding;
    int i = 0;

    public void onBackPressed() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityDeleteProcessBinding inflate = ActivityDeleteProcessBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        resize();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    public void run() {
                        for (int i = 0; i < DeleteProcessActivity.deleteList.size(); i++) {
                            DeleteProcessActivity.this.i = i;
                            DeleteProcessActivity.handler1.post(new Runnable() {
                                public void run() {
                                    if (DeleteProcessActivity.deleteList.get(DeleteProcessActivity.this.i).isDirectory()) {
                                        try {
                                            FileUtils.deleteDirectory(DeleteProcessActivity.deleteList.get(DeleteProcessActivity.this.i));
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    } else if (!DeleteProcessActivity.deleteList.get(DeleteProcessActivity.this.i).delete()) {
                                        Log.d("TAG", "run: if ");
                                        try {
                                            Log.d("TAG", "run: try ");
                                            FileUtils.forceDelete(DeleteProcessActivity.deleteList.get(DeleteProcessActivity.this.i));
                                            MediaScannerConnection.scanFile(DeleteProcessActivity.this, new String[]{DeleteProcessActivity.deleteList.get(DeleteProcessActivity.this.i).getAbsolutePath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                                public void onScanCompleted(String str, Uri uri) {
                                                }
                                            });
                                        } catch (Exception e2) {
                                            Log.d("TAG", "run: catch  " + e2.getMessage());
                                            e2.printStackTrace();
                                        }
                                    } else {
                                        Log.d("TAG", "run: else ");
                                        MediaScannerConnection.scanFile(DeleteProcessActivity.this, new String[]{DeleteProcessActivity.deleteList.get(DeleteProcessActivity.this.i).getAbsolutePath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                            public void onScanCompleted(String str, Uri uri) {
                                            }
                                        });
                                    }
                                    DeleteProcessActivity.this.binding.progress.setProgress(((DeleteProcessActivity.this.i + 1) * 100) / DeleteProcessActivity.deleteList.size());
                                    if (DeleteProcessActivity.this.i == DeleteProcessActivity.deleteList.size() - 1) {
                                        Log.d("TAG", "run:  demo  ");
                                        DeleteProcessActivity.handler1.removeCallbacksAndMessages((Object) null);
                                        Intent intent = new Intent(DeleteProcessActivity.this, CompleteProcessActivity.class);
                                        intent.setFlags(536870912);
                                        intent.putExtra("Type", 2);
                                        DeleteProcessActivity.this.startActivity(intent);
                                        DeleteProcessActivity.this.finish();
                                    }
                                }
                            });
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException unused) {
                            }
                        }
                    }
                }).start();
            }
        }, 1000);
    }

    private void resize() {
//        Resizer.getheightandwidth(this);
//        Resizer.setSize(this.binding.header.header, 1080, 154, true);
//        Resizer.setSize(this.binding.lottie, ServiceStarter.ERROR_UNKNOWN, ServiceStarter.ERROR_UNKNOWN, true);
//        Resizer.setSize(this.binding.pleaseWait, 326, 40, true);
//        Resizer.setSize(this.binding.msg, 444, 65, true);
//        Resizer.setSize(this.binding.progress, TYPE_INT, 20, true);
//        Resizer.setMargin(this.binding.pleaseWait, 0, 200, 0, 0);
        this.binding.header.back.setVisibility(8);
        this.binding.header.title.setText("Processing");
    }

}
