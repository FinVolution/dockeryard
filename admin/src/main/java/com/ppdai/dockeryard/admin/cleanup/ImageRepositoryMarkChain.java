package com.ppdai.dockeryard.admin.cleanup;

import com.google.common.collect.Sets;
import com.ppdai.dockeryard.admin.cleanup.policy.Maker;
import com.ppdai.dockeryard.core.po.ImageEntity;

import java.util.List;
import java.util.Set;

public class ImageRepositoryMarkChain {

    private List<Maker<ImageEntity>> makers;

    public ImageRepositoryMarkChain(List<Maker<ImageEntity>> makers) {
        this.makers = makers;
    }

    public Set<ImageEntity> needCleanupImages(String appName) {
        Set<ImageEntity> markNeedCleanupImages = Sets.newHashSet();
        for (Maker<ImageEntity> maker : makers) {
            if (maker.isActive()) {
                markNeedCleanupImages = maker.mark(appName, markNeedCleanupImages);
            }
        }
        return markNeedCleanupImages;
    }

}
