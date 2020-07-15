package com.ppdai.dockeryard.admin.service.impl;

import com.ppdai.atlas.model.EnvDto;
import com.ppdai.dockeryard.admin.service.EnvironmentService;
import com.ppdai.dockeryard.admin.service.ImageExtendService;
import com.ppdai.dockeryard.core.dto.ImageExtendDto;
import com.ppdai.dockeryard.core.mapper.EnvironmentMapper;
import com.ppdai.dockeryard.core.po.EnvironmentEntity;
import com.ppdai.dockeryard.core.po.ImageExtendEntity;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class EnvironmentServiceImpl implements EnvironmentService {

    @Autowired
    private EnvironmentMapper environmentMapper;

    @Autowired
    private ImageExtendService imageExtendService;

    @Override
    public List<EnvironmentEntity> getAll() {
        return environmentMapper.getAll();
    }

    @Override
    public List<String> getAllNames() {
        return environmentMapper.getAllEvnNames();
    }

    @Override
    public EnvironmentEntity getByName(String name) {
        return environmentMapper.getByName(name);
    }

    @Override
    public EnvironmentEntity getByUrl(String url) {
        return environmentMapper.getByUrl(url);
    }

    @Override
    public EnvironmentEntity getById(Long id) {
        return environmentMapper.getById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void update(EnvironmentEntity environment) {
        Long id = environment.getId();
        EnvironmentEntity orgEnvironment = environmentMapper.getById(id);
        String orgUrl = orgEnvironment.getUrl();
        String url = environment.getUrl();
        //保证页面编辑时url不能传空值。
        if(StringUtils.hasLength(url)){
            //只有在页面新输入的url与原来的值不相同时才更新。同时需要把imageExtend中对应ikey的值都同步变更。
            if(!url.equals(orgUrl)){
                environmentMapper.update(environment);
                ImageExtendDto imageExtendDto = new ImageExtendDto();
                imageExtendDto.setIKey(orgUrl);
                List<ImageExtendEntity> imageExtends = imageExtendService.getByParam(imageExtendDto);
                if(!CollectionUtils.isEmpty(imageExtends)){
                    for (ImageExtendEntity imageExtendEntity : imageExtends) {
                        imageExtendEntity.setIKey(url);
                        imageExtendService.update(imageExtendEntity, null);
                    }
                }
            }
        }

    }

    @Override
    public void delete(Long id) {
        environmentMapper.delete(id);
    }

    @Override
    public void insert(EnvironmentEntity environment) {
        environmentMapper.insert(environment);
    }

    @Override
    public void SyncAllEnv(List<EnvDto> remoteEnvList) {
        for (EnvDto envDto : remoteEnvList) {
            Long id = envDto.getId();
            EnvironmentEntity environment = environmentMapper.getById(id);
            try {
                if(environment == null){
                    environment = new EnvironmentEntity();
                    environment.setId(envDto.getId());
                    environment.setName(envDto.getName());
                    environment.setInsertBy(StringUtils.hasLength(envDto.getInsertBy()) ? envDto.getInsertBy() : "by synchronization");
                    environment.setUpdateBy(StringUtils.hasLength(envDto.getUpdateBy()) ? envDto.getUpdateBy() : "by synchronization");
                    environment.setIsActive(envDto.isActive());
                    environmentMapper.insert(environment);
                } else {
                    environment.setName(envDto.getName());
                    environment.setUpdateBy(StringUtils.hasLength(envDto.getUpdateBy()) ? envDto.getUpdateBy() : "by synchronization");
                    environment.setIsActive(envDto.isActive());
                    environmentMapper.update(environment);
                }
            }catch (Exception e){
                continue;
            }
        }
    }

}
