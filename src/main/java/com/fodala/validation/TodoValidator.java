package com.fodala.validation;

import com.fodala.pojo.ToDo;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TodoValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return cls == ToDo.class;
    }

    @Override
    public void validate(Object ip, Errors errors) {
        ToDo toDo = (ToDo) ip;
        if (toDo.getTitle() == null || toDo.getTitle().length() == 0) {
            errors.reject("title", "Please enter an action to do.");
        }
    }

}

