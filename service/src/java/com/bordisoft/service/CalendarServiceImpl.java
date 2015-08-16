package com.bordisoft.service;

import com.bordisoft.common.Event;
import com.bordisoft.common.Person;
import com.bordisoft.datastore.DataStore;

import java.util.*;

public class CalendarServiceImpl implements CalendarService {

    private final DataStore dataStore;

    public CalendarServiceImpl(DataStore store) {
        dataStore = store;
    }

    @Override
    public void addEvent(String name, String description, String startTime, String endTime, List<Person> attenders) {
        Event event = new Event.Builder()
                .name(name)
                .description(description)
                .startTime(startTime)
                .endTime(endTime)
                .attenders(attenders)
                .build();
        dataStore.addEvent(event);
    }

    @Override
    public void addEvent(Event event) {
        dataStore.addEvent(event);
    }

    @Override
    public Event removeEvent(Event event) {
        dataStore.removeEvent(event);
        return event;
    }

    @Override
    public Event addAttender(Event event, Person... newPersons) {
        if(newPersons == null || newPersons.length == 0)
            return event;

        List<Person> attenders = event.getAttenders();
        attenders.addAll(Arrays.asList(newPersons));
        updateEvent(event);

        return event;
    }

    @Override
    public List<Event> getEventsByName(String name) {
        return dataStore.getEventsByName(name);
    }

    @Override
    public List<Event> getAllEvents() {
        return dataStore.getAllEvents();
    }

    private void updateEvent(Event event) {
        dataStore.removeEvent(event);
        dataStore.addEvent(event);
    }
}
