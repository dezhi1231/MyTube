package com.opposs.tube.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by xcl on 16/2/24.
 */
public class MyClosable {


    public static void close(Closeable closeable){

        synchronized (closeable){
            try {

                if(closeable!=null){
                    closeable.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
