package com.ppdai.dockeryard.admin.cleanup.policy;

import java.util.Set;

public interface Maker<T> {

    /**
     *
     * @param appName
     * @param t
     * @return
     */
    Set<T> mark(String appName, Set<T> t);

    boolean isActive();

}
