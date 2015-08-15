package com.diosoft.lecture11.datastore;

import com.diosoft.lecture11.common.Event;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class XmlDataStoreImportImpl extends DataStoreImport {

    private final DataStore dataStore;

    XmlDataStoreImportImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public synchronized void importDataStore(String path) {
        try {
            File file = new File(path);
            List<File> list = unpackZip(file);

            if(list.isEmpty())
                return;

            JAXBContext jaxbContext = JAXBContext.newInstance(Event.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            for(Iterator<File> i = list.iterator(); i.hasNext();) {
                Event event = (Event) jaxbUnmarshaller.unmarshal(i.next());
                dataStore.addEvent(event);
            }

            File dir = list.get(0).getParentFile();
            deleteDirectory(dir);
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
        catch(IOException io) {
            io.printStackTrace();
        }
    }
}
