package com.snyam.designwale.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "personal")
public class PersonalItem {

    @PrimaryKey(autoGenerate = true)
    public int pId;

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("type")
    public String type;

    public PersonalItem(int pId, String id, String name, String type) {
        this.pId = pId;
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
