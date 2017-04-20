package com.gai.ewbbu.ewb.ui;

import com.google.api.services.calendar.model.Event;

import java.util.List;

/**
 * Created by Daniel on 7/21/2015.
 */
public interface CalendarAsyncInterface {
    // Necessary component to move results from Async task to actual fragment.
    void onTaskComplete(List<Event> result);
}
