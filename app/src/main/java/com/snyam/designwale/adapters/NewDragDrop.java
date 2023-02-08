package com.snyam.designwale.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.util.Pair;

import com.snyam.designwale.R;
import com.snyam.designwale.listener.StartDragListener;
import com.snyam.designwale.ui.stickers.RelStickerView;
import com.snyam.designwale.ui.stickers.text.AutofitTextRel;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

public class NewDragDrop extends DragItemAdapter<Pair<Long, View>, NewDragDrop.ViewHolder>{

    public boolean mDragOnLongPress;
    public int mGrabHandleId;
    private int mLayoutId;
    public Activity activity;
    StartDragListener startDragListener;

    public NewDragDrop(Activity activity2, ArrayList<Pair<Long, View>> arrayList,
                              int i, int i2, boolean z, StartDragListener startDragListener) {
        this.mLayoutId = i;
        this.mGrabHandleId = i2;
        this.activity = activity2;
        this.mDragOnLongPress = z;
        this.startDragListener = startDragListener;
        setItemList(arrayList);
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        public TextView mTitle;
        View rowView;
        ImageView ivDrag, iv_close, iv_hide, iv_main, iv_lock;

        public void onItemClicked(View view) {
        }

        public boolean onItemLongClicked(View view) {
            return true;
        }

        ViewHolder(View view) {
            super(view, mGrabHandleId, mDragOnLongPress);
            rowView = itemView;
            mTitle = itemView.findViewById(R.id.tvText);
            ivDrag = itemView.findViewById(R.id.ivDrag);
            iv_close = itemView.findViewById(R.id.ivRemove);
            iv_main = itemView.findViewById(R.id.ivPosterImg);
            iv_lock = itemView.findViewById(R.id.ivLock);
            iv_hide = itemView.findViewById(R.id.ivVisibility);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(this.mLayoutId, viewGroup, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        View view = (View) ((Pair) this.mItemList.get(position)).second;

        if (view instanceof AutofitTextRel) {
            holder.iv_main.setVisibility(View.GONE);
            holder.mTitle.setText(((AutofitTextRel) view).getText().toString());
            if (((AutofitTextRel) view).getVisibility() == View.VISIBLE) {
                holder.iv_hide.setImageResource(R.drawable.s_visible);
            } else {
                holder.iv_hide.setImageResource(R.drawable.s_hide);
            }
            if (((AutofitTextRel) view).isMultiTouchEnabled) {
                holder.iv_lock.setImageResource(R.drawable.s_unlock);
            } else {
                holder.iv_lock.setImageResource(R.drawable.s_lock);
            }
        }
        if (view instanceof RelStickerView) {
            holder.mTitle.setVisibility(View.GONE);
            holder.iv_main.setImageBitmap(((RelStickerView) view).getMainImageBitmap());
            if (((RelStickerView) view).getVisibility() == View.VISIBLE) {
                holder.iv_hide.setImageResource(R.drawable.s_visible);
            } else {
                holder.iv_hide.setImageResource(R.drawable.s_hide);
            }
            if (((RelStickerView) view).isMultiTouchEnabled) {
                holder.iv_lock.setImageResource(R.drawable.s_unlock);
            } else {
                holder.iv_lock.setImageResource(R.drawable.s_lock);
            }
        }

        holder.iv_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view instanceof RelStickerView) {
                    if (((RelStickerView) view).isMultiTouchEnabled) {
                        ((RelStickerView) view).isMultiTouchEnabled = ((RelStickerView) view).setDefaultTouchListener(false);
                        holder.iv_lock.setImageResource(R.drawable.s_lock);
                        if(((RelStickerView) view).getBorderVisbilty()) {
                            ((RelStickerView) view).setBorderVisibility(false);
                        }
                    } else {
                        ((RelStickerView) view).isMultiTouchEnabled = ((RelStickerView) view).setDefaultTouchListener(true);
                        holder.iv_lock.setImageResource(R.drawable.s_unlock);
                    }
                }
                if (view instanceof AutofitTextRel){
                    if (((AutofitTextRel) view).isMultiTouchEnabled) {
                        ((AutofitTextRel) view).isMultiTouchEnabled = ((AutofitTextRel) view).setDefaultTouchListener(false);
                        holder.iv_lock.setImageResource(R.drawable.s_lock);
                        if(((AutofitTextRel) view).getBorderVisibility()) {
                            ((AutofitTextRel) view).setBorderVisibility(false);
                        }
                    } else {
                        ((AutofitTextRel) view).isMultiTouchEnabled = ((AutofitTextRel) view).setDefaultTouchListener(true);
                        holder.iv_lock.setImageResource(R.drawable.s_unlock);
                    }
                }
            }
        });

        holder.iv_close.setOnClickListener(v -> {
            startDragListener.onDelete(position);
        });

        holder.iv_hide.setOnClickListener(v -> {
            if (view instanceof RelStickerView) {
                if (((RelStickerView) view).getVisibility() == View.VISIBLE) {
                    ((RelStickerView) view).setVisibility(View.GONE);
                    holder.iv_hide.setImageResource(R.drawable.s_hide);
                    if(((RelStickerView) view).getBorderVisbilty()) {
                        ((RelStickerView) view).setBorderVisibility(false);
                    }
                } else {
                    ((RelStickerView) view).setVisibility(View.VISIBLE);
                    holder.iv_hide.setImageResource(R.drawable.s_visible);
                }
            }
            if (view instanceof AutofitTextRel){
                if (((AutofitTextRel) view).getVisibility() == View.VISIBLE) {
                    ((AutofitTextRel) view).setVisibility(View.GONE);
                    holder.iv_hide.setImageResource(R.drawable.s_hide);
                    if(((AutofitTextRel) view).getBorderVisibility()) {
                        ((AutofitTextRel) view).setBorderVisibility(false);
                    }
                } else {
                    ((AutofitTextRel) view).setVisibility(View.VISIBLE);
                    holder.iv_hide.setImageResource(R.drawable.s_visible);
                }
            }
        });

    }

    public long getUniqueItemId(int i) {
        return ((Long) ((Pair) this.mItemList.get(i)).first).longValue();
    }
}