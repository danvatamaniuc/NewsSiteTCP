package com.vda.gecko.main.net;

import com.vda.gecko.data.exceptions.ServiceException;
import com.vda.gecko.data.net.CommChannel;
import com.vda.gecko.data.net.Message;
import com.vda.gecko.main.utils.Constants;
import com.vda.gecko.main.utils.IOUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 1 on 11/29/2015.
 * Responsible for communication with the server
 */
public class RpcClient implements CommChannel {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private String serverHost;
    private int serverPort;

    public RpcClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public Message sendAndReceive(Message request){
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        LOGGER.log(Level.INFO, "Connecting to the service+++");

        try (Socket socket = new Socket(serverHost, serverPort)){

            LOGGER.log(Level.INFO, "Connection successful+++");

            LOGGER.log(Level.INFO, "Attempting to send request to server---");

            //write to the socket
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            request.writeTo(objectOutputStream);
            objectOutputStream.flush();

            LOGGER.log(Level.INFO, "Request sent to the server+++");

            //read the response

            LOGGER.log(Level.INFO, "Waiting for response---");

            objectInputStream = new ObjectInputStream(socket.getInputStream());

            Message response = new SerializedMessage.Builder().build();
            response.readFrom(objectInputStream);

            //check status of response
            if (response.getWhat().equalsIgnoreCase(Constants.MESSAGE_OK)){
                LOGGER.log(Level.INFO, "Message received successfully+++");
                return response;
            } else {
                LOGGER.log(Level.INFO, "Bad response: " + response.toString());
                throw new ServiceException((String) response.getBody());
            }

        } catch (IOException e){
            LOGGER.log(Level.SEVERE, "IO Error: ");
            e.printStackTrace();

            throw new ServiceException(e);
        } finally {
            IOUtils.close(objectInputStream);
            IOUtils.close(objectOutputStream);
        }
    }

}
