package com.snyam.designwale.items;

import com.google.gson.annotations.SerializedName;

public class ItemGradient {

    @SerializedName("gStart")
    public String startColor = "";

    @SerializedName("gEnd")
    public String endColor = "";

    public ItemGradient(String startColor, String endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }
}
