package com.snyam.designwale.items;

import com.google.gson.annotations.SerializedName;

public class CashFreeOrder {

    @SerializedName("cf_order_id")
    public String cf_order_id;

    @SerializedName("order_id")
    public String order_id;

    @SerializedName("payment_session_id")
    public String payment_session_id;

    public CashFreeOrder(String cf_order_id, String order_id, String payment_session_id) {
        this.cf_order_id = cf_order_id;
        this.order_id = order_id;
        this.payment_session_id = payment_session_id;
    }

    @Override
    public String toString() {
        return "CashFreeOrder{" +
                "cf_order_id='" + cf_order_id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", payment_session_id='" + payment_session_id + '\'' +
                '}';
    }
}
