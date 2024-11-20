package com.raktatech.winzip.zip;

public interface IZipCallback {
    void onFinish(boolean z);

    void onProgress(int i);

    void onStartIZip();
}
