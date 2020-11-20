package com.example.booksapp;

import android.os.Parcel;
import android.os.Parcelable;

//Objek dibungkus dalam bentuk parsel agar bisa dikirim lewat intent
public class ItemData implements Parcelable {
    public String itemTitle;
    public String itemAuthor;
    public String itemImage;
    public String itemDescription;

    public ItemData(){

    }

    protected ItemData(Parcel in) {
        itemTitle = in.readString();
        itemAuthor = in.readString();
        itemImage = in.readString();
        itemDescription = in.readString();
    }

    public static final Creator<ItemData> CREATOR = new Creator<ItemData>() {
        @Override
        public ItemData createFromParcel(Parcel in) {
            return new ItemData(in);
        }

        @Override
        public ItemData[] newArray(int size) {
            return new ItemData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemTitle);
        dest.writeString(itemAuthor);
        dest.writeString(itemImage);
        dest.writeString(itemDescription);
    }
}
