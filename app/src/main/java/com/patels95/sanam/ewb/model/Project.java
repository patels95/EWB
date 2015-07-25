package com.patels95.sanam.ewb.model;


public class Project {

    private String mTitle;
    private String mDescription;
    private String mImageUri;

    public Project() {
        // default constructor
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

    public String getImageUri() {
        return mImageUri;
    }

    public void setImageUri(String imageUri) {
        mImageUri = imageUri;
    }
}
