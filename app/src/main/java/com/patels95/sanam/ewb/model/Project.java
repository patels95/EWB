package com.patels95.sanam.ewb.model;


public class Project {

    private String mTitle;
    private String mDescription;
    private int mIndex;

    public Project(String title, String description, int index){
        mTitle = title;
        mDescription = description;
        mIndex = index;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }
}
