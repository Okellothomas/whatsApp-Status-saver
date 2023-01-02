package com.statuses.flavoured.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.statuses.flavoured.R;
import com.statuses.flavoured.model.ImageModel;
import com.statuses.flavoured.recycler.VideoViewHolder;

import java.util.ArrayList;

public class WhatsappVideoAdapter extends
        RecyclerView.Adapter<VideoViewHolder> {
    private ArrayList<ImageModel> arrayList;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;

    public WhatsappVideoAdapter(Context context,
                          ArrayList<ImageModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        mSelectedItemsIds = new SparseBooleanArray();

    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder,
                                 int position) {

        //Setting text over text view
        RequestOptions centerCrop = new RequestOptions().override(holder.imageView.getWidth(), holder.imageView.getHeight()).centerCrop();
        Glide.with(context).asBitmap().apply(centerCrop).load((String) arrayList.get(position).getPath()).transition(BitmapTransitionOptions.withCrossFade()).into(holder.imageView);

        if(mSelectedItemsIds.get(position)) {
            holder.imageViewCheck.setVisibility(View.VISIBLE);
            holder.imageViewPlay.setVisibility(View.GONE);
        }
        else {
            holder.imageViewCheck.setVisibility(View.GONE);
            holder.imageViewPlay.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public VideoViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.video_item_list, viewGroup, false);
        return new VideoViewHolder(mainGroup);

    }


    /***
     * Methods required for do selections, remove selections, etc.
     */

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public ImageModel getItem(int i) {
        return (ImageModel) arrayList.get(i);
    }
    public void updateData(ArrayList<ImageModel> viewModels) {
        arrayList.clear();
        arrayList.addAll(viewModels);
        notifyDataSetChanged();
    }


}
