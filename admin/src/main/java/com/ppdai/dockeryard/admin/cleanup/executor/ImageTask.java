package com.ppdai.dockeryard.admin.cleanup.executor;

import com.ppdai.dockeryard.admin.cleanup.remote.ImageRepositoryServer;
import com.ppdai.dockeryard.core.po.ImageEntity;

import java.util.concurrent.Callable;

public class ImageTask implements Callable<ImageEntity> {

    private final ImageEntity imageEntity;
    private final ImageRepositoryServer imageRepositoryServer;

    public ImageTask(ImageEntity imageEntity,
                     ImageRepositoryServer imageRepositoryServer) {
        this.imageRepositoryServer = imageRepositoryServer;
        this.imageEntity = imageEntity;
    }

    @Override
    public ImageEntity call() throws Exception {
        return imageRepositoryServer.remoteDeleteImages(imageEntity);
    }

}
