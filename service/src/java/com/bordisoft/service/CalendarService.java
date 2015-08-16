package com.bordisoft.service;

import com.bordisoft.common.Event;
import com.bordisoft.common.Person;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CalendarService extends Remote {

    void addEvent(String name, String description, String startTime, String endTime, List<Person> attenders) throws RemoteException;

    void addEvent(Event event) throws RemoteException;

    Event removeEvent(Event event) throws RemoteException;

    Event addAttender(Event event, Person... newPersons) throws RemoteException;

    List<Event> getEventsByName(String name) throws RemoteException;

    List<Event> getAllEvents() throws RemoteException;
}
