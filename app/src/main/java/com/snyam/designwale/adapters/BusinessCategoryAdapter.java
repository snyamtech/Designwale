package com.snyam.designwale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snyam.designwale.Config;
import com.snyam.designwale.MyApplication;
import com.snyam.designwale.R;
import com.snyam.designwale.databinding.ItemCustomCategoryBinding;
import com.snyam.designwale.databinding.ItemSpinnerBinding;
import com.snyam.designwale.items.BusinessCategoryItem;
import com.snyam.designwale.items.CustomCategory;
import com.snyam.designwale.listener.ClickListener;

import java.util.List;

public class BusinessCategoryAdapter extends RecyclerView.Adapter {

    public Context context;
    public ClickListener<BusinessCategoryItem> listener;
    public List<BusinessCategoryItem> CustomCategoryList;
    boolean isHome;
    boolean isSelectable;
    int itemWidth;

    public BusinessCategoryAdapter(Context context, ClickListener<BusinessCategoryItem> listener, boolean isHome, boolean isSelectable) {
        this.context = context;
        this.listener = listener;
        this.isHome = isHome;
        this.isSelectable = isSelectable;
        itemWidth = MyApplication.getColumnWidth(3, context.getResources().getDimension(com.intuit.ssp.R.dimen._3ssp));
    }

    public void setCategories(List<BusinessCategoryItem> categories) {
        this.CustomCategoryList = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (!isSelectable) {
            ItemCustomCategoryBinding binding = ItemCustomCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyViewHolder(binding);
        } else {
            ItemSpinnerBinding binding2 = ItemSpinnerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MySpinnerViewHolder(binding2);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).binding.setCategoryData(new CustomCategory(CustomCategoryList.get(position).businessCategoryId,
                    CustomCategoryList.get(position).businessCategoryName, CustomCategoryList.get(position).businessCategoryIcon));

            int index = 0;
            for (int i = 0; i < position; i++) {
                index++;
                if (index == 10) {
                    index = 0;
                }
            }
            String[] colorsTxt = context.getResources().getStringArray(R.array.cat_colors);

            //  holder.binding.catCard.setCardBackgroundColor(Color.parseColor(colorsTxt[index]));

            holder.itemView.setOnClickListener(v -> {
                listener.onClick(CustomCategoryList.get(position));
            });
        } else {
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((MySpinnerViewHolder) holder).binding.lvCate.getLayoutParams();
//            params.height = itemWidth;
//            params.width = itemWidth;
//            ((MySpinnerViewHolder) holder).binding.lvCate.setLayoutParams(params);

            ((MySpinnerViewHolder) holder).binding.setData(CustomCategoryList.get(position));

            int index = 0;
            for (int i = 0; i < position; i++) {
                index++;
                if (index == 10) {
                    index = 0;
                }
            }
            String[] colorsTxt = context.getResources().getStringArray(R.array.cat_colors);

            //  holder.binding.catCard.setCardBackgroundColor(Color.parseColor(colorsTxt[index]));

            holder.itemView.setOnClickListener(v -> {
                listener.onClick(CustomCategoryList.get(position));
            });
        }
    }

    @Override
    public int getItemCount() {
        if (CustomCategoryList != null && CustomCategoryList.size() > 0) {
            if (CustomCategoryList.size() > Config.HOME_BUSINESS_CATEGORY_SHOW && isHome) {
                return Config.HOME_BUSINESS_CATEGORY_SHOW;
            } else {
                return CustomCategoryList.size();
            }
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCustomCategoryBinding binding;

        public MyViewHolder(@NonNull ItemCustomCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class MySpinnerViewHolder extends RecyclerView.ViewHolder {

        ItemSpinnerBinding binding;

        public MySpinnerViewHolder(ItemSpinnerBinding binding2) {
            super(binding2.getRoot());
            this.binding = binding2;
        }
    }
}
