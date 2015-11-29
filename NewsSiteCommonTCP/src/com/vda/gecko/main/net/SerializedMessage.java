package com.vda.gecko.main.net;

import com.vda.gecko.data.net.Message;
import com.vda.gecko.data.net.MessageBuilder;
import com.vda.gecko.main.utils.Constants;

import java.io.*;

/**
 * Created by 1 on 11/28/2015.
 * Implementation of Message entity
 *
 * Messages will be able to read/write info from the sockets, and
 * provide their content and meta info
 */
public class SerializedMessage<E> implements Message{

    private String what;
    private E body;

    public SerializedMessage() {
    }

//  class responsible for building a message by chaining methods
    public static class Builder<F> implements MessageBuilder{
        private SerializedMessage<F> message = new SerializedMessage<F>();

        public Builder setWhat(String what){
            message.setWhat(what);
            return this;
        }

        public Builder setBody(Object body){
            message.setBody(body);
            return this;
        }

        public SerializedMessage build(){
            return message;
        }
    }

//  getters and setters

    @Override
    public String getWhat() {
        return what;
    }

    @Override
    public E getBody() {
        return body;
    }

    private void setWhat(String what) {
        this.what = what;
    }

    private void setBody(Object body) {
        this.body = (E) body;
    }

//  functional methods

    @Override
    public String toString() {
        return "SerializedMessage{" +
                "what='" + what + '\'' +
                ", body='" + body.toString() + '\'' +
                '}';
    }

    public void writeTo(ObjectOutputStream outputStream) throws IOException{
        outputStream.writeObject(this);
    }

    public void readFrom(ObjectInputStream inputStream) throws IOException{
        try {

            SerializedMessage<E> message = (SerializedMessage<E>) inputStream.readObject();

            this.setWhat(message.getWhat());
            this.setBody(message.getBody());

        } catch (ClassNotFoundException e){
            throw new IOException(e);
        }
    }

}
