package com.vda.gecko.main.domain.validator;

import com.vda.gecko.data.domain.Validator;
import com.vda.gecko.data.exceptions.ValidationException;
import com.vda.gecko.main.domain.Author;
import com.vda.gecko.main.utils.StringUtils;

/**
 * Created by 1 on 10/9/2015.
 */
public class AuthorValidator implements Validator<Author> {

    public void validate(Author author) {
        if (author.getAge() < 0){
            throw new ValidationException("Invalid author age");
        }

        if (StringUtils.checkForNumbers(author.getName())){
            throw new ValidationException("Invalid author name");
        }
    }
}
