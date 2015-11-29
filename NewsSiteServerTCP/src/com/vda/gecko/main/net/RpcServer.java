package com.vda.gecko.main.net;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.vda.gecko.data.exceptions.ServiceException;
import com.vda.gecko.data.net.CommChannel;
import com.vda.gecko.data.net.Message;
import com.vda.gecko.data.net.MethodHandler;
import com.vda.gecko.main.utils.Constants;
import com.vda.gecko.main.utils.IOUtils;
import com.vda.gecko.main.utils.ServerConstants;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 1 on 11/28/2015.
 * The bit that provides connection between server and client.
 * This is the server side of the connection. (Amazing)
 *
 * Requires an ExecutorService, the host and port dedicated to the server instance.
 */
public class RpcServer implements CommChannel {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final ExecutorService executorService;
    private final String serviceHost;
    private final int servicePort;

    private Map<String, MethodHandler> methodHandlers = new HashMap<>();

    public RpcServer(ExecutorService executorService, String serviceHost, int servicePort) {
        this.executorService = executorService;
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
    }

    public void addHandler(String method, MethodHandler methodHandler){
        methodHandlers.put(method, methodHandler);
    }

    public void start(){
        try {

            LOGGER.log(Level.INFO, "Creating socket---");
            ServerSocket serverSocket = new ServerSocket(ServerConstants.SERVER_PORT);
            LOGGER.log(Level.INFO, "Socket created---");

            while (!Thread.currentThread().isInterrupted()){
                LOGGER.log(Level.INFO, "Waiting for clients---");
                Socket socket = serverSocket.accept();
                LOGGER.log(Level.INFO, "Client inbound, accepted---");

                executorService.submit(new RpcHandler(socket));
            }
        } catch (IOException e){
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    private class RpcHandler implements Runnable {
        private final Socket socket;

        public RpcHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run(){
            ObjectOutputStream outputStream = null;
            ObjectInputStream inputStream = null;

            try{
                LOGGER.log(Level.INFO, "Handling client request---");

                //read the request
                inputStream = new ObjectInputStream(socket.getInputStream());
//                byte[] bytes = new byte[1024];
//                int bytesRec = inputStream.read(bytes);

                Message request = new SerializedMessage.Builder().build();
                request.readFrom(inputStream);
                MethodHandler methodHandler = methodHandlers.get(request.getWhat());

                //call the method using the method handler
                Exception methodException = null;
                Message response = null;

                if (methodHandler != null){
                    try {

                        response = methodHandler.execute(request);
                        LOGGER.log(Level.INFO, "Request processed---");

                    } catch (Exception e) {

                        LOGGER.log(Level.INFO, "Request failed to process---");
                        methodException = e;

                    }
                } else {
                    methodException = new Exception("Unknown request");
                }

                if (methodException != null){
                    String exceptionMessage = methodException.getMessage();
                    if (exceptionMessage == null){
                        exceptionMessage = "Unknown Service Exception";
                    }

                    response = new SerializedMessage.Builder()
                            .setWhat(Constants.SERVICE_ERROR)
                            .setBody(exceptionMessage)
                            .build();
                }

                //send back the response
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                response.writeTo(outputStream);
                outputStream.flush();

            } catch (IOException e){
                LOGGER.log(Level.SEVERE, "Caught IOException while handling a request---");
                e.printStackTrace();
            } finally {
                IOUtils.close(outputStream);
                IOUtils.close(inputStream);
                IOUtils.close(socket);
            }
        }
    }

//  RpcHandler class, responsible for handling out requests
}
