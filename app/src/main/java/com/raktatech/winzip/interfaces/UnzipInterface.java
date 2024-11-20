package com.raktatech.winzip.interfaces;

public interface UnzipInterface {
    void complete();

    void error();

    void progressUpdate(int i);

    void start();
}
