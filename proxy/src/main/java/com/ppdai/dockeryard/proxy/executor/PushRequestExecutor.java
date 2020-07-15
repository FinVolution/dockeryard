package com.ppdai.dockeryard.proxy.executor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppdai.dockeryard.core.po.OrganizationEntity;
import com.ppdai.dockeryard.proxy.config.LittleShootProxyConfig;
import com.ppdai.dockeryard.proxy.dao.UltraDao;
import com.ppdai.dockeryard.proxy.util.ApplicationContextHelper;
import com.ppdai.dockeryard.proxy.util.CommonUtil;
import com.ppdai.dockeryard.proxy.util.DockerRegistryV2RequestType;
import com.ppdai.dockeryard.proxy.util.DockerRegistryV2Util;
import com.ppdai.pauth.client.api.OAuth2EndpointApi;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.littleshoot.proxy.impl.ProxyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class PushRequestExecutor implements RequestExecutor {

    private static Logger logger = LoggerFactory.getLogger(PushRequestExecutor.class);

    private OAuth2EndpointApi authApi;

    PushRequestExecutor() {
        LittleShootProxyConfig littleShootProxyConfig = ApplicationContextHelper.getApplicationContext().getBean("littleShootProxyConfig", LittleShootProxyConfig.class);
        authApi = new OAuth2EndpointApi();
        authApi.getApiClient().setBasePath(littleShootProxyConfig.getAuthServerHost());
    }

    /**
     * 此处的详细步骤：
     * 1）校验Authorization header 对应的jwt token过期与否
     * 2）解析token，获取userName&userOrgName
     * 若解析不出userOrgName，异常退出
     * 3）数据库检索，是否存在对应userOrgName的组织实例：
     * 若无，异常退出
     * 4）数据库检索，判断镜像是否存在
     * 5）保存相关记录
     *
     * @param httpRequest
     * @return
     */
    @Override
    public HttpResponse execute(HttpRequest httpRequest) {
        String shortUuid = httpRequest.headers().get("shortUuid");
        DockerRegistryV2RequestType requestType = DockerRegistryV2Util.getRequestType(httpRequest);
        if (requestType == DockerRegistryV2RequestType.PUSH_LAYER || requestType == DockerRegistryV2RequestType.PUSH_MANIFEST) {
            String uri = httpRequest.getUri();
            String auth = httpRequest.headers().get("Authorization");
            logger.info(shortUuid + " PushRequestExecutor received a request : " + httpRequest.getMethod() + " " + httpRequest.getUri() + "; requestType is " + requestType + "; Authorization is " + auth);
            try {
                //token验证通过后，解析token，从中获取用户的基本信息。
                DecodedJWT jwt = CommonUtil.validateTokenInRequest(auth);
                String userName = jwt.getSubject();
                String userOrgName = jwt.getClaim("user_org").asString();
                UltraDao ultraDao = ApplicationContextHelper.getApplicationContext().getBean("ultraDao", UltraDao.class);
                if (StringUtils.hasLength(userOrgName)) {
                    OrganizationEntity organizationEntity = ultraDao.getOrganizationByName(userOrgName);
                    if (organizationEntity == null) {
                        String errorMsg = String.format("[%s] could not load any organization by the orgName [%s] from the token", shortUuid, userOrgName);
                        logger.error(errorMsg);
                        return ProxyUtils.createFullHttpResponse(HttpVersion.HTTP_1_1,
                                HttpResponseStatus.FORBIDDEN, CommonUtil.formatError("UNAUTHORIZED", errorMsg));
                    }
                    //上传_manifests的时候需要判断images是否存在并记录相关审计日志。
                    if (requestType == DockerRegistryV2RequestType.PUSH_MANIFEST) {
                        //判断镜像是否存在，如果存在，返回repository:tag
                        String existingImage = ultraDao.isImageExistent(uri);
                        if (StringUtils.hasLength(existingImage)) {
                            String errorMsg = String.format("the image [%s] has already been pushed.", existingImage);
                            return ProxyUtils.createFullHttpResponse(HttpVersion.HTTP_1_1,
                                    HttpResponseStatus.FORBIDDEN, CommonUtil.formatError("UNAUTHORIZED", errorMsg));
                        }
                        //保存相关记录,镜像相关的信息，通过uri从atlas获取
                        ultraDao.saveRecordForPushImage(false, uri, userName, organizationEntity);
                        logger.info("{} PushRequestExecutor tag in the Metric", shortUuid);
                    }
                    logger.info("{} PushRequestExecutor finished a {} request", shortUuid, requestType);
                } else {
                    String errorMsg = "could not find organization from the JWT token ";
                    logger.error("{} PushRequestExecutor {}", shortUuid, errorMsg);
                    return ProxyUtils.createFullHttpResponse(HttpVersion.HTTP_1_1,
                            HttpResponseStatus.FORBIDDEN, CommonUtil.formatError("FORBIDDEN", errorMsg));
                }
            } catch (Exception e) {
                logger.error(shortUuid + " :" + e.getMessage(), e);
                return ProxyUtils.createFullHttpResponse(HttpVersion.HTTP_1_1,
                        HttpResponseStatus.FORBIDDEN, CommonUtil.formatError("FORBIDDEN", e.getMessage()));
            }
        }
        //正常透传
        return null;
    }

}
