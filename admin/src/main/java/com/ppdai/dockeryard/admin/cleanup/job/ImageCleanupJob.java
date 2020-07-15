package com.ppdai.dockeryard.admin.cleanup.job;


import com.ppdai.dockeryard.admin.cleanup.handler.invoker.ImageHandlerInvokerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.ppdai.dockeryard.admin.constants.CleanPolicyType.MARK_CLEAN;

@Slf4j
@Component
public class ImageCleanupJob {

    private final ImageHandlerInvokerClient client;

    public ImageCleanupJob(ImageHandlerInvokerClient client) {
        this.client = client;
    }

    @Scheduled(cron = "0 20 1 ? * *")
    public void execute() {
        log.info("job execute start..............");
        CompletableFuture.runAsync(() -> client.invoke(MARK_CLEAN));
    }

}
