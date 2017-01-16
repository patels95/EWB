package com.gai.ewbbu.ewb.model;


import java.io.File;

public class Project {

    private String mTitle;
    private String mDescription;
    private String mImageUri;
    private Task[] mTasks;
    private String mFirebaseKey;
    private File[] mResources;

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

    public Task[] getTasks() {
        return mTasks;
    }

    public void setTasks(Task[] tasks) {
        mTasks = tasks;
    }

    public String getFirebaseKey() {
        return mFirebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.mFirebaseKey = firebaseKey;
    }

    public File[] getResources() {
        return mResources;
    }

    public void setResources(File[] resources) {
        mResources = resources;
    }
}
