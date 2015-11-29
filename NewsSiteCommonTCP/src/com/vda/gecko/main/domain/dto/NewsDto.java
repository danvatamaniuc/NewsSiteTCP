package com.vda.gecko.main.domain.dto;

import java.io.Serializable;

/**
 * Created by 1 on 11/11/2015.
 */
public class NewsDto implements Serializable{
    public String title;
    public String content;

    @Override
    public String toString() {
        return "News: " +
                "title = '" + title + '\'' +
                ", content = '" + content + '\'';
    }
}
