package com.ppdai.dockeryard.admin.cleanup.policy;

import com.ppdai.dockeryard.admin.api.StarGateFeignClient;
import com.ppdai.dockeryard.admin.configuration.RepositoryPolicyProperties;
import com.ppdai.dockeryard.admin.service.ImageService;
import com.ppdai.dockeryard.core.po.ImageEntity;

import java.util.Set;

import static com.google.common.collect.Sets.difference;

public class ProdImageMaker implements Maker<ImageEntity> {

    private final RepositoryPolicyProperties policyProperties;
    private final StarGateFeignClient starGateClient;
    private final ImageService imageService;

    public ProdImageMaker(RepositoryPolicyProperties policyProperties,
                          StarGateFeignClient starGateClient,
                          ImageService imageService) {
        this.imageService = imageService;
        this.starGateClient = starGateClient;
        this.policyProperties = policyProperties;
    }

    @Override
    public Set<ImageEntity> mark(String appName, Set<ImageEntity> images) {
        Set<ImageEntity> imageEntities = findProdUsedImages(appName, policyProperties.getKeepProNumber());
        //从image表中获取一遍
        Set<ImageEntity> imagesFromDb = imageService.findImages(imageEntities);
        //获取差集，获得集合为最终需要处理的镜像
        return difference(images, imagesFromDb);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    private Set<ImageEntity> findProdUsedImages(String appName, int keepProNumber) {
        return starGateClient.obtainUsedImage(appName, keepProNumber, "PRO");
    }
}
