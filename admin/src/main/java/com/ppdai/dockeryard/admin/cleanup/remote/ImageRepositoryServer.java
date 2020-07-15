package com.ppdai.dockeryard.admin.cleanup.remote;

import com.ppdai.dockeryard.core.po.ImageEntity;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;

@Slf4j
public class ImageRepositoryServer implements Closeable {
    private static final String IMAGE_NOT_FOUND = "Not Found";
    private ImageRepositoryApiClient client;
    private String url;

    public ImageRepositoryServer(String url) {
        this.url = url;
        this.client = new ImageRepositoryApiClient(this.url);
    }

    /**
     * 删除镜像
     * <p>首先从请求头获取对应版本镜像的digest</p>
     * <p>用获取的digest删除对应的镜像（该删除仅仅是逻辑上的删除即标记为可回收镜像）</p>
     * 垃圾回收{@link com.ppdai.dockeryard.admin.cleanup.RemoteImageRepositoryCleaner}
     *
     * @param image
     * @return
     */
    public ImageEntity remoteDeleteImages(ImageEntity image) {
        try {
            String digest = this.client.getDigest(image.getRepoName(), image.getTag());
            this.client.deleteImage(image.getRepoName(), digest);
            image.setIsActive(false);
        } catch (Exception e) {
            if (IMAGE_NOT_FOUND.equalsIgnoreCase(e.getMessage())) {
                image.setIsActive(false);
                return image;
            }
            log.error("删除镜像{}失败:{}", image.getAppName() + ":" + image.getTag(), e);
        }
        return image;
    }

    @Override
    public void close() {
        this.client.close();
    }
}
