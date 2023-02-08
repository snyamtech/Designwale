package com.snyam.designwale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.snyam.designwale.MyApplication;
import com.snyam.designwale.databinding.ItemDynamicFrameBinding;
import com.snyam.designwale.items.DynamicFrameItem;
import com.snyam.designwale.listener.ClickListener;
import com.snyam.designwale.utils.Util;

import java.util.List;

public class DynamicFrameAdapter extends RecyclerView.Adapter<DynamicFrameAdapter.MyViewHolder> {

    public Context context;
    public List<DynamicFrameItem> frameItemList;
    public ClickListener<DynamicFrameItem> listener;
    private int selectedPos = 0;
    private int screenWidth = 0;
    float ratio = 0;

    public DynamicFrameAdapter(Context context, ClickListener<DynamicFrameItem> listener, float ratio) {
        this.context = context;
        this.listener = listener;
        this.ratio = ratio;
//        Display defaultDisplay = MyApplication.getDefaultDisplay();
//        Point point = new Point();
//        defaultDisplay.getSize(point);
//        screenWidth = point.x;
//        int i = screenWidth;
//        screenWidth = (i / 4);
        screenWidth = MyApplication.getColumnWidth(4, context.getResources().getDimension(com.intuit.ssp.R.dimen._4ssp));
    }

    public void setFrameItemList(List<DynamicFrameItem> frameItemList) {
        this.frameItemList = frameItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDynamicFrameBinding binding = ItemDynamicFrameBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Util.showLog("SSS: " + ratio + " " + (int)(screenWidth*ratio));
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.cvBase.getLayoutParams();
        params.width = (int) (screenWidth*ratio);
        params.height = screenWidth;
        holder.binding.cvBase.requestLayout();
        holder.binding.cvBase.setLayoutParams(params);
        holder.binding.setFrameData(frameItemList.get(position));
        holder.itemView.setOnClickListener(v -> {
            listener.onClick(frameItemList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (frameItemList != null && frameItemList.size() > 0) {
            return frameItemList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemDynamicFrameBinding binding;

        public MyViewHolder(ItemDynamicFrameBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
