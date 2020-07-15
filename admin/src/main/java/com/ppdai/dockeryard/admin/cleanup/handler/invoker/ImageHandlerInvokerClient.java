package com.ppdai.dockeryard.admin.cleanup.handler.invoker;

import com.ppdai.dockeryard.admin.cleanup.handler.ImageOnlyMarkHandler;
import com.ppdai.dockeryard.admin.cleanup.handler.ImageRemoveHandler;
import com.ppdai.dockeryard.admin.cleanup.handler.ImageRepositoryGarbageCollectHandler;
import com.ppdai.dockeryard.admin.constants.CleanPolicyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static com.google.common.collect.Lists.newArrayList;
import static com.ppdai.dockeryard.admin.constants.CleanPolicyType.*;

@Component
public class ImageHandlerInvokerClient {

    @Autowired
    private ApplicationContext applicationContext;

    public void invoke(CleanPolicyType type) {
        ImageRemoveHandler removeHandler = applicationContext.getBean(ImageRemoveHandler.class);
        ImageOnlyMarkHandler imageOnlyMarkHandler = applicationContext.getBean(ImageOnlyMarkHandler.class);
        ImageRepositoryGarbageCollectHandler collectHandler = applicationContext.getBean(ImageRepositoryGarbageCollectHandler.class);
        if (ONLY_MARK.equals(type)) {
            new ImageHandlerInvoker(newArrayList(imageOnlyMarkHandler)).execute();
        } else if (ONLY_CLEAN.equals(type)) {
            new ImageHandlerInvoker(newArrayList(removeHandler)).execute();
        } else if (MARK_CLEAN.equals(type)) {
            //请保证handler顺序 imageOnlyMarkHandler removeHandler
            new ImageHandlerInvoker(newArrayList(imageOnlyMarkHandler, removeHandler)).execute();
        } else if (GARBAGE_COLLECT.equals(type)) {
            new ImageHandlerInvoker(newArrayList(collectHandler)).execute();
        }
    }

}
