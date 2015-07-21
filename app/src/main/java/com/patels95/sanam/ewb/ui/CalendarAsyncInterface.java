package com.patels95.sanam.ewb.ui;

import com.google.api.services.calendar.model.Event;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 7/21/2015.
 */
public interface CalendarAsyncInterface {
    // Necessary component to move results from Async task to actual fragment.
    // Don't ask why, that's just how it works
    void onTaskComplete(List<Event> result);
}
