package org.rhok.linguist.util;

public class FileUtils {
    /** Returns the string after the last '.' character */
    public static String getExtension(String path) {
        String[] parts = path.split("\\.");
        return parts[parts.length - 1];
    }
}
