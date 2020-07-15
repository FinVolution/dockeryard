package com.ppdai.dockeryard.proxy.util;

import com.ppdai.atlas.api.AppControllerApiClient;
import com.ppdai.atlas.api.AppQuotaControllerApiClient;
import com.ppdai.atlas.api.QuotaControllerApiClient;
import com.ppdai.atlas.model.AppDto;
import com.ppdai.atlas.model.ResponseAppDto;
import com.ppdai.dockeryard.core.exception.DockeryardBaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AtlasService {
    @Autowired
    AppControllerApiClient appControllerApiClient;

    @Autowired
    QuotaControllerApiClient quotaControllerApiClient;

    @Autowired
    AppQuotaControllerApiClient appQuotaControllerApiClient;

    public AppDto getAppByAppName(String appName) {
        if (StringUtils.isBlank(appName)) {
            log.warn("appName is null");
            return null;
        }
        ResponseEntity<ResponseAppDto> responseEntity = appControllerApiClient.findUniqueAppByAppNameUsingGET(appName);
        if (null == responseEntity || responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
            String errorMessage = "Interaction with Atlas seems to went wrong.Please contact zhongyi for advice.";
            throw new DockeryardBaseException(errorMessage);
        }
        AppDto app = responseEntity.getBody().getDetail();
        if (null == app) {
            String warnMessage = String.format("AppName:%s.Please ensure the appinfo is stored in Atlas.", appName);
            log.warn(warnMessage);
            return null;
        }
        log.info(app.toString());
        return app;
    }
}
