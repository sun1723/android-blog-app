package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class ImgItem implements Parcelable {
    private String title;
    private int imageUrl;

    public ImgItem(String title, int imageUrl)
    {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    protected ImgItem(Parcel in) {
        title = in.readString();
        imageUrl = in.readInt();
    }

    public static final Creator<ImgItem> CREATOR = new Creator<ImgItem>() {
        @Override
        public ImgItem createFromParcel(Parcel in) {
            return new ImgItem(in);
        }

        @Override
        public ImgItem[] newArray(int size) {
            return new ImgItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public int getImageUrl()
    {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(imageUrl);
    }
}
