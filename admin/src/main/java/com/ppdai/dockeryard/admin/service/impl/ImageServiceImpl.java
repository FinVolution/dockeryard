package com.ppdai.dockeryard.admin.service.impl;

import com.google.common.collect.Sets;
import com.ppdai.dockeryard.admin.service.EventLogService;
import com.ppdai.dockeryard.admin.service.ImageService;
import com.ppdai.dockeryard.admin.service.OrganizationService;
import com.ppdai.dockeryard.admin.util.JsonHttpClient;
import com.ppdai.dockeryard.admin.util.RequestUtil;
import com.ppdai.dockeryard.core.dto.ImageDto;
import com.ppdai.dockeryard.core.exception.DockeryardBaseException;
import com.ppdai.dockeryard.core.mapper.ImageExtendMapper;
import com.ppdai.dockeryard.core.mapper.ImageMapper;
import com.ppdai.dockeryard.core.po.EventLogEntity;
import com.ppdai.dockeryard.core.po.ImageEntity;
import com.ppdai.dockeryard.core.po.OrganizationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class ImageServiceImpl implements ImageService {

    private static final String DOCKER_REGISTRY_URL = "http://localhost";
    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EventLogService eventLogService;

    @Autowired
    private ImageExtendMapper imageExtendMapper;

    private JsonHttpClient dockerHttpClient = new JsonHttpClient(32000, 32000);

    /**
     * 给已经存在于dockeryard的镜像打一个新的tag。
     * 成功，返回新的imageEntity且保证不为null；否则丢异常
     *
     * @param existingImage
     * @param expectedImageTag
     * @return 打了新tag的image的相关信息
     */
    @Override
    public ImageEntity tagExistingImage(ImageDto existingImage, String expectedImageTag) {
        if (existingImage == null || !StringUtils.hasLength(existingImage.getRepoName()) || !StringUtils.hasLength(existingImage.getTag())) {
            throw new DockeryardBaseException("Input Param:ImageDto is not correct");
        }
        String token = RequestUtil.getJWTTokenFromHeader("jwt-token");
        if (!StringUtils.hasLength(token)) {
            throw new DockeryardBaseException("Lack of Token");
        }

        ImageEntity currentImageEntity = getOneExistingImageEntity(existingImage);
        tagExistingImage(currentImageEntity, expectedImageTag, token);

        existingImage.setTag(expectedImageTag);
        ImageEntity imageTagged = getOneExistingImageEntity(existingImage);
        if (imageTagged == null || !StringUtils.hasText(imageTagged.getRepoName()) || !StringUtils.hasText(imageTagged.getTag())) {
            throw new DockeryardBaseException("Input Param:ImageDto is not correct");
        }
        return imageTagged;

    }

    @Override
    public int countImage(String appName, String beforeMonthFormat) {
        return imageMapper.countImageByAppAndTime(appName, beforeMonthFormat);
    }

    @Override
    public Set<ImageEntity> beforeInsertTime(String appName, String time) {
        return imageMapper.beforeImagesInertTime(appName, time);
    }

    @Override
    public Set<ImageEntity> findNeedMarkAllImages(String appName, Set<ImageEntity> imageEntities, int keepNumber) {
        List<Long> ids = imageEntities.stream().map(ImageEntity::getId).collect(Collectors.toList());
        int keepImageNumber = keepNumber - ids.size();
        return imageMapper.findNeedMarkAllImages(appName, ids, keepImageNumber > 0 ? keepImageNumber : 10);
    }

    @Override
    public Set<ImageEntity> findImages(Set<ImageEntity> images) {
        HashSet<ImageEntity> imageEntities = Sets.newHashSet();
        for (ImageEntity image : images) {
            Set<ImageEntity> imageEntitySet = imageMapper.findImageByAppAndTag(image.getRepoName(), image.getTag());
            if (!isEmpty(imageEntitySet)) {
                imageEntities.addAll(imageEntitySet);
            }
        }
        return imageEntities;
    }

    /**
     * DB中获取某一个确定存在的唯一的image的相关信息。
     * 如果不满足"存在且唯一"，直接丢异常。
     *
     * @param existingImage
     * @return
     */
    private ImageEntity getOneExistingImageEntity(ImageDto existingImage) {
        List<ImageEntity> imageLists = imageMapper.getByParam(existingImage);
        if (null == imageLists || imageLists.size() != 1) {
            throw new DockeryardBaseException(String.format("ImageRepoName:%s ,ImageTag:%s ,", existingImage.getRepoName(), existingImage.getTag()));
        }

        return imageLists.get(0);
    }

    //https://stackoverflow.com/questions/37134929/how-to-tag-image-in-docker-registry-v2/38362476#38362476
    //https://wheleph.gitlab.io/2016/07/13/tagging-using-docker-registry-http-api-v2/
    private void tagExistingImage(ImageEntity imageEntity, String expectedImageTag, String token) {
        try {
            // 获取原镜像的manifest
            Map<String, String> getHeaders = new HashMap<>();
            getHeaders.put("accept", "application/vnd.docker.distribution.manifest.v2+json");
            String getUrl = DOCKER_REGISTRY_URL + "/v2/" + imageEntity.getRepoName() + "/manifests/" + imageEntity.getTag();
            String manifest = dockerHttpClient.get(getUrl, getHeaders);

            // 上传新tag
            Map<String, String> putHeaders = new HashMap<>();
            putHeaders.put("Content-Type", "application/vnd.docker.distribution.manifest.v2+json");
            putHeaders.put("Authorization", "Bearer " + token);
            String putUrl = DOCKER_REGISTRY_URL + "/v2/" + imageEntity.getRepoName() + "/manifests/" + expectedImageTag;
            dockerHttpClient.put(putUrl, putHeaders, manifest);
        } catch (IOException e) {
            throw new DockeryardBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<ImageEntity> getAll() {
        return imageMapper.getAll();
    }

    @Override
    public List<ImageEntity> getByParam(ImageDto imageDto) {
        if (imageDto.getPageNo() != null && imageDto.getPageSize() != null) {
            //pageNo从0开始
            int startRecord = imageDto.getPageNo() * imageDto.getPageSize();
            imageDto.setStartRecord(startRecord);
        }
        return imageMapper.getByParam(imageDto);
    }

    @Override
    public List<ImageEntity> getByAppName(String appName) {
        return imageMapper.getByAppName(appName);
    }

    @Override
    public int getRecordCountByParam(ImageDto imageDto) {
        return imageMapper.countByParam(imageDto);
    }

    @Override
    public ImageEntity getById(Long id) {
        return imageMapper.getById(id);
    }

    @Override
    public void update(ImageEntity image) {
        imageMapper.update(image);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void delete(Long id, String operatorName) {
        if (StringUtils.hasLength(operatorName)) {
            ImageEntity image = imageMapper.getById(id);
            EventLogEntity eventLog = new EventLogEntity();
            eventLog.setEventName(image.getRepoName());
            eventLog.setEventAddition(image.getTag());
            eventLog.setOptionType("delete");
            eventLog.setOperator(operatorName);
            eventLogService.insert(eventLog);
        }
        imageMapper.delete(id, operatorName);
        //删除Image时，删除相关联的ImageExtend
        imageExtendMapper.deleteByImageId(id, operatorName);
    }

    @Override
    public void insert(ImageEntity image) {
        OrganizationEntity organization = organizationService.getById(image.getOrgId());
        if (organization != null) {
            //orgName在image中是冗余字段，为了保证一致性，从organization中获取。
            String orgName = organization.getName();
            image.setOrgName(orgName);
        }
        imageMapper.insert(image);
    }
}
