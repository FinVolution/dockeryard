package com.ppdai.dockeryard.admin.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    public static ResponseEntity<Object> createExceptionResponseEntity(Throwable throwable,String shortUuid){
        Map<String,Object> body = new HashMap<>(16);
        if(!ObjectUtils.isEmpty(throwable)){
            body.put("exception",throwable.getClass().getName());
            body.put("message",throwable.getMessage());
        }
        if(StringUtils.hasLength(shortUuid)){
            body.put("shortuuid",shortUuid);
        }
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String uri = request.getRequestURI();
        body.put("path",uri);
        body.put("timestamp",new Date());
        body.put("status",HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(body,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
