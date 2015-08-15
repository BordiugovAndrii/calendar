package com.diosoft.lecture11;

import com.diosoft.lecture11.common.Person;
import com.diosoft.lecture11.datastore.DataStoreExport;
import com.diosoft.lecture11.datastore.DataStoreImport;
import com.diosoft.lecture11.datastore.XmlDataStoreExportImpl;
import com.diosoft.lecture11.datastore.XmlDataStoreImportImpl;
import com.diosoft.lecture11.service.CalendarServiceImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Main {

    public static final Logger logger = Logger.getAnonymousLogger();

    public static void main(String[] args) {
        if(args.length == 0)
            throw new RuntimeException("No descriptions passed");

        logger.info("Service has been started");

        // predefined attenders
        List<Person> attenders = new ArrayList<Person>() {{
            this.add(new Person.Builder().firstName("Bobo").email("test@test.ua").phone("849384938903").build());
            this.add(new Person.Builder().firstName("John").email("John@test.ua").phone("8493845444938903").build());
            this.add(new Person.Builder().firstName("Kaka").email("Kaka@test.ua").phone("456468787897").build());
        }};

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/diosoft/lecture11/applicationContext.xml");
        CalendarServiceImpl calendarService = context.getBean("calendarService", CalendarServiceImpl.class);

        // predefined events
        for(int i = 0; i < args.length; i++)
            calendarService.addEvent(args[i], ("Test task" + i), "2015-07-25 12:00:00", "2015-07-25 13:00:00", attenders);

        DataStoreExport xmlExport = context.getBean("xmlExport", XmlDataStoreExportImpl.class);
        xmlExport.exportDataStore("D:\\Projects\\HW\\src\\com\\diosoft\\lecture11\\export\\xml.zip");

        DataStoreImport xmlImport = context.getBean("xmlImport", XmlDataStoreImportImpl.class);
        xmlImport.importDataStore("D:\\Projects\\HW\\src\\com\\diosoft\\lecture11\\export\\xml.zip");
    }
}
