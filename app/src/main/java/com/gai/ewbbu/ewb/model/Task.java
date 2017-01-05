package com.gai.ewbbu.ewb.model;

import java.util.Calendar;

public class Task {

    private String mTitle;
    private String mDescription;
    private String mTaskId;
    private String mFirebaseProjectKey;
    private boolean mComplete;
    private Calendar mDueDate;

    public Task() {
        // default constructor
    }

    public Task(String title, String description, String firebaseProjectKey, boolean complete, Calendar dueDate) {
        mTitle = title;
        mDescription = description;
        mFirebaseProjectKey = firebaseProjectKey;
        mComplete = complete;
        mDueDate = dueDate;
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

    public String getTaskId() {
        return mTaskId;
    }

    public void setTaskId(String taskId) {
        mTaskId = taskId;
    }

    public String getFirebaseProjectKey() {
        return mFirebaseProjectKey;
    }

    public void setFirebaseProjectKey(String firebaseProjectKey) {
        mFirebaseProjectKey = firebaseProjectKey;
    }

    public boolean isComplete() {
        return mComplete;
    }

    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    public Calendar getDueDate() {
        return mDueDate;
    }

    public void setDueDate(Calendar dueDate) {
        mDueDate = dueDate;
    }
}
