package com.vda.gecko.data.net;

import java.io.*;

/**
 * Created by 1 on 11/28/2015.
 */
public interface Message<E> extends Serializable{
    public void writeTo(ObjectOutputStream outputStream) throws IOException;
    public void readFrom(ObjectInputStream inputStream) throws IOException;

    public E getBody();
    public String getWhat();


}
