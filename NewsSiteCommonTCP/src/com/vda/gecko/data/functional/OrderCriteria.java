package com.vda.gecko.data.functional;

/**
 * Created by 1 on 11/13/2015.
 */

@FunctionalInterface
public interface OrderCriteria {
    int order(String s, String other);
}
