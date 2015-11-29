package com.vda.gecko.main.utils;

import java.io.Closeable;

/**
 * Created by 1 on 11/28/2015.
 * Houses methods used to ease IO operations
 */
public class IOUtils {

    public static void close(Closeable closeable){
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

}
