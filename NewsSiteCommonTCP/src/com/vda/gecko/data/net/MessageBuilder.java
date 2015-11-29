package com.vda.gecko.data.net;

/**
 * Created by 1 on 11/28/2015.
 */
public interface MessageBuilder {
    public MessageBuilder setWhat(String what);
    public MessageBuilder setBody(Object body);
    public Message build();
}
