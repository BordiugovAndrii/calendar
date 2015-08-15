package com.diosoft.lecture11.service;

import com.diosoft.lecture11.common.Event;
import com.diosoft.lecture11.common.Person;

import java.util.List;

public interface CalendarService {

    void addEvent(String name, String description, String startTime, String endTime, List<Person> attenders);

    void addEvent(Event event);

    Event removeEvent(Event event);

    Event addAttender(Event event, Person... newPersons);

    List<Event> getEventsByName(String name);

    List<Event> getAllEvents();
}
