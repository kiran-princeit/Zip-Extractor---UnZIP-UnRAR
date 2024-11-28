package com.files.zip.unzip.unrar.ultrapro.interfaces;

import com.files.zip.unzip.unrar.ultrapro.model.DataModel;
import java.util.ArrayList;

public interface OnProgressUpdate {
    void onComplete(ArrayList<DataModel> arrayList);

    void onTaskStart();
}
