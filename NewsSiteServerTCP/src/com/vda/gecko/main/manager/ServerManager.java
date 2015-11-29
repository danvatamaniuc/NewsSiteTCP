package com.vda.gecko.main.manager;

import com.vda.gecko.data.functional.OrderCriteria;
import com.vda.gecko.data.manager.Manager;
import com.vda.gecko.data.transfer.Paginator;
import com.vda.gecko.main.domain.Author;
import com.vda.gecko.main.domain.News;
import com.vda.gecko.main.domain.User;
import com.vda.gecko.main.domain.dto.NewsDto;
import com.vda.gecko.main.repository.AuthorRepo;
import com.vda.gecko.main.repository.NewsRepo;
import com.vda.gecko.main.repository.UserRepo;
import com.vda.gecko.main.utils.Constants;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by 1 on 10/8/2015.
 */
public class ServerManager implements Manager {

    private NewsRepo    newsRepo;
    private AuthorRepo  authorRepo;
    private UserRepo    userRepo;

    private ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public ServerManager(NewsRepo newsRepo, AuthorRepo authorRepo, UserRepo userRepo) {
        this.newsRepo   = newsRepo;
        this.authorRepo = authorRepo;
        this.userRepo   = userRepo;
    }

    @Override
    public void save(News news) throws FileNotFoundException {
        newsRepo.save(news);

        //lazy, but saves to xml every new news added
        //TODO: think of when to save to xml data
        newsRepo.saveAllToXml();
    }

    public void save(Author author){
        authorRepo.save(author);
    }

    public void save(User user) {
        userRepo.save(user);
    }

    @Override
    public List<News> getAllNews(){
        return newsRepo.getAll();
    }

    @Override
    public Paginator<NewsDto> getNewsPages(String sortOrder){

        //submit a new thread to the executor service
        //in other words, prepare a thread to be executed
        Future<List<News>> futureNews = executorService.submit(() -> {
            newsRepo.loadAllFromXML();
            return newsRepo.getAll();
        });

        //get the results

        List<News> news = new ArrayList<>();

        try {

            //get the news
            news = futureNews.get();

        } catch (InterruptedException e){
            LOGGER.log(Level.SEVERE, "Fetch action interrupted");
        } catch (ExecutionException e){
            LOGGER.log(Level.SEVERE, "Execution error");
        }

        //using functional interfaces, we'll choose the sorting order and filtering criteria
        OrderCriteria orderAsc = new OrderCriteria() {
            @Override
            public int order(String s, String other) {
                return s.compareTo(other);
            }
        };

        OrderCriteria orderDesc = new OrderCriteria() {
            @Override
            public int order(String s, String other) {
                return other.compareTo(s);
            }
        };

        OrderCriteria chosenOrder;

        if (sortOrder.equals(Constants.SORT_ASCENDING)){
            chosenOrder = orderAsc;
        } else if (sortOrder.equals(Constants.SORT_DESCENDING)) {
            chosenOrder = orderDesc;
        } else {
            chosenOrder = orderAsc;
        }

        //sort ascending, create the dto-s for each news and collect them in a list
        //TODO: refactor to allow filtering
        //previous sorting: (n1, n2) -> n1.getTitle().compareTo(n2.getTitle())
        List<NewsDto> packet = news.stream()
                .sorted((n1,n2) -> chosenOrder.order(n1.getTitle(),n2.getTitle()))
                .map((article) -> {

                    //create a new dto for pagination
                    NewsDto newsDto = new NewsDto();

                    //add the title and content of the article to the dto object
                    newsDto.title   = article.getTitle();
                    newsDto.content = article.getContent();

                    return newsDto;
                })
                .collect(Collectors.toList());

        Paginator<NewsDto> paginator = new Paginator<NewsDto>(packet);
        paginator.setPageSize(5);

        return paginator;

    }

    //shutdown all active threads
    public void shutdown() {
        executorService.shutdownNow();
    }
}
