package com.vda.gecko.data.net;


/**
 * Created by 1 on 11/28/2015.
 * Functional interface responsible for executing message requests
 */

@FunctionalInterface
public interface MethodHandler {
     public Message execute(Message request);
}
