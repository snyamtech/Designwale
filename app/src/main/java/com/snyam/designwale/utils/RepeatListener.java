package com.snyam.designwale.utils;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class RepeatListener implements View.OnTouchListener {

    public final View.OnClickListener clickListener;
    public final int normalInterval;
    public View downView;
    public Handler handler = new Handler();
    private Runnable handlerRunnable = new Runnable() {
        public void run() {
            RepeatListener.this.handler.postDelayed(this, (long) RepeatListener.this.normalInterval);
            RepeatListener.this.clickListener.onClick(RepeatListener.this.downView);
        }
    };
    private int initialInterval;

    public RepeatListener(int i, int i2, View.OnClickListener onClickListener) {
        if (onClickListener == null) {
            throw new IllegalArgumentException("null runnable");
        } else if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("negative interval");
        } else {
            this.initialInterval = i;
            this.normalInterval = i2;
            this.clickListener = onClickListener;
        }
    }


    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
            } else if (action != 3) {
                return false;
            }
            this.handler.removeCallbacks(this.handlerRunnable);
            this.downView.setPressed(false);
            this.downView = null;
            return true;
        }


        this.handler.removeCallbacks(this.handlerRunnable);
        this.handler.postDelayed(this.handlerRunnable, (long) this.initialInterval);
        this.downView = view;
        this.downView.setPressed(true);
        this.clickListener.onClick(view);
        return true;
    }
}
