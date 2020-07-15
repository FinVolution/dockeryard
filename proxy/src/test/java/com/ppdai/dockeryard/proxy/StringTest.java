package com.ppdai.dockeryard.proxy;

import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by chenlang on 2020/5/9
 **/
public class StringTest {


    public static void main(String[] args) {
        String replace = "chenlang.love".replace("love", "shabi");
        System.out.println(replace);

        System.out.println(null instanceof HttpRequest);
    }


}
