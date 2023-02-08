package com.snyam.designwale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.snyam.designwale.R;
import com.snyam.designwale.binding.GlideBinding;
import com.snyam.designwale.items.StoryItem;
import com.snyam.designwale.listener.ClickListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class HomeBannerAdapter extends LoopingPagerAdapter {


    public Context context;
    public List<StoryItem> storyItemList;
    public ClickListener<StoryItem> listener;

    public HomeBannerAdapter(Context context, List<StoryItem> storyItemList, ClickListener<StoryItem> listener) {
        super(storyItemList, true);
        this.context = context;
        this.storyItemList = storyItemList;
        this.listener = listener;
    }

    @Override
    protected void bindView(@NonNull View view, int i, int i1) {

        if(storyItemList!=null && storyItemList.size()>0){
            RoundedImageView iv_banner;

            iv_banner = view.findViewById(R.id.iv_home_banner);

            GlideBinding.bindImage(iv_banner, storyItemList.get(i).imageUrl);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(storyItemList.get(i));
                }
            });

            view.setTag("" + i);
        }

    }

    @Override
    protected View inflateView(int i, @NonNull ViewGroup viewGroup, int i1) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_home_banner, viewGroup, false);
    }


}
