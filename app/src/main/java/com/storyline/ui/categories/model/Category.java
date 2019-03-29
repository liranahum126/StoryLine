package com.storyline.ui.categories.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Category implements Parcelable {

    private String categoryName;
    private int background;
    private int logo;
    private int avatar;
    private List<String> categoryLines;

    public Category(String categoryName, int background, int logo, int avatar, List<String> categoryLines) {
        this.categoryName = categoryName;
        this.background = background;
        this.logo = logo;
        this.avatar = avatar;
        this.categoryLines = categoryLines;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getBackground() {
        return background;
    }

    public int getLogo() {
        return logo;
    }

    public int getAvatar() {
        return avatar;
    }

    public List<String> getCategoryLines() {
        return categoryLines;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryName);
        dest.writeInt(this.background);
        dest.writeInt(this.logo);
        dest.writeInt(this.avatar);
        dest.writeStringList(this.categoryLines);
    }

    protected Category(Parcel in) {
        this.categoryName = in.readString();
        this.background = in.readInt();
        this.logo = in.readInt();
        this.avatar = in.readInt();
        this.categoryLines = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
