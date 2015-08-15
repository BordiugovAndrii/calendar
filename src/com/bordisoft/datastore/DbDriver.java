package com.diosoft.lecture11.datastore;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class DbDriver <K,V>{
    private final String DB_PATH;
    private Map<K,V> container;
    private final Class<K> keyParameterClass;
    private final Class<V> valueParameterClass;

    DbDriver (String db_path, Class<K> keyParameterClass, Class<V> valueParameterClass) {
        DB_PATH = db_path;
        this.keyParameterClass = keyParameterClass;
        this.valueParameterClass = valueParameterClass;
        retrieveData();
    }

    public synchronized void addObject(K key, V obj) {
        if(container.containsKey(key))
            return;

        container.put(key,obj);
        putData();
    }

    public synchronized V removeKey(K key) {
        V obj = container.get(key);
        if(obj != null)
            container.remove(key);
        return obj;
    }

    public synchronized V removeObject(V obj) {
        V o = null;
        if(obj != null) {
            Collection<V> values = container.values();
            if(values.contains(obj)) {
                while (values.remove(obj)) ;
                o = obj;
            }
        }
        return o;
    }

    public Map<K,V> getObjects() {
        return container;
    }

    private void retrieveData() {
        container = new HashMap<K,V>();
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(DB_PATH));
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType type = objectMapper.getTypeFactory().constructMapType(Map.class, keyParameterClass, valueParameterClass);
            container = objectMapper.readValue(jsonData, type);
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    private void putData() {
        try {
            File file = new File(DB_PATH);
            File parentDir = file.getParentFile();
            if(! parentDir.exists())
                parentDir.mkdirs();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            objectMapper.writeValue(file, container);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
