package com.vda.gecko.data.manager;

import com.vda.gecko.data.transfer.Paginator;
import com.vda.gecko.main.domain.News;
import com.vda.gecko.main.domain.dto.NewsDto;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by 1 on 11/29/2015.
 */
public interface Manager {
    String SERVER_GET_ALL_NEWS = "getAllNews";
    String SERVER_GET_NEWS_PAGES = "getNewsPages";
    String SERVER_SAVE = "save";

    void save(News news) throws FileNotFoundException;

    List<News> getAllNews();

    Paginator<NewsDto> getNewsPages(String sortOrder);
}
