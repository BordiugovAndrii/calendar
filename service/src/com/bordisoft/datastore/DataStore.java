package com.diosoft.lecture11.datastore;

import com.diosoft.lecture11.common.Event;

import java.util.List;

public interface DataStore {

    void addEvent(Event inputValue);

    void removeEvent(Event inputValue);

    List<Event> getEventsByName(String inputValue);

    List<Event> getAllEvents();
}
