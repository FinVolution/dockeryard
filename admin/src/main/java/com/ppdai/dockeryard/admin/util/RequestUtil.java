package com.ppdai.dockeryard.admin.util;

//import com.auth0.jwt.JWT;
//import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppdai.dockeryard.core.po.UserEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    public static UserEntity parseJWTTokenFromHeader(String tokenKey){
        UserEntity userEntity = null;
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String jwtToken = request.getHeader(tokenKey);
        if(StringUtils.hasLength(jwtToken)){
            try {
//                DecodedJWT jwt = JWT.decode(jwtToken);
//                String userName = jwt.getSubject();
//                String userMail = jwt.getClaim("user_mail").asString();
//                String userRole = jwt.getClaim("user_role").asString();
//                String userOrg = jwt.getClaim("user_org").asString();
                userEntity = new UserEntity();
//                userEntity.setName(userName);
//                userEntity.setEmail(userMail);
//                userEntity.setOrgName(userOrg);
//                userEntity.setRole(userRole);
            }catch (Exception e){

            }
        }
        return userEntity;
    }

    /**
     * header 中获取jwt-token 并做一定校验后返回该token，否则返回null
     * @param tokenKey
     * @return
     */
    public static String getJWTTokenFromHeader(String tokenKey){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String jwtToken = request.getHeader(tokenKey);
//        if(StringUtils.hasLength(jwtToken)){
//            DecodedJWT jwt = JWT.decode(jwtToken);
//            String userName = jwt.getSubject();
//            if(StringUtils.hasLength(userName)){
//                return jwtToken;
//            }
//        }
        return null;
    }



}
