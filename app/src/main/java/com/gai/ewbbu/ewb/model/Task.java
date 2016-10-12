package com.gai.ewbbu.ewb.model;

import java.util.Calendar;

public class Task {

    private String mTitle;
    private String mDescription;
    private String mTaskId;
    private String mProjectId;
    private boolean mComplete;
    private Calendar mDueDate;

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

    public String getProjectId() {
        return mProjectId;
    }

    public void setProjectId(String projectId) {
        mProjectId = projectId;
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
