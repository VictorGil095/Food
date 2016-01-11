package com.example.victor.food;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryModel implements Parcelable{
    private String namecategory;
    private int id;

    protected CategoryModel(Parcel in) {
        namecategory = in.readString();
        id = in.readInt();
    }

    public CategoryModel() {
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamecategory() {
        return namecategory;
    }

    public void setNamecategory(String namecategory) {
        this.namecategory = namecategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(namecategory);
        dest.writeInt(id);
    }
}