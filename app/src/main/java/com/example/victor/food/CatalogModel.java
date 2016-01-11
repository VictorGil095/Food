package com.example.victor.food;

import android.os.Parcel;
import android.os.Parcelable;

public class CatalogModel implements Parcelable{
    private String mIcon;
    private String mName;
    private String mWeight;
    private String mPrice;
    private String mDescription;
    private int mId;
    private int mCategoryId;

    /*public CatalogModel(int id, String name, String price, String description, String icon, int categoryId, String weight) {
        mId = id;
        mName = name;
        mPrice = price;
        mDescription = description;
        mIcon = icon;
        mCategoryId = categoryId;
        mWeight = weight;
    }*/

    public CatalogModel(String name, String price, String description, String icon, int categoryId, String weight) {
        mName = name;
        mPrice = price;
        mDescription = description;
        mIcon = icon;
        mCategoryId = categoryId;
        mWeight = weight;
    }

    public CatalogModel(String name, String price, String description, String icon, int categoryId) {
        mName = name;
        mPrice = price;
        mDescription = description;
        mIcon = icon;
        mCategoryId = categoryId;
    }

    public CatalogModel() {
    }

    protected CatalogModel(Parcel in) {
        mIcon = in.readString();
        mName = in.readString();
        mWeight = in.readString();
        mPrice = in.readString();
        mDescription = in.readString();
        mId = in.readInt();
        mCategoryId = in.readInt();
    }

    public static final Creator<CatalogModel> CREATOR = new Creator<CatalogModel>() {
        @Override
        public CatalogModel createFromParcel(Parcel in) {
            return new CatalogModel(in);
        }

        @Override
        public CatalogModel[] newArray(int size) {
            return new CatalogModel[size];
        }
    };

    /*public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }*/


    public int getMcategoryId() {
        return mCategoryId;
    }

    public String getMdescription() {
        return mDescription;
    }

    public void setMdescription(String mdescription) {
        this.mDescription = mdescription;
    }

    public void setMcategoryId(int mcategoryId) {
        this.mCategoryId = mcategoryId;
    }

    public String getmIcon() {
        return mIcon;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmWeight() {
        return mWeight;
    }

    public void setmWeight(String mWeight) {
        this.mWeight = mWeight;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIcon);
        dest.writeString(mName);
        dest.writeString(mWeight);
        dest.writeString(mPrice);
        dest.writeString(mDescription);
        dest.writeInt(mId);
        dest.writeInt(mCategoryId);
    }
}