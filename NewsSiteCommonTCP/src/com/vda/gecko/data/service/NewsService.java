package com.vda.gecko.data.service;

import com.vda.gecko.data.transfer.Paginator;
import com.vda.gecko.main.domain.News;

import java.util.List;

/**
 * Created by 1 on 11/28/2015.
 * Provides an interface to the servitor class responsible for news entities
 */
public interface NewsService {

    public List<News> getAll();
    public Paginator<News> getPaginatedNews(String sortOrder);

}
