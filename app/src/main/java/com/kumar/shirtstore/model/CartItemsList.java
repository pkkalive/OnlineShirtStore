package com.kumar.shirtstore.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Purushotham on 09/08/17.
 */

public class CartItemsList implements Parcelable {

    private int id;
    private double price;
    private String picture;
    private String colour;
    private String size;
    private String name;
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.price);
        dest.writeString(this.colour);
        dest.writeString(this.size);
        dest.writeInt(this.quantity);
    }

    public CartItemsList() {
    }

    protected CartItemsList(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.price = in.readDouble();
        this.colour = in.readString();
        this.size = in.readString();
        this.quantity = in.readInt();
    }

    public static final Parcelable.Creator<CartItemsList> CREATOR = new Parcelable.Creator<CartItemsList>() {
        @Override
        public CartItemsList createFromParcel(Parcel source) {
            return new CartItemsList(source);
        }

        @Override
        public CartItemsList[] newArray(int size) {
            return new CartItemsList[size];
        }
    };
}