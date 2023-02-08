package com.snyam.designwale.items;

import android.graphics.Bitmap;
import android.net.Uri;

public class DownloadItem {

    public Uri uri;
    public Bitmap bitmap;
    public boolean isVideo;

    public DownloadItem(Uri uri, Bitmap bitmap, boolean isVideo) {
        this.uri = uri;
        this.bitmap = bitmap;
        this.isVideo = isVideo;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}
