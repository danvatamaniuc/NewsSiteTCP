package com.vda.gecko.main.manager;

import com.vda.gecko.data.exceptions.ServiceException;
import com.vda.gecko.data.manager.Manager;
import com.vda.gecko.data.net.Message;
import com.vda.gecko.data.transfer.Paginator;
import com.vda.gecko.main.domain.News;
import com.vda.gecko.main.domain.dto.NewsDto;
import com.vda.gecko.main.net.RpcClient;
import com.vda.gecko.main.net.SerializedMessage;
import com.vda.gecko.main.utils.Constants;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by 1 on 11/29/2015.
 */
public class ClientManager implements Manager {
    private final ExecutorService executorService;
    private RpcClient rpcClient;

    public ClientManager(ExecutorService executorService, RpcClient rpcClient) {
        this.executorService = executorService;
        this.rpcClient = rpcClient;
    }

    @Override
    public void save(News news) throws FileNotFoundException {

        //what is a raw type and why is this appearing here?
        Message<News> request = new SerializedMessage.Builder<News>()
                .setWhat(Manager.SERVER_SAVE)
                .setBody(news)
                .build();
        Message response = rpcClient.sendAndReceive(request);

        //check for any server errors
        if (response.getWhat().equalsIgnoreCase(Constants.SERVICE_ERROR)){
            throw new ServiceException((String) response.getBody());
        }
    }

    @Override
    public List<News> getAllNews() {
        Message<String> request = new SerializedMessage.Builder<String>()
                .setWhat(Manager.SERVER_GET_ALL_NEWS)
                .build();

        Message<List<News>> response = rpcClient.sendAndReceive(request);

        if (response.getWhat().equalsIgnoreCase(Constants.MESSAGE_OK)){
            return response.getBody();
        } else {
            throw new ServiceException(response.getWhat());
        }
    }

    @Override
    public Paginator<NewsDto> getNewsPages(String sortOrder) {
        Message<String> request = new SerializedMessage.Builder<String>()
                .setWhat(Manager.SERVER_GET_NEWS_PAGES)
                .setBody(sortOrder)
                .build();

        Message<Paginator<NewsDto>> response = rpcClient.sendAndReceive(request);

        if (response.getWhat().equalsIgnoreCase(Constants.MESSAGE_OK)){
            return response.getBody();
        } else {
            throw new ServiceException(response.getWhat());
        }
    }
}
