package com.snyam.designwale.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.snyam.designwale.R;
import com.snyam.designwale.listener.ItemMoveCallback;
import com.snyam.designwale.listener.StartDragListener;
import com.snyam.designwale.ui.stickers.RelStickerView;
import com.snyam.designwale.ui.stickers.text.AutofitTextRel;

import java.util.ArrayList;
import java.util.Collections;

public class LayersAdapter extends RecyclerView.Adapter<LayersAdapter.MyViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract {

    public ArrayList<Pair<Long, View>> mItemArray = new ArrayList<>();

    private final StartDragListener mStartDragListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        View rowView;
        ImageView ivDrag, iv_close, iv_hide, iv_main, iv_lock;

        public MyViewHolder(View itemView) {
            super(itemView);

            rowView = itemView;
            mTitle = itemView.findViewById(R.id.tvText);
            ivDrag = itemView.findViewById(R.id.ivDrag);
            iv_close = itemView.findViewById(R.id.ivRemove);
            iv_main = itemView.findViewById(R.id.ivPosterImg);
            iv_lock = itemView.findViewById(R.id.ivLock);
            iv_hide = itemView.findViewById(R.id.ivVisibility);
        }
    }

    public LayersAdapter(ArrayList<Pair<Long, View>> data, StartDragListener startDragListener) {
        mStartDragListener = startDragListener;
        this.mItemArray = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        View view = (View) ((Pair) this.mItemArray.get(position)).second;

        if (view instanceof AutofitTextRel) {
            holder.iv_main.setVisibility(View.GONE);
            holder.mTitle.setText(((AutofitTextRel) view).getText().toString());
            if (((AutofitTextRel) view).getVisibility() == View.VISIBLE) {
                holder.iv_hide.setImageResource(R.drawable.s_visible);
            } else {
                holder.iv_hide.setImageResource(R.drawable.s_hide);
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
           mStartDragListener.onDelete(position);
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

        holder.ivDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    mStartDragListener.requestDrag(holder);
                }
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return mItemArray.size();
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        /*if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mItemArray, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mItemArray, i, i - 1);
            }
        }*/
        Collections.swap(mItemArray, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);
        mStartDragListener.onDragEnd(myViewHolder);
    }
}
