package com.raktatech.winzip.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raktatech.winzip.R;
import com.raktatech.winzip.activity.GalleryActivity;
import com.raktatech.winzip.databinding.GalleryAdapterLayoutBinding;
import com.raktatech.winzip.interfaces.CommonInter;
import com.raktatech.winzip.model.DataModel;
import com.raktatech.winzip.utils.Resizer;
import com.raktatech.winzip.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    ArrayList<DataModel> arrayList;
    CommonInter commonInter;
    Context context;
    int type;

    public GalleryAdapter(Context context2, ArrayList<DataModel> arrayList2, int i, CommonInter commonInter2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.type = i;
        this.commonInter = commonInter2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(GalleryAdapterLayoutBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        File file = new File(this.arrayList.get(i).getPath());
        int i2 = this.type;
        if (i2 == 3 || i2 == 4 || i2 == 5) {
            if (i2 == 3) {
                if (StorageUtils.isAudioFile(file.getAbsolutePath()) || file.getName().endsWith(".mp3")) {
                    Glide.with(this.context).load(Integer.valueOf(R.drawable.music_thumb)).into(viewHolder.binding.imageTwo);
//                    Resizer.setSize(viewHolder.binding.imageTwo, 150, 150, true);
                } else {
                    Glide.with(this.context).load(Integer.valueOf(R.drawable.apk_icon_mask_default)).into(viewHolder.binding.imageTwo);
//                    Resizer.setSize(viewHolder.binding.imageTwo, 150, 150, true);
                }
            } else if (i2 == 5) {
                if (file.getName().endsWith(".zip") || file.getName().endsWith(".tar") || file.getName().endsWith(".7z")) {
                    Glide.with(this.context).load(Integer.valueOf(R.drawable.zip_thumb)).into(viewHolder.binding.imageTwo);
                    Log.e("TAG", "onBindViewHolder: " + Integer.valueOf(R.drawable.zip_thumb));
                }
                Glide.with(this.context).load(Integer.valueOf(R.drawable.zip_thumb)).into(viewHolder.binding.imageTwo);

            } else if (file.getName().endsWith(".docx") || file.getName().endsWith(".doc")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.doc)).into(viewHolder.binding.imageTwo);
            } else if (file.getName().endsWith(".pdf")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.pdf)).into(viewHolder.binding.imageTwo);
            } else if (file.getName().endsWith(".txt")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.text_icon)).into(viewHolder.binding.imageTwo);
            } else if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.exl)).into(viewHolder.binding.imageTwo);
            } else if (file.getName().endsWith(".pptx") || file.getName().endsWith(".ppt")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.ppt)).into(viewHolder.binding.imageTwo);
            }
            viewHolder.binding.checkboxTwo.setChecked(this.arrayList.get(i).isCheck());
            viewHolder.binding.nameTwo.setText(this.arrayList.get(i).getName());
            viewHolder.binding.timeTwo.setText(this.arrayList.get(i).getTime());
            viewHolder.binding.sizeTwo.setText(this.arrayList.get(i).getSize());
            viewHolder.binding.checkboxTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commonInter.clickItem(i, 1);
                }
            });

            viewHolder.binding.outClickTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commonInter.clickItem(i, 0);
                }
            });
            return;
        }

        if (i2 == 2) {
            Glide.with(this.context).load(Integer.valueOf(R.drawable.music_thumb)).into(viewHolder.binding.image);
        } else {
            Glide.with(this.context).load(this.arrayList.get(i).getPath()).into(viewHolder.binding.image);
        }

        viewHolder.binding.checkbox.setChecked(this.arrayList.get(i).isCheck());
        viewHolder.binding.name.setText(this.arrayList.get(i).getName());
        viewHolder.binding.time.setText(this.arrayList.get(i).getTime());

        viewHolder.binding.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonInter.clickItem(i, 1);
            }
        });


        viewHolder.binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonInter.clickItem(i, 0);
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
        GalleryAdapterLayoutBinding binding;

        public ViewHolder(GalleryAdapterLayoutBinding galleryAdapterLayoutBinding) {
            super(galleryAdapterLayoutBinding.getRoot());
            this.binding = galleryAdapterLayoutBinding;
            Resizer.getheightandwidth(GalleryAdapter.this.context);
//            Resizer.setSize(galleryAdapterLayoutBinding.checkboxLl, 60, 60, true);
//            Resizer.setSize(galleryAdapterLayoutBinding.timeBg, 177, 69, true);
//            Resizer.setSize(galleryAdapterLayoutBinding.playBtn, 120, 120, true);
            if (GalleryAdapter.this.type == 0) {
                Resizer.setSize(galleryAdapterLayoutBinding.cardView, 320, 320, true);
                Resizer.setMargin(galleryAdapterLayoutBinding.getRoot(), 10, 10, 10, 10);
                galleryAdapterLayoutBinding.layoutTwo.setVisibility(8);
                galleryAdapterLayoutBinding.layoutOne.setVisibility(0);
                galleryAdapterLayoutBinding.time.setVisibility(8);
                galleryAdapterLayoutBinding.name.setVisibility(4);
                galleryAdapterLayoutBinding.timeBg.setVisibility(8);
                galleryAdapterLayoutBinding.playBtn.setVisibility(8);
            } else if (GalleryAdapter.this.type == 1 || GalleryAdapter.this.type == 2) {
                Resizer.setSize(galleryAdapterLayoutBinding.cardView, 490, 490, true);
                Resizer.setMargin(galleryAdapterLayoutBinding.getRoot(), 10, 10, 10, 10);
                galleryAdapterLayoutBinding.layoutTwo.setVisibility(8);
                galleryAdapterLayoutBinding.layoutOne.setVisibility(0);
                galleryAdapterLayoutBinding.time.setVisibility(0);
                galleryAdapterLayoutBinding.name.setVisibility(0);
                galleryAdapterLayoutBinding.timeBg.setVisibility(0);
                galleryAdapterLayoutBinding.playBtn.setVisibility(0);
            } else if (GalleryAdapter.this.type == 3 || GalleryAdapter.this.type == 4) {
//                Resizer.setSize(galleryAdapterLayoutBinding.layoutTwo, 1000, 220, true);
//                Resizer.setSize(galleryAdapterLayoutBinding.imageTwo, 97, 120, true);
                galleryAdapterLayoutBinding.layoutOne.setVisibility(8);
                galleryAdapterLayoutBinding.layoutTwo.setVisibility(0);
            }
        }
    }

    public void notifyAdapter(ArrayList<DataModel> arrayList2) {
        this.arrayList = arrayList2;
        notifyDataSetChanged();
    }
}
