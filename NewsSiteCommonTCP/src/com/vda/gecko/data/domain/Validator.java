package com.vda.gecko.data.domain;

/**
 * Created by 1 on 10/20/2015.
 */
public interface Validator<E> {
    void validate(E e);
}
