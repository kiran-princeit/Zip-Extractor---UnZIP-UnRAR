package com.files.zip.unzip.unrar.ultrapro.zip;

public interface IZipCallback {
    void onFinish(boolean z);

    void onProgress(int i);

    void onStartIZip();
}
