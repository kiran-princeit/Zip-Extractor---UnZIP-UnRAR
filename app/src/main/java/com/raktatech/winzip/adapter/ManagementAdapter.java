package com.raktatech.winzip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.raktatech.winzip.R;
import com.raktatech.winzip.databinding.ManagementAdapterLayoutBinding;
import com.raktatech.winzip.interfaces.CommonInter;
import com.raktatech.winzip.model.DataModel;

import com.raktatech.winzip.utils.StorageUtils;
import java.io.File;
import java.util.ArrayList;

public class ManagementAdapter extends RecyclerView.Adapter<ManagementAdapter.ViewHolder> {
    ArrayList<DataModel> arrayList;
    CommonInter commonInter;
    Context context;

    public ManagementAdapter(Context context2, ArrayList<DataModel> arrayList2, CommonInter commonInter2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.commonInter = commonInter2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(ManagementAdapterLayoutBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        File file = new File(this.arrayList.get(i).getPath());
        if (file.isDirectory()) {
            Glide.with(this.context).load(Integer.valueOf(R.drawable.folder_thumb)).into(viewHolder.binding.image);
        } else if (file.getName().endsWith(".apk")) {
            Glide.with(this.context).load(Integer.valueOf(R.drawable.apk)).into(viewHolder.binding.image);
        } else if (StorageUtils.isAudioFile(file.getAbsolutePath())) {
            Glide.with(this.context).load(Integer.valueOf(R.drawable.music_thumb)).into(viewHolder.binding.image);
        } else if (StorageUtils.isImageFile(file.getAbsolutePath())) {
            Glide.with(this.context).load(file.getAbsolutePath()).into(viewHolder.binding.image);
        } else if (StorageUtils.isVideoFile(file.getAbsolutePath())) {
            Glide.with(this.context).load(file.getAbsolutePath()).into(viewHolder.binding.image);
        } else {
            Glide.with(this.context).load(Integer.valueOf(R.drawable.doc)).into(viewHolder.binding.image);
        }
        viewHolder.binding.name.setText(this.arrayList.get(i).getName());
        viewHolder.binding.time.setText(this.arrayList.get(i).getTime());
        viewHolder.binding.size.setText(this.arrayList.get(i).getSize());
        viewHolder.binding.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonInter.clickItem(i, 1);
            }
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
        ManagementAdapterLayoutBinding binding;

        public ViewHolder(ManagementAdapterLayoutBinding managementAdapterLayoutBinding) {
            super(managementAdapterLayoutBinding.getRoot());
            this.binding = managementAdapterLayoutBinding;
//            Resizer.getheightandwidth(ManagementAdapter.this.context);
//            Resizer.setSize(managementAdapterLayoutBinding.getRoot(), 1000, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, true);
//            Resizer.setSize(managementAdapterLayoutBinding.image, 180, 180, true);
        }
    }

    public void notifyAdapter(ArrayList<DataModel> arrayList2) {
        this.arrayList = arrayList2;
        notifyDataSetChanged();
    }
}
