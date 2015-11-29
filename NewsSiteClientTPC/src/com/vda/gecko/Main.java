package com.vda.gecko;

import com.vda.gecko.main.UI.ConsoleUI;
import com.vda.gecko.main.manager.ClientManager;
import com.vda.gecko.main.net.RpcClient;
import com.vda.gecko.main.utils.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        RpcClient rpcClient = new RpcClient(Constants.SERVICE_HOST,Constants.SERVER_PORT);

        ClientManager clientManager = new ClientManager(executorService,rpcClient);

        ConsoleUI consoleUI = new ConsoleUI(clientManager);

        consoleUI.run();

        executorService.shutdownNow();
    }
}
