package com.vda.gecko.main.repository;

import com.vda.gecko.data.repository.AbstractRepo;
import com.vda.gecko.main.domain.Author;

import java.util.HashMap;

/**
 * Created by 1 on 10/9/2015.
 */
public class AuthorRepo extends AbstractRepo<Author> {

    public AuthorRepo() {
        super();
    }

    public void setEntityId(Author author){
        author.setId(lastId);
    }

    protected Author getObject(HashMap<String, String> objectProperties){

        //get the name of the author
        String name = objectProperties.get("name");

        //and his age, convert it to string
        String ageString = objectProperties.get("age");
        int age = Integer.parseInt(ageString);

        Author author = new Author(name, age);

        //and finally set its id
        String idString = objectProperties.get("id");
        int id = Integer.parseInt(idString);

        author.setId(id);

        return author;
    }
}
