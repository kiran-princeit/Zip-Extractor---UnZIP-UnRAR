package com.files.zip.unzip.unrar.ultrapro.interfaces;

public interface UnzipInterface {
    void complete();

    void error();

    void progressUpdate(int i);

    void start();
}
