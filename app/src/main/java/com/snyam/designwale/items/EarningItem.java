package com.snyam.designwale.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "earning")
public class EarningItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("user")
    public String userName;

    @SerializedName("amount")
    public String amount;

    public EarningItem(int id, String userName, String amount) {
        this.id = id;
        this.userName = userName;
        this.amount = amount;
    }
}
