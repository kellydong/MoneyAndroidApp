package com.example.app;

public class ExampleItem {
    private int mImageResource;
    private String category;
    private String cost;

    public ExampleItem(int imageResource, String category, String cost){
        mImageResource = imageResource;
        this.category = category;
        this.cost = cost;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getCategory() {
        return category;
    }

    public String getCost() {
        return cost;
    }
}
