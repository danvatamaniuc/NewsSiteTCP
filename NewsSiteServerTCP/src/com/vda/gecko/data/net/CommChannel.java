package com.vda.gecko.data.net;

/**
 * Created by 1 on 11/28/2015.
 * Interface to
 */
public interface CommChannel{

    public void addHandler(String methodName, MethodHandler methodHandler);
    public void start();

}
