package com.bordisoft.datastore;

import com.bordisoft.common.Event;

import java.util.List;

public interface DataStore {

    void addEvent(Event inputValue);

    void removeEvent(Event inputValue);

    List<Event> getEventsByName(String inputValue);

    List<Event> getAllEvents();
}
