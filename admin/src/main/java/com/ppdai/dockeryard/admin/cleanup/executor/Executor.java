package com.ppdai.dockeryard.admin.cleanup.executor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

public interface Executor<T, E> {

    List<Future<E>> execute(Collection<T> tasks);
}
