package com.diosoft.lecture11.datastore;

import com.diosoft.lecture11.common.Event;

import java.io.Serializable;
import java.util.*;

public class CalendarDataStoreImpl implements Serializable,DataStore {

    private final DbDriver<Integer, Event> dbDriver;

    CalendarDataStoreImpl(String dbPath) {
        dbDriver = new DbDriver<Integer, Event>(dbPath, Integer.class, Event.class);
    }

    @Override
    public void addEvent(Event event) {
        dbDriver.addObject(event.getUuid(), event);
    }

    @Override
    public void removeEvent(Event event) {
        dbDriver.removeKey(event.getUuid());
    }

    @Override
    public List<Event> getEventsByName(String name) {
        List<Event> data = null;
        Map<Integer, Event> events = dbDriver.getObjects();

        if(!events.isEmpty()) {
            data = new ArrayList<Event>();
            Collection<Integer> keys = events.keySet();
            Iterator<Integer> it = keys.iterator();
            while(it.hasNext()) {
                Integer key = it.next();
                Event obj = events.get(key);
                if(obj.getName().equals(name))
                    data.add(obj);
            }
        }

        return data;
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> data = null;
        Map<Integer, Event> events = dbDriver.getObjects();

        if(!events.isEmpty())
            data = new ArrayList<Event>(events.values());

        return data;
    }
}
