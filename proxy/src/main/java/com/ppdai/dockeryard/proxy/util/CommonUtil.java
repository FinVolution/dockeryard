package com.ppdai.dockeryard.proxy.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppdai.dockeryard.core.exception.DockeryardBaseException;
import com.ppdai.dockeryard.proxy.constant.ProxyConstant;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class CommonUtil {

    private CommonUtil() {
        throw new AssertionError();
    }

    private static final String SEPARATOR = "/";

    public static String formatError(String code, String message) {
        return String.format("{\"errors\":[{\"code\":\"%s\",\"detail\":[],\"message\":\"%s\"}]}", code, message);
    }

    /**
     * 通过docker daemon向proxy server发送push命令时的参数解析出image的repository的名称和tag。
     *
     * @return 下标0表示repository的名称，下标1表示tag（默认值是latest）。
     */
    public static List<String> convertImageUrl(String imageUrl) {
        String repoName;
        String tag;
        List<String> convertedElements = new ArrayList<>();
        if (StringUtils.hasLength(imageUrl) && imageUrl.contains(ProxyConstant.SEPARATOR + ProxyConstant.MANIFESTS + ProxyConstant.SEPARATOR)) {
            //如果push是带版本号，url就会以/manifests/结尾，版本号设为默认值latest。
            String[] temp = imageUrl.split("/manifests/");
            if (temp.length > 1) {
                tag = temp[1];
            } else {
                tag = "latest";
            }
            //将url头部的/v2/去掉。剩余部分就是repository的名称。
            repoName = temp[0].substring(temp[0].indexOf(ProxyConstant.SEPARATOR + ProxyConstant.V2 + ProxyConstant.SEPARATOR) + 4);
            convertedElements.add(repoName);
            convertedElements.add(tag);
        } else if (StringUtils.hasLength(imageUrl) && imageUrl.contains(ProxyConstant.BLOBS_UPLOADS)) {
            repoName = imageUrl.substring(imageUrl.indexOf(ProxyConstant.SEPARATOR + ProxyConstant.V2 + ProxyConstant.SEPARATOR) + 4);
            repoName = repoName.substring(0, repoName.indexOf(ProxyConstant.BLOBS_UPLOADS));
            convertedElements.add(repoName);
        }
        return convertedElements;
    }

    public static List<String> splitRepositoryName(String repositoryName) {
        List<String> splitElements = new ArrayList<>();
        if (repositoryName.contains(SEPARATOR) && repositoryName.split(SEPARATOR).length > 1) {
            splitElements = CollectionUtils.arrayToList(repositoryName.split(SEPARATOR));
        } else {
            splitElements.add(repositoryName);
        }
        return splitElements;
    }

    public static DecodedJWT validateTokenInRequest(String auth) throws Exception {
        if (StringUtils.isEmpty(auth)) {
            throw new DockeryardBaseException("No authorization in request header");
        }
        if ((!auth.startsWith(ProxyConstant.BEARER))) {
            throw new DockeryardBaseException("Please ensure you have apply proper permission to take this action");
        }
        //验证用户的token
        String token = auth.replaceFirst("Bearer ", "");
        //token验证通过后，解析token，从中获取用户的基本信息。
        return JWT.decode(token);
    }

}
