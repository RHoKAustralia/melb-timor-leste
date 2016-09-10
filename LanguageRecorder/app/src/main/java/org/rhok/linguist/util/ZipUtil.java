package org.rhok.linguist.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by bramleyt on 6/09/2016.
 */
public class ZipUtil {

    /**
     * zip files or strings
     * @param files optional list of files to flat zip
     * @param mapOfTextFileNameBody optional map of string file name to file body
     * @param destinationFile where the new zip file will be written
     * @throws IOException
     */
    public static void zip(List<File> files, Map<String, String> mapOfTextFileNameBody, File destinationFile) throws IOException{

        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(destinationFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            final int BUFFER = 1024;
            byte data[] = new byte[BUFFER];
            if(mapOfTextFileNameBody!=null) for(Map.Entry<String, String> e : mapOfTextFileNameBody.entrySet()){
                ZipEntry entry = new ZipEntry(e.getKey());
                out.putNextEntry(entry);
                byte[] d = e.getValue().getBytes();
                out.write(d, 0, d.length);
                out.closeEntry();
            }

            if(files!=null) for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(file.getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                out.closeEntry();
                origin.close();
            }

            out.close();
        } finally {

        }
    }
}
