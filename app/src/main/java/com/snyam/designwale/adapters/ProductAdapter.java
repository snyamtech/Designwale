package com.snyam.designwale.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snyam.designwale.databinding.ItemProductBinding;
import com.snyam.designwale.items.ProductItem;
import com.snyam.designwale.ui.activities.ProductDetailActivity;

import java.io.Serializable;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    List<ProductItem> productItemList;
    Context context;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setData(productItemList.get(position));
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("PRODUCT", (Serializable) productItemList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (productItemList != null && productItemList.size() > 0) {
            return productItemList.size();
        }
        return 0;
    }

    public void setData(List<ProductItem> productItemList) {
        this.productItemList = productItemList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public MyViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
