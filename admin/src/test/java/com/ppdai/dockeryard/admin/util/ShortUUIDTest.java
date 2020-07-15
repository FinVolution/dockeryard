package com.ppdai.dockeryard.admin.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by chenlang on 2020/5/14
 **/
public class ShortUUIDTest {

    @Test
    public void generateShortUuid() {
        int size = IntStream.rangeClosed(1, 100000).parallel()
                .mapToObj(a -> ShortUUID.generateShortUuid())
                .collect(Collectors.toSet()).size();
        Assert.assertEquals(100000, size);
    }

}
