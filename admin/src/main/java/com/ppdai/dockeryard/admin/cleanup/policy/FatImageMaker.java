package com.ppdai.dockeryard.admin.cleanup.policy;

import com.ppdai.dockeryard.admin.api.StarGateFeignClient;
import com.ppdai.dockeryard.admin.configuration.RepositoryPolicyProperties;
import com.ppdai.dockeryard.admin.service.ImageService;
import com.ppdai.dockeryard.core.po.ImageEntity;

import java.util.Set;

import static com.google.common.collect.Sets.difference;
import static com.ppdai.dockeryard.admin.util.DateUtils.currentTimeMinus;
import static com.ppdai.dockeryard.admin.util.DateUtils.localDateTimeFormat;
import static java.time.temporal.ChronoUnit.MONTHS;

public class FatImageMaker implements Maker<ImageEntity> {

    private final RepositoryPolicyProperties policyProperties;
    private final ImageService imageService;
    private final StarGateFeignClient starGateClient;

    public FatImageMaker(RepositoryPolicyProperties policyProperties,
                         ImageService imageService,
                         StarGateFeignClient starGateClient) {
        this.starGateClient = starGateClient;
        this.policyProperties = policyProperties;
        this.imageService = imageService;
    }

    /**
     * 标记测试环境中可能被需要处理的镜像
     * （可能被处理，是因为被标记过的镜像还要经过是不是生产需要保留的镜像）
     *
     * @param appName
     * @param images
     * @return
     */
    @Override
    public Set<ImageEntity> mark(String appName, Set<ImageEntity> images) {
        String beforeMonthFormat = localDateTimeFormat(currentTimeMinus(policyProperties.getKeepFatTime(), MONTHS));
        //获取正在使用的测试环境版本
        Set<ImageEntity> imageEntities = findNonProdIsUsingImages(appName);
        Set<ImageEntity> imagesFromDb = imageService.findImages(imageEntities);
        //获取某段时间内该应用的镜像数量
        int count = imageCounterBeforeTime(appName, beforeMonthFormat);
        if (count >= policyProperties.getKeepFatNumber()) {
            return difference(allImagesBeforeInsertTime(appName, beforeMonthFormat), imagesFromDb);
        } else {
            return imageService.findNeedMarkAllImages(appName, imagesFromDb, policyProperties.getKeepFatNumber());
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }

    private int imageCounterBeforeTime(String appName, String beforeMonthFormat) {
        return imageService.countImage(appName, beforeMonthFormat);
    }

    private Set<ImageEntity> allImagesBeforeInsertTime(String appName, String time) {
        return imageService.beforeInsertTime(appName, time);
    }

    private Set<ImageEntity> findNonProdIsUsingImages(String appName) {
        //远程调用获取正在使用的镜像
        return starGateClient.obtainIsUsingImage(appName, "FAT");
    }

}
