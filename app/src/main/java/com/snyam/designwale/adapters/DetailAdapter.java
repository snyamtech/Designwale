package com.snyam.designwale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.snyam.designwale.MyApplication;
import com.snyam.designwale.databinding.ItemPostBinding;
import com.snyam.designwale.databinding.ItemPostDetailBinding;
import com.snyam.designwale.items.PostItem;
import com.snyam.designwale.listener.ClickListener;
import com.snyam.designwale.utils.Util;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {

    Context context;
    ClickListener<Integer> listener;

    List<PostItem> postItemList;
    private int itemWidth = 0;
    int column;
    float width;

    public DetailAdapter(Context context, ClickListener<Integer> listener, int column, float width) {
        this.context = context;
        this.listener = listener;
        this.column = column;
        this.width = width;
        itemWidth = MyApplication.getColumnWidth(column, width);
    }

    public void setData(List<PostItem> postItemList) {
        this.postItemList = postItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostDetailBinding binding = ItemPostDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setPostdata(postItemList.get(position));
        holder.itemView.setOnClickListener(v -> {
            listener.onClick(position);
        });

        if (postItemList.get(position).is_video) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.cvBase.getLayoutParams();
            params.width = itemWidth;
            params.height = itemWidth;

            holder.binding.cvBase.requestLayout();
            holder.binding.cvBase.setLayoutParams(params);
        } else {
            float f = 1.0f;
            String width = postItemList.get(position).postWidth;
            String height = postItemList.get(position).postHeight;

            f = Float.parseFloat(height) / Float.parseFloat(width);

            Util.showLog("SS: " + f);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.cvBase.getLayoutParams();
            params.width = itemWidth;
            params.height = (int) (itemWidth * f);

            holder.binding.cvBase.requestLayout();
            holder.binding.cvBase.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        if (postItemList != null && postItemList.size() > 0) {
            return postItemList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemPostDetailBinding binding;

        public MyViewHolder(@NonNull ItemPostDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
