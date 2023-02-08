package com.snyam.designwale.ui.stickers.text;

import android.os.Parcel;
import android.os.Parcelable;

public class TextSTRInfo implements Parcelable {
    public static final Creator<TextSTRInfo> CREATOR = new Creator<TextSTRInfo>() {

        public TextSTRInfo createFromParcel(Parcel parcel) {
            return new TextSTRInfo(parcel);
        }

        public TextSTRInfo[] newArray(int i) {
            return new TextSTRInfo[i];
        }

    };
    private String font_family;
    private String text;
    private String text_id;
    private String txt_color;
    private String txt_height;
    private String txt_order;
    private String txt_rotation;
    private String txt_width;
    private String txt_x_pos;
    private String txt_y_pos;
    private String name;

    public TextSTRInfo() {
    }


    protected TextSTRInfo(Parcel parcel) {
        this.txt_height = parcel.readString();
        this.txt_width = parcel.readString();
        this.text = parcel.readString();
        this.txt_x_pos = parcel.readString();
        this.font_family = parcel.readString();
        this.txt_y_pos = parcel.readString();
        this.txt_order = parcel.readString();
        this.text_id = parcel.readString();
        this.txt_rotation = parcel.readString();
        this.txt_color = parcel.readString();
        this.name = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public String getTxt_height() {
        return this.txt_height;
    }

    public void setTxt_height(String str) {
        this.txt_height = str;
    }

    public String getTxt_width() {
        return this.txt_width;
    }

    public void setTxt_width(String str) {
        this.txt_width = str;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public String getTxt_x_pos() {
        return this.txt_x_pos;
    }

    public void setTxt_x_pos(String str) {
        this.txt_x_pos = str;
    }

    public String getFont_family() {
        return this.font_family;
    }

    public void setFont_family(String str) {
        this.font_family = str;
    }

    public String getTxt_y_pos() {
        return this.txt_y_pos;
    }

    public void setTxt_y_pos(String str) {
        this.txt_y_pos = str;
    }

    public String getTxt_order() {
        return this.txt_order;
    }

    public void setTxt_order(String str) {
        this.txt_order = str;
    }

    public String getText_id() {
        return this.text_id;
    }

    public void setText_id(String str) {
        this.text_id = str;
    }

    public String getTxt_rotation() {
        return this.txt_rotation;
    }

    public void setTxt_rotation(String str) {
        this.txt_rotation = str;
    }

    public String getTxt_color() {
        return this.txt_color;
    }

    public void setTxt_color(String str) {
        this.txt_color = str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TextSTRInfo{" +
                "font_family='" + font_family + '\'' +
                ", text='" + text + '\'' +
                ", text_id='" + text_id + '\'' +
                ", txt_color='" + txt_color + '\'' +
                ", txt_height='" + txt_height + '\'' +
                ", txt_order='" + txt_order + '\'' +
                ", txt_rotation='" + txt_rotation + '\'' +
                ", txt_width='" + txt_width + '\'' +
                ", txt_x_pos='" + txt_x_pos + '\'' +
                ", txt_y_pos='" + txt_y_pos + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.txt_height);
        parcel.writeString(this.txt_width);
        parcel.writeString(this.text);
        parcel.writeString(this.txt_x_pos);
        parcel.writeString(this.font_family);
        parcel.writeString(this.txt_y_pos);
        parcel.writeString(this.txt_order);
        parcel.writeString(this.text_id);
        parcel.writeString(this.txt_rotation);
        parcel.writeString(this.txt_color);
        parcel.writeString(this.name);
    }
}
