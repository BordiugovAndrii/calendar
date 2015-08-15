package com.diosoft.lecture11.datastore;

import com.diosoft.lecture11.common.Event;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlDataStoreExportImpl extends DataStoreExport {

    private final DataStore dataStore;

    XmlDataStoreExportImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public synchronized void exportDataStore(final String path) {
        List<Event> list = dataStore.getAllEvents();

        if(list.isEmpty())
            return;

        List<File> files = new ArrayList<File>();
        long unixTime = System.currentTimeMillis() / 1000L;
        final String dir_path = System.getProperty("java.io.tmpdir") + "exports-" + unixTime + "/";

        try {

            for(Iterator<Event> i = list.iterator(); i.hasNext();) {
                Event event = i.next();

                File file = new File(dir_path + event.getUuid() + ".xml");
                File parentDir = file.getParentFile();
                if(! parentDir.exists())
                    parentDir.mkdirs();

                JAXBContext jaxbContext = JAXBContext.newInstance(Event.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                jaxbMarshaller.marshal(event, file);

                files.add(file);
            }

            packZip(new File(path), files);
            deleteDirectory(new File(dir_path));
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
        catch (IOException i) {
            i.printStackTrace();
        }
    }
}
