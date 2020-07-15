package com.ppdai.dockeryard.admin.cleanup.handler;

import com.ppdai.dockeryard.admin.cleanup.RemoteImageRepositoryCleaner;

/**
 * 对于经过{@link ImageRemoveHandler}处理的镜像，需要垃圾回收才能真正释放存储空间
 * <p>
 * 会调用远程机器的clean.sh脚本执行垃圾回收：
 * 垃圾回收过程时间较长，仓库内容大小决定的，该过程为非离线状态，执行垃圾回收会导致上传的镜像不可用，请慎重调用
 * </p>
 * Created by chenlang on 2020/4/30
 **/
public class ImageRepositoryGarbageCollectHandler implements Handler {

    private RemoteImageRepositoryCleaner cleaner;

    public ImageRepositoryGarbageCollectHandler(RemoteImageRepositoryCleaner cleaner) {
        this.cleaner = cleaner;
    }

    @Override
    public void handle() {
        cleaner.collectImages();
    }

}
