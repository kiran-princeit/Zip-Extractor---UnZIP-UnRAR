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
import com.files.zip.unzip.unrar.ultrapro.activity.FolderActivity;
import com.files.zip.unzip.unrar.ultrapro.databinding.CompressDialogLayoutBinding;
import com.files.zip.unzip.unrar.ultrapro.databinding.DocumentAdapterLayoutBinding;
import com.files.zip.unzip.unrar.ultrapro.interfaces.CommonInter;
import com.files.zip.unzip.unrar.ultrapro.model.DataModel;

import com.files.zip.unzip.unrar.ultrapro.utils.Common;
import com.files.zip.unzip.unrar.ultrapro.utils.Resizer;
import com.files.zip.unzip.unrar.ultrapro.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> implements Filterable {
    ArrayList<DataModel> arrayList;
    ArrayList<DataModel> filteredList;
    CommonInter commonInter;
    Context context;

    public FolderAdapter(Context context2, ArrayList<DataModel> arrayList2, CommonInter commonInter2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.filteredList = new ArrayList<>(arrayList);
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
            if (file.getName().endsWith(".apk")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.apk)).into(viewHolder.binding.image);
            } else if (file.getName().endsWith(".mp3")) {
                Glide.with(this.context).load(Integer.valueOf(R.drawable.music_thumb)).into(viewHolder.binding.image);
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

        if (dataModel.isCheckboxVisible()) {
            viewHolder.binding.checkbox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.binding.checkbox.setVisibility(View.GONE);
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
                    dataModel.setCheckboxVisible(false);
                    ((FolderActivity) context).updateFooterVisibility();

                } else {
                    int position = viewHolder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        commonInter.clickItem(position, 0);
                    }
                }
                FolderAdapter.this.notifyAdapter(Common.arrayList);
            }
        });

        viewHolder.itemView.setOnLongClickListener(view -> {
            viewHolder.binding.checkbox.setVisibility(View.GONE);
            dataModel.setCheckboxVisible(true);
            commonInter.clickItem(i, 1);
            notifyDataSetChanged();
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

    public void updateList(ArrayList<DataModel> list) {
        filteredList.clear();
        filteredList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateListnew(ArrayList<DataModel> updatedList) {
        arrayList.clear();
        arrayList.addAll(updatedList);
        notifyDataSetChanged();
    }

    ValueFilter valueFilter;

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
