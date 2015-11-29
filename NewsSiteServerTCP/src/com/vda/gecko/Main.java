package com.vda.gecko;

import com.vda.gecko.data.domain.Validator;
import com.vda.gecko.data.manager.Manager;
import com.vda.gecko.data.transfer.Paginator;
import com.vda.gecko.main.domain.News;
import com.vda.gecko.main.domain.dto.NewsDto;
import com.vda.gecko.main.domain.validator.AuthorValidator;
import com.vda.gecko.main.domain.validator.NewsValidator;
import com.vda.gecko.main.domain.validator.UserValidator;
import com.vda.gecko.main.manager.ServerManager;
import com.vda.gecko.main.net.RpcServer;
import com.vda.gecko.main.net.SerializedMessage;
import com.vda.gecko.main.repository.AuthorRepo;
import com.vda.gecko.main.repository.NewsRepo;
import com.vda.gecko.main.repository.UserRepo;
import com.vda.gecko.main.utils.ServerConstants;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        //prepare the server to work
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Validator newsValidator = new NewsValidator();
        Validator authorValidator = new AuthorValidator();
        Validator userValidator = new UserValidator();

        NewsRepo newsCRUDRepository = new NewsRepo();
        AuthorRepo authorCRUDRepository = new AuthorRepo();
        UserRepo userCRUDRepository = new UserRepo();

        newsCRUDRepository.setValidator(newsValidator);
        authorCRUDRepository.setValidator(authorValidator);
        userCRUDRepository.setValidator(userValidator);

        Manager manager = new ServerManager(newsCRUDRepository, authorCRUDRepository, userCRUDRepository);

        RpcServer rpcServer = new RpcServer(executorService, ServerConstants.SERVICE_HOST, ServerConstants.SERVER_PORT);

        rpcServer.addHandler(Manager.SERVER_SAVE, (request) -> {
            try {
                manager.save((News) request.getBody());
            } catch (IOException e){
                e.printStackTrace();
            }

            //TODO: get this working using interfaces
            return new SerializedMessage.Builder<String>()
                    .setWhat(ServerConstants.MESSAGE_OK)
                    .setBody(null)
                    .build();
        });

        rpcServer.addHandler(Manager.SERVER_GET_ALL_NEWS, (request) -> {
            List<News> news = manager.getAllNews();
            return new SerializedMessage.Builder<List<News>>()
                    .setWhat(ServerConstants.MESSAGE_OK)
                    .setBody(news)
                    .build();
        });

        rpcServer.addHandler(Manager.SERVER_GET_NEWS_PAGES, (request) -> {
            Paginator<NewsDto> newsPaginator = manager.getNewsPages((String) request.getBody());

            return new SerializedMessage.Builder<Paginator<NewsDto>>()
                    .setWhat(ServerConstants.MESSAGE_OK)
                    .setBody(newsPaginator)
                    .build();
        });

        rpcServer.start();
    }
}
