package com.ppdai.dockeryard.admin.configuration;

import com.ppdai.dockeryard.admin.api.StarGateFeignClient;
import com.ppdai.dockeryard.admin.cleanup.ImageRepositoryMarkChain;
import com.ppdai.dockeryard.admin.cleanup.RemoteImageRepositoryCleaner;
import com.ppdai.dockeryard.admin.cleanup.executor.ImageRepositoryCleanupExecutor;
import com.ppdai.dockeryard.admin.cleanup.handler.ImageOnlyMarkHandler;
import com.ppdai.dockeryard.admin.cleanup.handler.ImageRemoveHandler;
import com.ppdai.dockeryard.admin.cleanup.handler.ImageRepositoryGarbageCollectHandler;
import com.ppdai.dockeryard.admin.cleanup.policy.FatImageMaker;
import com.ppdai.dockeryard.admin.cleanup.policy.Maker;
import com.ppdai.dockeryard.admin.cleanup.policy.ProdImageMaker;
import com.ppdai.dockeryard.admin.service.ImageService;
import com.ppdai.dockeryard.admin.service.JobCleanupLogService;
import com.ppdai.dockeryard.core.mapper.ImageMapper;
import com.ppdai.dockeryard.core.po.ImageEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Configuration
@EnableConfigurationProperties({RepositoryPolicyProperties.class})
public class DockerYardConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FatImageMaker fatImageMaker(RepositoryPolicyProperties policyProperties,
                                       ImageService imageService,
                                       StarGateFeignClient starGateClient) {
        return new FatImageMaker(policyProperties, imageService, starGateClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public ProdImageMaker proImageMaker(RepositoryPolicyProperties policyProperties,
                                        StarGateFeignClient starGateClient,
                                        ImageService imageService) {
        return new ProdImageMaker(policyProperties, starGateClient, imageService);
    }

    @Bean
    @ConditionalOnMissingBean
    public ImageRepositoryMarkChain imageRepositoryChain(List<Maker<ImageEntity>> makers) {
        return new ImageRepositoryMarkChain(makers);
    }


    @Bean
    @ConditionalOnMissingBean
    public ImageRepositoryCleanupExecutor imageRepositoryCleanupExecutor() {
        return new ImageRepositoryCleanupExecutor();
    }

    @Bean
    @ConditionalOnMissingBean
    public ImageOnlyMarkHandler imageOnlyMarkHandler(ImageRepositoryMarkChain chain,
                                                     RepositoryPolicyProperties policyProperties,
                                                     ImageMapper imageMapper,
                                                     JobCleanupLogService jobCleanupLogService) {
        return new ImageOnlyMarkHandler(chain, policyProperties, imageMapper, jobCleanupLogService);
    }

    @Bean
    @ConditionalOnMissingBean
    public ImageRemoveHandler imageRemoveHandler(JobCleanupLogService jobCleanupLogService,
                                                 ImageMapper imageMapper,
                                                 RemoteImageRepositoryCleaner cleaner,
                                                 @Lazy ImageRemoveHandler handler,
                                                 @Value("${dockeryard.register.url}") String url) {
        return new ImageRemoveHandler(jobCleanupLogService, imageMapper, cleaner, handler, url);
    }

    @Bean
    @ConditionalOnMissingBean
    public ImageRepositoryGarbageCollectHandler imageRepositoryGarbageCollectHandler(RemoteImageRepositoryCleaner cleaner) {
        return new ImageRepositoryGarbageCollectHandler(cleaner);
    }


}
