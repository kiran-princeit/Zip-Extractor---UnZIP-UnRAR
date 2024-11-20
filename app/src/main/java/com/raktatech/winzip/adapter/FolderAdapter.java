package com.raktatech.winzip.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raktatech.winzip.R;
import com.raktatech.winzip.activity.CompressedProcessActivity;
import com.raktatech.winzip.activity.FolderActivity;
import com.raktatech.winzip.databinding.CompressDialogLayoutBinding;
import com.raktatech.winzip.databinding.DocumentAdapterLayoutBinding;
import com.raktatech.winzip.interfaces.CommonInter;
import com.raktatech.winzip.model.DataModel;

import com.raktatech.winzip.utils.Common;
import com.raktatech.winzip.utils.Resizer;
import com.raktatech.winzip.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    ArrayList<DataModel> arrayList;
    CommonInter commonInter;
    Context context;

    public FolderAdapter(Context context2, ArrayList<DataModel> arrayList2, CommonInter commonInter2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.commonInter = commonInter2;

    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(DocumentAdapterLayoutBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        DataModel dataModel = arrayList.get(i);

        File file = new File(dataModel.getPath());
        if (file.isDirectory()) {
            Glide.with(this.context).load(Integer.valueOf(R.drawable.folder_thumb)).into(viewHolder.binding.image);
        } else {
            Log.d("TAG", "onBindViewHolder: else " + file.getAbsolutePath());
            if (file.getName().endsWith(".apk")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.apk)).into(viewHolder.binding.image);
            } else if (file.getName().endsWith(".mp3")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.music_thumb)).into(viewHolder.binding.image);
                Log.d("TAG", "onBindViewHolder: else-if " + file.getAbsolutePath());
            } else if (StorageUtils.isImageFile(file.getAbsolutePath())) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.image_thumb)).into(viewHolder.binding.image);
            } else if (file.getName().endsWith(".mp4")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.video_thumb)).into(viewHolder.binding.image);
            } else if (file.getName().endsWith(".docx") || file.getName().endsWith(".doc")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.doc)).into(viewHolder.binding.image);
            } else if (file.getName().endsWith(".pdf")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.pdf)).into(viewHolder.binding.image);
            } else if (file.getName().endsWith(".txt")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.text_icon)).into(viewHolder.binding.image);
            } else if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.exl)).into(viewHolder.binding.image);
            } else if (file.getName().endsWith(".pptx") || file.getName().endsWith(".ppt")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.ppt)).into(viewHolder.binding.image);
            } else {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.zip_thumb)).into(viewHolder.binding.image);
            }
        }
        viewHolder.binding.checkbox.setChecked(this.arrayList.get(i).isCheck());
        viewHolder.binding.name.setText(this.arrayList.get(i).getName());
        viewHolder.binding.time.setText(this.arrayList.get(i).getTime());
        viewHolder.binding.size.setText(this.arrayList.get(i).getSize());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.binding.checkbox.getVisibility() == View.VISIBLE) {

                    viewHolder.binding.checkbox.setVisibility(View.GONE);
                    Common.arrayList.get(i).setCheck(!Common.arrayList.get(i).isCheck());
                    FolderAdapter.this.notifyAdapter(Common.arrayList);
                } else {

                    int position = viewHolder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        commonInter.clickItem(position, 0);

                    }
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(view -> {
            commonInter.clickItem(i, 1);
            viewHolder.binding.checkbox.setVisibility(View.VISIBLE);

            return true;
        });

    }

    public int getItemViewType(int i) {
        return super.getItemViewType(i);
    }

    public long getItemId(int i) {
        return super.getItemId(i);
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        DocumentAdapterLayoutBinding binding;

        public ViewHolder(DocumentAdapterLayoutBinding documentAdapterLayoutBinding) {
            super(documentAdapterLayoutBinding.getRoot());
            this.binding = documentAdapterLayoutBinding;
            Resizer.getheightandwidth(FolderAdapter.this.context);
        }
    }

    public void notifyAdapter(ArrayList<DataModel> arrayList2) {
        this.arrayList = arrayList2;
        notifyDataSetChanged();
    }
}
