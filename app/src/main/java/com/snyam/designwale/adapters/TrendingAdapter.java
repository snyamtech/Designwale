package com.snyam.designwale.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.snyam.designwale.MyApplication;
import com.snyam.designwale.databinding.ItemPostBinding;
import com.snyam.designwale.items.PostItem;
import com.snyam.designwale.listener.ClickListener;
import com.snyam.designwale.utils.Util;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.MyViewHolder> {

    public Context context;
    public List<PostItem> postItemList;
    private int itemWidth = 0;
    ClickListener<PostItem> listener;
    private int screenWidth = 0;

    public TrendingAdapter(Context context, ClickListener<PostItem> listener) {
        this.context = context;
        this.listener = listener;
        itemWidth = MyApplication.getColumnWidth(2, context.getResources().getDimension(com.intuit.ssp.R.dimen._15ssp));

        Display defaultDisplay = MyApplication.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        screenWidth = point.x;
        int i = screenWidth;
        screenWidth = (i / 5) + (i/6);
    }

    public void setTrending(List<PostItem> postItemList) {
        this.postItemList = postItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.setPostdata(postItemList.get(position));

        float f = 1.0f;
        String width = postItemList.get(position).postWidth;
        String height = postItemList.get(position).postHeight;

        f = Float.parseFloat(height) / Float.parseFloat(width);

        int i2 = screenWidth;
        int i3 = Math.round((1.0f / f) * ((float) screenWidth));

        Util.showLog("width: " + i2 + " height: " + i3);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(i3, i2);
        holder.binding.cvBase.requestLayout();

//        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) ((MyViewHolder) holder).binding.cvBase.getLayoutParams();
//        params.width = itemWidth;
//        params.height = itemWidth;

        holder.binding.cvBase.setLayoutParams(params);

        holder.itemView.setOnClickListener(v -> {
            listener.onClick(postItemList.get(position));
        });
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
        ItemPostBinding binding;

        public MyViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
