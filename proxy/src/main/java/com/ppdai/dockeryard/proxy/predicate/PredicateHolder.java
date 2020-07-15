package com.ppdai.dockeryard.proxy.predicate;

import java.util.function.Predicate;

public class PredicateHolder {

    private static PredicateHolder predicateHolder = new PredicateHolder();

    private Predicate<Object> predicate;

    private PredicateHolder (){
        this.predicate = new IpWhiteListPredicate();
    }

    public static Predicate<Object> getPredicate(){
        return predicateHolder.predicate;
    }

}
