package com.snyam.designwale.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "refer_detail")
public class ReferDetail {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("referralCode")
    public String referralCode;

    @SerializedName("currentBalance")
    public String currentBalance;

    @SerializedName("totalBalance")
    public String totalBalance;

    @SerializedName("totalReferUser")
    public String totalReferUser;

    @SerializedName("totalSubscriptionUsingRefer")
    public String totalSubscriptionUsingRefer;

    @TypeConverters
    @SerializedName("earningHistory")
    public List<EarningItem> earningHistory;

    public ReferDetail(int id, String referralCode, String currentBalance, String totalBalance, String totalReferUser, String totalSubscriptionUsingRefer, List<EarningItem> earningHistory) {
        this.id = id;
        this.referralCode = referralCode;
        this.currentBalance = currentBalance;
        this.totalBalance = totalBalance;
        this.totalReferUser = totalReferUser;
        this.totalSubscriptionUsingRefer = totalSubscriptionUsingRefer;
        this.earningHistory = earningHistory;
    }

    @Override
    public String toString() {
        return "ReferDetail{" +
                "id=" + id +
                ", referralCode='" + referralCode + '\'' +
                ", currentBalance='" + currentBalance + '\'' +
                ", totalBalance='" + totalBalance + '\'' +
                ", totalReferUser='" + totalReferUser + '\'' +
                ", totalSubscriptionUsingRefer='" + totalSubscriptionUsingRefer + '\'' +
                ", earningHistory=" + earningHistory +
                '}';
    }
}
