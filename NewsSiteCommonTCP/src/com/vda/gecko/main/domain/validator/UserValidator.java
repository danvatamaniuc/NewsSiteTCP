package com.vda.gecko.main.domain.validator;

import com.vda.gecko.data.domain.Validator;
import com.vda.gecko.data.exceptions.ValidationException;
import com.vda.gecko.main.domain.User;

/**
 * Created by 1 on 10/10/2015.
 */
public class UserValidator implements Validator<User> {
    public void validate(User user){
        if (user.getUsername() == ""){
            throw new ValidationException("Numele utilizatorului nu e definit!");
        }
    }
}
