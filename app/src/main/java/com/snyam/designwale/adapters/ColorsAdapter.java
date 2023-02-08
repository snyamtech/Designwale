package com.snyam.designwale.adapters;

import android.content.Context;
import android.graphics.Color;

import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snyam.designwale.R;
import com.snyam.designwale.listener.ClickListener;
import com.snyam.designwale.utils.Util;

import java.io.IOException;
import java.util.List;


public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.MyViewHolder> {

    private List<String> colors;
    public boolean isPattern = false;
    private Context mContext;
    private int screenWidth;
    ClickListener<String> itemClickListener;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageFilterView imgFilterView;

        public MyViewHolder(View view) {
            super(view);
            this.imgFilterView = (ImageFilterView) view.findViewById(R.id.ivID);
        }
    }

    public ColorsAdapter(Context context, List<String> strArr, boolean isPattern, ClickListener<String> itemClickListener) {
        this.mContext = context;
        this.isPattern = isPattern;
        this.colors = strArr;
        this.itemClickListener = itemClickListener;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_color_layout,
                viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int i) {
        if (isPattern) {
            try {
                holder.imgFilterView.setImageBitmap(Util.getBitmapFromAssets(mContext, "pattern/" + colors.get(i)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            holder.imgFilterView.setBackgroundColor(Color.parseColor(colors.get(i)));
        }
        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onClick(colors.get(i));
        });

    }

    public int getItemCount() {
        if (colors != null) {
            return colors.size();
        }
        return 0;
    }
}
