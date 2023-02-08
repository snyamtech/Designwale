package com.snyam.designwale.adapters;

import android.content.Context;

import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snyam.designwale.R;
import com.snyam.designwale.items.ItemGradient;
import com.snyam.designwale.listener.ClickListener;
import com.snyam.designwale.utils.Util;

import java.util.List;

public class GradientAdapter extends RecyclerView.Adapter<GradientAdapter.ViewHolderCollagePattern> {

    private String gradientType;
    private List<ItemGradient> gradients;
    private String linearDirection;
    private Context mContext;
    String[] gradient;
    ClickListener<String[]> itemClickListener;

    static class ViewHolderCollagePattern extends RecyclerView.ViewHolder {
        ImageFilterView imageFilterView;

        public ViewHolderCollagePattern(View view) {
            super(view);
            this.imageFilterView = view.findViewById(R.id.ivID);
        }
    }

    public GradientAdapter(Context context, ClickListener<String[]> clickListener) {
        this.mContext = context;
        this.itemClickListener = clickListener;
    }

    public void setData(List<ItemGradient> gradients, String gradientType, String direction) {
        this.gradients = gradients;
        this.gradientType = gradientType;
        this.linearDirection = direction;
        notifyDataSetChanged();
    }

    public ViewHolderCollagePattern onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolderCollagePattern(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_gradient_layout, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolderCollagePattern viewHolderCollagePattern, final int i) {
        gradient = new String[]{gradients.get(i).startColor.toString(), gradients.get(i).endColor.toString()};
        viewHolderCollagePattern.imageFilterView.setBackgroundDrawable(Util.generateViewGradient(gradient,
                this.gradientType, this.linearDirection, viewHolderCollagePattern.imageFilterView.getWidth(),
                viewHolderCollagePattern.imageFilterView.getHeight()));
        viewHolderCollagePattern.itemView.setOnClickListener(v -> {
//            gradient = Util.strTOStrArray(gradients[i], " ");
            gradient = new String[]{gradients.get(i).startColor, gradients.get(i).endColor};
            itemClickListener.onClick(gradient);
        });
    }

    public int getItemCount() {
        if (gradients != null) {
            return gradients.size();
        }
        return 0;
    }
}
