package com.vda.gecko.main.repository;

import com.vda.gecko.data.repository.AbstractRepo;
import com.vda.gecko.main.domain.News;

import java.util.HashMap;

/**
 * Created by 1 on 10/8/2015.
 */
public class NewsRepo extends AbstractRepo<News>{

    public NewsRepo() {
        super();
    }

    public void setEntityId(News news){
        news.setId(lastId);
    }

    protected News getObject(HashMap<String, String> objectProperties){

        //get the news title and content
        String title = objectProperties.get("title");
        String content = objectProperties.get("content");

        //also get the object's id
        String idString = objectProperties.get("id");
        int id = Integer.parseInt(idString);

        News news = new News(title, content);

        news.setId(id);

        return news;

    }


}
