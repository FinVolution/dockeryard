package com.ppdai.dockeryard.admin.cleanup.job;

import com.ppdai.dockeryard.admin.cleanup.handler.invoker.ImageHandlerInvokerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.ppdai.dockeryard.admin.constants.CleanPolicyType.GARBAGE_COLLECT;

@Slf4j
@Component
public class ImageGarbageCollectJob {

    @Autowired
    private ImageHandlerInvokerClient client;


    @Scheduled(cron = "0 10 0 ? * MON")
    public void execute() {
        log.info("镜像仓库垃圾回收开始....");
        client.invoke(GARBAGE_COLLECT);
    }
}
