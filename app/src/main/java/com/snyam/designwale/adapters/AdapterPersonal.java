package com.snyam.designwale.adapters;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snyam.designwale.R;
import com.snyam.designwale.databinding.ItemPersonalBinding;
import com.snyam.designwale.items.PersonalItem;
import com.snyam.designwale.listener.ClickListener;

import java.util.List;

public class AdapterPersonal extends RecyclerView.Adapter<AdapterPersonal.MyViewHolder>{

    List<PersonalItem> personalItemList;
    ClickListener<PersonalItem> clickListener;
    private int selectedPos = 0;
    Context context;

    public AdapterPersonal(Context context, ClickListener<PersonalItem> clickListener) {
        this.clickListener = clickListener;
        this.context = context;
    }

    public void setData(List<PersonalItem> personalItemList){
        this.personalItemList = personalItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPersonalBinding binding = ItemPersonalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.setCategoryData(personalItemList.get(position));
        holder.itemView.setOnClickListener(v -> {
            setSelected(position);
            clickListener.onClick(personalItemList.get(position));
        });
        if (position == selectedPos) {
            holder.binding.txtCategory.setTextColor(Color.BLACK);
            holder.binding.lvText.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        } else {
            holder.binding.txtCategory.setTextColor(Color.WHITE);
            holder.binding.lvText.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.blue_grey_900)));
        }
    }

    public void setSelected(int pos) {
        int oldPos = selectedPos;
        selectedPos = pos;
        notifyItemChanged(oldPos);
        notifyItemChanged(pos);
    }

    @Override
    public int getItemCount() {
        if(personalItemList!=null){
            return personalItemList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ItemPersonalBinding binding;
        public MyViewHolder(@NonNull ItemPersonalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
