package com.gai.ewbbu.ewb.ui;

import com.google.api.services.calendar.model.Event;

import java.util.List;

/**
 * Created by Daniel on 7/21/2015.
 */
public interface CalendarAsyncInterface {
    // Necessary component to move results from Async task to actual fragment.
    // Don't ask why, that's just how it works
    void onTaskComplete(List<Event> result);
}
