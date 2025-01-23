package com.files.zip.unzip.unrar.ultrapro.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.files.zip.unzip.unrar.ultrapro.R;
import com.files.zip.unzip.unrar.ultrapro.databinding.GalleryAdapterLayoutBinding;
import com.files.zip.unzip.unrar.ultrapro.interfaces.CommonInter;
import com.files.zip.unzip.unrar.ultrapro.model.DataModel;
import com.files.zip.unzip.unrar.ultrapro.utils.Resizer;
import com.files.zip.unzip.unrar.ultrapro.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> implements Filterable {
    static ArrayList<DataModel> arrayList;
    ArrayList<DataModel> filteredList;
    CommonInter commonInter;
    Context context;
    int type;
    ValueFilter valueFilter;

    public GalleryAdapter(Context context2, ArrayList<DataModel> arrayList2, int i, CommonInter commonInter2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.filteredList = new ArrayList<>(arrayList);
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
                } else {
                    Glide.with(this.context).load(Integer.valueOf(R.drawable.apk)).into(viewHolder.binding.imageTwo);
                }
            } else if (i2 == 5) {
                if (file.getName().endsWith(".zip") || file.getName().endsWith(".tar") || file.getName().endsWith(".7z")) {
                    Glide.with(this.context).load(Integer.valueOf(R.drawable.zip_thumb)).into(viewHolder.binding.imageTwo);
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
            viewHolder.binding.checkboxTwo.setChecked(arrayList.get(i).isCheck());
            viewHolder.binding.nameTwo.setText(arrayList.get(i).getName());
            viewHolder.binding.timeTwo.setText(arrayList.get(i).getTime());
            viewHolder.binding.sizeTwo.setText(arrayList.get(i).getSize());
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
            Glide.with(this.context).load(arrayList.get(i).getPath()).into(viewHolder.binding.image);
        }

        viewHolder.binding.checkbox.setChecked(arrayList.get(i).isCheck());
        viewHolder.binding.name.setText(arrayList.get(i).getName());
        viewHolder.binding.time.setText(arrayList.get(i).getTime());

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

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            String charString = charSequence.toString();
            ArrayList<DataModel> filteredListTemp;

            if (charString.isEmpty()) {
                filteredListTemp = filteredList;
            } else {
                filteredListTemp = searchFollowersFilter(filteredList, charString);
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredListTemp;
            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            @SuppressWarnings("unchecked")
            ArrayList<DataModel> resultList = (ArrayList<DataModel>) filterResults.values;
            arrayList.clear();
            if (resultList != null) {
                arrayList.addAll(resultList);
            }
            notifyDataSetChanged();
        }
    }

    public static ArrayList<DataModel> searchFollowersFilter(ArrayList<DataModel> list, String charString) {
        ArrayList<DataModel> filteredTempList = new ArrayList<>();
        for (DataModel follower : list) {
            if (follower != null) {
                if (containsIgnoreCase(follower.getName(), charString)
                        || containsIgnoreCase(String.valueOf(follower.getName()), charString)) {
                    filteredTempList.add(follower);
                }
            }
        }
        return filteredTempList;
    }

    public static boolean containsIgnoreCase(String src, String charString) {
        final int length = charString.length();
        if (length == 0) {
            return true;
        }
        for (int i = src.length() - length; i >= 0; i--) {
            if (src.regionMatches(true, i, charString, 0, length)) {
                return true;
            }
        }
        return false;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        GalleryAdapterLayoutBinding binding;

        public ViewHolder(GalleryAdapterLayoutBinding galleryAdapterLayoutBinding) {
            super(galleryAdapterLayoutBinding.getRoot());
            this.binding = galleryAdapterLayoutBinding;
            Resizer.getheightandwidth(GalleryAdapter.this.context);
            if (GalleryAdapter.this.type == 0) {
                Resizer.setSize(galleryAdapterLayoutBinding.cardView, 320, 320, true);
                Resizer.setMargin(galleryAdapterLayoutBinding.getRoot(), 0, 0, 0, 0);
                galleryAdapterLayoutBinding.layoutTwo.setVisibility(8);
                galleryAdapterLayoutBinding.layoutOne.setVisibility(0);
                galleryAdapterLayoutBinding.time.setVisibility(8);
                galleryAdapterLayoutBinding.name.setVisibility(4);
                galleryAdapterLayoutBinding.timeBg.setVisibility(8);
                galleryAdapterLayoutBinding.playBtn.setVisibility(8);
            } else if (GalleryAdapter.this.type == 1 || GalleryAdapter.this.type == 2) {
                galleryAdapterLayoutBinding.layoutTwo.setVisibility(8);
                galleryAdapterLayoutBinding.layoutOne.setVisibility(0);
                galleryAdapterLayoutBinding.time.setVisibility(0);
                galleryAdapterLayoutBinding.name.setVisibility(0);
                galleryAdapterLayoutBinding.timeBg.setVisibility(0);
                galleryAdapterLayoutBinding.playBtn.setVisibility(0);
            } else if (GalleryAdapter.this.type == 3 || GalleryAdapter.this.type == 4) {
                galleryAdapterLayoutBinding.layoutOne.setVisibility(8);
                galleryAdapterLayoutBinding.layoutTwo.setVisibility(0);
            }
        }
    }

    public void notifyAdapter(ArrayList<DataModel> arrayList2) {
        arrayList = arrayList2;
        notifyDataSetChanged();
    }

}
