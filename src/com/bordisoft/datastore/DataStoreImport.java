package com.diosoft.lecture11.datastore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class DataStoreImport {
    public abstract void importDataStore(String path);

    protected List<File> unpackZip(File zipFile) throws IOException {
        List<File> list = new ArrayList<File>();
        long unixTime = System.currentTimeMillis() / 1000L;
        final String dir_path = System.getProperty("java.io.tmpdir") + "imports-" + unixTime + "/";
        byte[] buffer = new byte[1024];
        //create output directory is not exists
        File folder = new File(dir_path);
        if(!folder.exists()){
            folder.mkdir();
        }

        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry ze = zis.getNextEntry();

        while(ze != null) {
            String fileName = ze.getName();
            File newFile = new File(dir_path + fileName);

            System.out.println("file unzip : "+ newFile.getAbsoluteFile());

            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();
            ze = zis.getNextEntry();

            list.add(newFile);
        }

        zis.closeEntry();
        zis.close();

        System.out.println("Done");

        return list;
    }

    protected boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return(directory.delete());
    }
}
