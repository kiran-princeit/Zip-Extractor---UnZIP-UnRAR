package com.files.zip.unzip.unrar.ultrapro.activity;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.files.zip.unzip.unrar.ultrapro.R;
import com.files.zip.unzip.unrar.ultrapro.databinding.ActivitySplashBinding;
import com.files.zip.unzip.unrar.ultrapro.utils.LanguagePreference;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    private MediaPlayer mediaPlayer;
    private TextureView textureView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivitySplashBinding inflate = ActivitySplashBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textureView = findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(android.graphics.SurfaceTexture  surface, int width, int height) {
                Surface videoSurface = new Surface(surface);
                playVideo(videoSurface);
            }
            @Override
            public void onSurfaceTextureSizeChanged(android.graphics.SurfaceTexture  surface, int width, int height) {}
            @Override
            public boolean onSurfaceTextureDestroyed(android.graphics.SurfaceTexture  surface) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                return true;
            }
            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

            }
        });
    }

    private void playVideo(Surface surface) {
        try {
            mediaPlayer = new MediaPlayer();
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video);
            mediaPlayer.setDataSource(this, videoUri);
            mediaPlayer.setSurface(surface);
            mediaPlayer.setOnPreparedListener(mp -> {
                adjustVideoSize();
                mediaPlayer.start();
            });
            mediaPlayer.setOnCompletionListener(mp -> {
               jump();
            });

            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adjustVideoSize() {
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        float videoAspectRatio = (float) videoWidth / videoHeight;
        float screenAspectRatio = (float) screenWidth / screenHeight;

        ViewGroup.LayoutParams layoutParams = textureView.getLayoutParams();

        if (videoAspectRatio > screenAspectRatio) {
            layoutParams.width = screenWidth;
            layoutParams.height = (int) (screenWidth / videoAspectRatio);
        } else {
            layoutParams.width = (int) (screenHeight * videoAspectRatio);
            layoutParams.height = screenHeight;
        }
        textureView.setLayoutParams(layoutParams);
    }

    private void jump() {

        new Handler().postDelayed(() -> {
            if (LanguagePreference.isFirstTime(this)) {
                Intent intent = new Intent(this, LanguageSelectionActivity.class);
                startActivity(intent);
                LanguagePreference.setFirstTime(this, false);
            } else {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }
            finish();

        }, 2000);
    }
}
