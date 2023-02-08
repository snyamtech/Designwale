package com.snyam.designwale.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snyam.designwale.R;
import com.snyam.designwale.items.EarningItem;

import java.util.List;

public class EarningAdapter extends RecyclerView.Adapter<EarningAdapter.MyViewHolder> {

    Context context;
    List<EarningItem> earningItemList;

    public EarningAdapter(Context context, List<EarningItem> earningItemList) {
        this.context = context;
        this.earningItemList = earningItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_earning, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvName.setText(earningItemList.get(position).userName);
        holder.tvAmount.setText("₹ " + earningItemList.get(position).amount);

        if(earningItemList.get(position).amount.contains("+")){
            holder.tvAmount.setTextColor(ColorStateList.valueOf(context.getColor(R.color.green_800)));
        }else {
            holder.tvAmount.setTextColor(ColorStateList.valueOf(context.getColor(R.color.red_800)));
        }

    }

    @Override
    public int getItemCount() {
        return earningItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAmount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvAmount = itemView.findViewById(R.id.tv_amount);
        }
    }
}
