package com.raktatech.winzip.interfaces;

import com.raktatech.winzip.model.DataModel;
import java.util.ArrayList;

public interface OnProgressUpdate {
    void onComplete(ArrayList<DataModel> arrayList);

    void onTaskStart();
}
