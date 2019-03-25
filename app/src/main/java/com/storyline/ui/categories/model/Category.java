package com.storyline.ui.categories.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Category implements Parcelable {

    private String categoryName;
    private int categoryImageId;
    private List<String> categoryLines;

    public Category(String categoryName, int categoryImageId, List<String> categoryLines) {
        this.categoryName = categoryName;
        this.categoryImageId = categoryImageId;
        this.categoryLines = categoryLines;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryImageId() {
        return categoryImageId;
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
        dest.writeInt(this.categoryImageId);
        dest.writeStringList(this.categoryLines);
    }

    protected Category(Parcel in) {
        this.categoryName = in.readString();
        this.categoryImageId = in.readInt();
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
