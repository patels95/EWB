package com.gai.ewbbu.ewb.ui;

import com.google.api.services.calendar.model.EventAttachment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Daniel on 8/5/2015.
 */
public class CalendarSerializable implements Serializable{
    private List<EventAttachment> mEventAttachments;

    public void setEventAttachments(List<EventAttachment> list){
        mEventAttachments = list;
    }
    public List<EventAttachment> getEventAttachments(){
        return mEventAttachments;
    }
}
