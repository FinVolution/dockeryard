package com.ppdai.dockeryard.proxy.dao;

import com.ppdai.atlas.model.AppDto;
import com.ppdai.atlas.model.OrgDto;
import com.ppdai.dockeryard.core.dto.ImageDto;
import com.ppdai.dockeryard.core.exception.DockeryardBaseException;
import com.ppdai.dockeryard.core.mapper.EventLogMapper;
import com.ppdai.dockeryard.core.mapper.ImageMapper;
import com.ppdai.dockeryard.core.mapper.OrganizationMapper;
import com.ppdai.dockeryard.core.mapper.RepositoryMapper;
import com.ppdai.dockeryard.core.po.EventLogEntity;
import com.ppdai.dockeryard.core.po.ImageEntity;
import com.ppdai.dockeryard.core.po.OrganizationEntity;
import com.ppdai.dockeryard.core.po.RepositoryEntity;
import com.ppdai.dockeryard.proxy.util.AtlasService;
import com.ppdai.dockeryard.proxy.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
@Slf4j
public class UltraDao {


    private static final String ERROR_MESSAGE = "";

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Autowired
    private EventLogMapper eventLogMapper;

    @Autowired
    private AtlasService atlasService;

    /**
     * 检查镜像是否可以被拉取
     */
    public void validateImage(String imageUrl) {
        //解析imageUrl 得出repository和tag
        List<String> imageElements = CommonUtil.convertImageUrl(imageUrl);
        if (imageElements.size() == 1) {
            throw new DockeryardBaseException(String.format(ERROR_MESSAGE, imageUrl));
        }
        String repositoryName = imageElements.get(0);
        String tag = imageElements.get(1);
        ImageDto imageDto = new ImageDto();
        imageDto.setRepoName(repositoryName);
        imageDto.setTag(tag);
        List<ImageEntity> imageEntities = imageMapper.getByParam(imageDto);
        if (ObjectUtils.isEmpty(imageEntities)) {
            throw new DockeryardBaseException("can not find the image " + repositoryName + ":" + tag);
        }
    }

    public OrganizationEntity getOrganizationByName(String organizationName) {
        if (StringUtils.hasLength(organizationName)) {
            OrganizationEntity organizationEntity = organizationMapper.getByName(organizationName);
            if (organizationEntity != null) {
                return organizationEntity;
            } else {
                throw new DockeryardBaseException("can not find a organization named " + organizationName);
            }
        } else {
            throw new DockeryardBaseException("the parameter organizationName is " + organizationName);
        }
    }

    /**
     * 判断image是否存在
     *
     * @return 如果存在，返回repository:tag
     */
    public String isImageExistent(String imageUrl) {
        List<String> imageElements = CommonUtil.convertImageUrl(imageUrl);
        if (imageElements.size() == 1) {
            throw new DockeryardBaseException(String.format(ERROR_MESSAGE, imageUrl));
        }
        String repositoryName = imageElements.get(0);
        String tag = imageElements.get(1);
        ImageDto imageDto = new ImageDto();
        imageDto.setRepoName(repositoryName);
        imageDto.setTag(tag);
        List<ImageEntity> imageEntities = imageMapper.getByParam(imageDto);
        if (!CollectionUtils.isEmpty(imageEntities)) {
            return repositoryName + ":" + tag;
        }
        return null;
    }

    /**
     * 保存image时记录所有相关的数据，包括审计。
     *
     * @param disregardOrganizationInParams
     * @param imageUrl                      镜像的url
     * @param operatorName                  此次操作的操作人员
     * @param backupInfoSource              如果Atlas返回appName对应的appInfo不存在，则使用此info
     */
    public void saveRecordForPushImage(boolean disregardOrganizationInParams, String imageUrl, String operatorName, OrganizationEntity backupInfoSource) {
        if (!StringUtils.hasLength(imageUrl)) {
            throw new DockeryardBaseException(String.format("ImageUrl:%s", imageUrl));
        }
        List<String> imageElements = CommonUtil.convertImageUrl(imageUrl);
        if (imageElements.size() == 1) {
            throw new DockeryardBaseException(String.format(ERROR_MESSAGE, imageUrl));
        }
        String repositoryName = imageElements.get(0);
        //再从repository中解析出OrgShortCode和appName。
        List<String> splitElements = CommonUtil.splitRepositoryName(repositoryName);
        if (CollectionUtils.isEmpty(splitElements)) {
            throw new DockeryardBaseException(String.format("repository name [%s] is contrary to the specification.", repositoryName));
        }

        String appName = splitElements.size() == 1 ? splitElements.get(0) : splitElements.get(1);
        AppDto appDto = atlasService.getAppByAppName(appName);
        if (null != appDto) {
            //存储的相关信息使用atlas中的信息
            OrgDto org = appDto.getOrgDto();
            saveRecordForPushImage(disregardOrganizationInParams, imageUrl, org.getId(), org.getName(), operatorName);
        } else if (backupInfoSource != null && backupInfoSource.getId() != null && backupInfoSource.getName() != null) {
            //存储的相关信息使用jwt token中获取的信息
            String infoMessage = String.format("Cannot get app info from Atlas with the appName:%s,so use the info from jwt token instead.", appName);
            log.info(infoMessage);
            saveRecordForPushImage(disregardOrganizationInParams, imageUrl, backupInfoSource.getId(), backupInfoSource.getName(), operatorName);
        } else {
            //见errorMessage。
            String errorMessage = "Cannot get any useful info from neither Atlas nor token for appName:" + appName + ".Please contact zhongyi for advice.";
            throw new DockeryardBaseException(errorMessage);
        }

    }

    /**
     * 保存image时记录所有相关的数据，包括审计。
     */
    private void saveRecordForPushImage(boolean disregardOrganizationInParams, String imageUrl, Long organizationId, String organizationName, String operatorName) {
        if (StringUtils.hasLength(imageUrl)) {
            //从参数Imageurl中解析出repository，tag。
            List<String> imageElements = CommonUtil.convertImageUrl(imageUrl);
            if (imageElements.size() == 1) {
                throw new DockeryardBaseException(String.format("imageUrl [%s] is contrary to the specification.", imageUrl));
            }
            String repositoryName = imageElements.get(0);
            String tag = imageElements.get(1);
            //再从repository中解析出OrgShortCode和appName。
            List<String> splitElements = CommonUtil.splitRepositoryName(repositoryName);
            if (CollectionUtils.isEmpty(splitElements)) {
                throw new DockeryardBaseException(String.format("repository name [%s] is contrary to the specification.", repositoryName));
            }
            String orgShortCode = splitElements.size() == 1 ? organizationName : splitElements.get(0);
            String appName = splitElements.size() == 1 ? splitElements.get(0) : splitElements.get(1);

            if (disregardOrganizationInParams) {
                OrganizationEntity organizationEntity = organizationMapper.getByShortCode(orgShortCode);
                if (organizationEntity == null) {
                    throw new DockeryardBaseException(String.format("the organization [%s] is not exists.", orgShortCode));
                }
                organizationId = organizationEntity.getId();
                organizationName = organizationEntity.getName();
            }
            RepositoryEntity existingRepository = repositoryMapper.getByName(repositoryName);
            long repositoryId;
            //判断repository是否存在，不存在就先创建repository
            if (existingRepository == null) {
                RepositoryEntity repositoryEntity = new RepositoryEntity();
                repositoryEntity.setName(repositoryName);
                repositoryEntity.setOrgId(organizationId);
                repositoryEntity.setOrgName(organizationName);
                repositoryEntity.setStarCount(0);
                repositoryEntity.setDownloadCount(0);
                repositoryEntity.setInsertBy(operatorName);
                repositoryEntity.setUpdateBy(operatorName);
                repositoryMapper.insert(repositoryEntity);
                repositoryId = repositoryEntity.getId();
                //记录repository的创建事件
                EventLogEntity eventLogEntity = new EventLogEntity();
                eventLogEntity.setEventName(imageElements.get(0));
                eventLogEntity.setEventAddition("N/A");
                eventLogEntity.setOperator(operatorName);
                eventLogEntity.setOptionType("create");
                eventLogMapper.insert(eventLogEntity);
            } else {
                repositoryId = existingRepository.getId();
            }
            //保存image信息
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setRepoName(repositoryName);
            imageEntity.setAppName(appName);
            imageEntity.setTag(tag);
            imageEntity.setRepoId(repositoryId);
            imageEntity.setOrgId(organizationId);
            imageEntity.setOrgName(organizationName);
            imageEntity.setInsertBy(operatorName);
            imageEntity.setUpdateBy(operatorName);
            imageMapper.insert(imageEntity);

            //记录image的上传事件
            EventLogEntity eventLogEntity = new EventLogEntity();
            eventLogEntity.setEventName(imageElements.get(0));
            eventLogEntity.setEventAddition(imageElements.get(1));
            eventLogEntity.setOperator(operatorName);
            eventLogEntity.setOptionType("push");
            eventLogMapper.insert(eventLogEntity);
        }
    }

}
