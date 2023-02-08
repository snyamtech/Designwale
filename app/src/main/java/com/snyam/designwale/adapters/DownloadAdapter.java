package com.snyam.designwale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.snyam.designwale.MyApplication;
import com.snyam.designwale.R;
import com.snyam.designwale.items.DownloadItem;
import com.snyam.designwale.listener.ClickListener;

import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.MyViewHolder> {

    Context context;
    List<DownloadItem> uriList;
    private int itemWidth = 0;
    ClickListener<DownloadItem> listener;

    public DownloadAdapter(Context context, List<DownloadItem> uriList, ClickListener<DownloadItem> listener) {
        this.context = context;
        this.uriList = uriList;
        this.listener = listener;
        try {

            itemWidth = MyApplication.getColumnWidth(3, context.getResources().getDimension(com.intuit.ssp.R.dimen._2ssp));

        } catch (Exception e) {

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//

        if (uriList.get(position).isVideo) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.cv_base.getLayoutParams();
            params.height = itemWidth;
            params.width = itemWidth;
            holder.cv_base.setLayoutParams(params);
            Glide.with(context).load(uriList.get(position).uri.getPath()).into(holder.iv_image);
            holder.iv_play_video.setVisibility(View.VISIBLE);
        } else {

//            Glide.with(context).load(uriList.get(position).uri).into(holder.iv_image);

            if (uriList.get(position).bitmap != null) {
                int imageHeight = uriList.get(position).bitmap.getHeight();
                int imageWidth = uriList.get(position).bitmap.getWidth();
                float ratio = (float) imageWidth / (float) imageHeight;

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.cv_base.getLayoutParams();
                params.height = (int) (itemWidth / ratio);
                params.width = itemWidth;
                holder.cv_base.setLayoutParams(params);

                holder.iv_image.setImageBitmap(uriList.get(position).bitmap);
            }
           /* Glide.with(context).asBitmap().load(uriList.get(position).uri).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
*/
            holder.iv_play_video.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            listener.onClick(uriList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cv_base;
        ImageView iv_image, iv_play_video;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cv_base = itemView.findViewById(R.id.cv_base);
            iv_image = itemView.findViewById(R.id.iv_post);
            iv_play_video = itemView.findViewById(R.id.iv_play_video);
        }
    }
}
