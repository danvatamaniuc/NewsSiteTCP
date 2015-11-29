package com.vda.gecko.data.net;

/**
 * Created by 1 on 11/29/2015.
 */
public interface CommChannel {
    public Message sendAndReceive(Message request);
}
