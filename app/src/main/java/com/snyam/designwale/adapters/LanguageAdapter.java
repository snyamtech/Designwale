package com.snyam.designwale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snyam.designwale.R;
import com.snyam.designwale.databinding.ItemLanguageBinding;
import com.snyam.designwale.databinding.ItemLanguageSubBinding;
import com.snyam.designwale.items.LanguageItem;
import com.snyam.designwale.listener.ClickListener;

import java.util.ArrayList;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {

    public Context context;
    public ClickListener<LanguageItem> listener;

    public List<LanguageItem> languageItemList;
    int selected = 0;

    public LanguageAdapter(Context context, ClickListener<LanguageItem> listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setLanguageItemList(List<LanguageItem> languageItemList) {
        this.languageItemList = languageItemList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLanguageSubBinding binding = ItemLanguageSubBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.setCategoryData(languageItemList.get(position));

        if(selected == position) {
            holder.binding.lvText.setBackgroundResource(R.drawable.langauage_active);
            holder.binding.txtCategory.setTextColor(context.getColor(R.color.al_white));
        }else {
            holder.binding.lvText.setBackgroundResource(R.drawable.langauage_simple);
            holder.binding.txtCategory.setTextColor(context.getColor(R.color.black));
        }

        holder.itemView.setOnClickListener(v->{
            setSelected(position);
            listener.onClick(languageItemList.get(position));
        });
    }

    private void setSelected(int position) {
        int oldPos = selected;
        selected = position;
        notifyItemChanged(oldPos);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        if (languageItemList != null && languageItemList.size() > 0) {
            return languageItemList.size();
        } else {
            return 0;
        }
    }

    public List<LanguageItem> getSelectedItems() {

        List<LanguageItem> selectedItems = new ArrayList<>();
        if (languageItemList!=null) {
            for (LanguageItem item : languageItemList) {
                if (item.isChecked) {
                    selectedItems.add(item);
                }
            }
        }
        return selectedItems;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemLanguageSubBinding binding;

        public MyViewHolder(ItemLanguageSubBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
