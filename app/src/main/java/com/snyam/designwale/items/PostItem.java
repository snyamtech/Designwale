package com.snyam.designwale.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "posts", primaryKeys = "postId")
public class PostItem implements Serializable {

    @NonNull
    @SerializedName("postId")
    public String postId;
    @SerializedName("id")
    public String fest_id;

    @SerializedName("type")
    public String type;

    @SerializedName("image")
    public String image_url;

    @SerializedName("language")
    public String language;

    @SerializedName("is_paid")
    public boolean is_premium;

    public boolean is_trending;

    @SerializedName("video")
    public boolean is_video = false;

    @SerializedName("width")
    public String postWidth;

    @SerializedName("height")
    public String postHeight;

    @SerializedName("aspect_ratio")
    public String aspectRatio;

    @SerializedName("business_sub_category")
    public String business_sub_category;

    public int page = 0;

    public PostItem(@NonNull String postId, String fest_id, String type, String image_url, String language, boolean is_premium, boolean is_trending, boolean is_video, String postWidth, String postHeight, String aspectRatio, String business_sub_category, int page) {
        this.postId = postId;
        this.fest_id = fest_id;
        this.type = type;
        this.image_url = image_url;
        this.language = language;
        this.is_premium = is_premium;
        this.is_trending = is_trending;
        this.is_video = is_video;
        this.postWidth = postWidth;
        this.postHeight = postHeight;
        this.aspectRatio = aspectRatio;
        this.business_sub_category = business_sub_category;
        this.page = page;
    }

    @Override
    public String toString() {
        return "PostItem{" +
                "postId='" + postId + '\'' +
                ", fest_id='" + fest_id + '\'' +
                ", type='" + type + '\'' +
                ", image_url='" + image_url + '\'' +
                ", language='" + language + '\'' +
                ", is_premium=" + is_premium +
                ", is_trending=" + is_trending +
                ", is_video=" + is_video +
                ", postWidth='" + postWidth + '\'' +
                ", postHeight='" + postHeight + '\'' +
                ", aspectRatio='" + aspectRatio + '\'' +
                ", business_sub_category='" + business_sub_category + '\'' +
                ", page=" + page +
                '}';
    }
}
