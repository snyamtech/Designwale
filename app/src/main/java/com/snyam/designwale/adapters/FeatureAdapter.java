package com.snyam.designwale.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snyam.designwale.MyApplication;
import com.snyam.designwale.R;
import com.snyam.designwale.databinding.ItemFeatureBinding;
import com.snyam.designwale.items.FeatureItem;
import com.snyam.designwale.items.PostItem;
import com.snyam.designwale.listener.ClickListener;
import com.snyam.designwale.ui.activities.DetailActivity;
import com.snyam.designwale.utils.Constant;

import java.util.List;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.MyViewHolder> {


    Context context;
    public List<FeatureItem> featureItemList;

    public FeatureAdapter(Context context) {
        this.context = context;
    }

    public void setFeatureItemList(List<FeatureItem> featureItemList) {
        this.featureItemList = featureItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeatureBinding binding = ItemFeatureBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (position % 2 == 0) {
            if (MyApplication.prefManager().getBoolean(Constant.IS_LOGIN)) {
                holder.binding.mainConstraint.setBackgroundColor(context.getColor(R.color.alpha_active_color));
            } else {
                holder.binding.mainConstraint.setBackgroundColor(context.getColor(R.color.transparent_color));
            }
        }

        holder.binding.txtViewTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Constant.INTENT_TYPE, featureItemList.get(position).type);
                intent.putExtra(Constant.INTENT_FEST_ID, featureItemList.get(position).festId);
                intent.putExtra(Constant.INTENT_FEST_NAME, featureItemList.get(position).title);
                intent.putExtra(Constant.INTENT_POST_IMAGE, "");
                intent.putExtra(Constant.INTENT_VIDEO, featureItemList.get(position).video);
                context.startActivity(intent);
            }
        });

        holder.adapters = new TrendingAdapter(context, new ClickListener<PostItem>() {
            @Override
            public void onClick(PostItem data) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Constant.INTENT_TYPE, data.type);
                intent.putExtra(Constant.INTENT_FEST_ID, data.fest_id);
                intent.putExtra(Constant.INTENT_FEST_NAME, featureItemList.get(position).title);
                intent.putExtra(Constant.INTENT_POST_IMAGE, data.image_url);
                intent.putExtra(Constant.INTENT_POST_ITEM, data);
                intent.putExtra(Constant.INTENT_VIDEO, featureItemList.get(position).video);
                context.startActivity(intent);
            }
        });
        holder.binding.rvFeature.setAdapter(holder.adapters);

        holder.binding.tvFeature.setText(featureItemList.get(position).title);
        holder.adapters.setTrending(featureItemList.get(position).postItemList);

    }

    @Override
    public int getItemCount() {
        if (featureItemList != null && featureItemList.size() > 0) {
            return featureItemList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemFeatureBinding binding;
        TrendingAdapter adapters;

        public MyViewHolder(@NonNull ItemFeatureBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
