package com.ppdai.dockeryard.core.mapper;

import com.ppdai.dockeryard.core.dto.ImageExtendDto;
import com.ppdai.dockeryard.core.po.ImageExtendEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageExtendMapper {

    /**
     * 获取所有镜像标记列表
     *
     * @return 镜像标记列表
     */
    List<ImageExtendEntity> getAll();

    /**
     * 根据查询条件获取镜像标记列表
     *
     * @param imageExtendDto 查询条件
     * @return 满足条件的镜像标记列表
     */
    List<ImageExtendEntity> getByParam(ImageExtendDto imageExtendDto);

    /**
     * 计算满足条件的数据条数
     *
     * @param imageExtendDto 查询条件
     * @return 镜像列表
     */
    int countByParam(ImageExtendDto imageExtendDto);

    /**
     * 获取指定镜像标记
     *
     * @param id 主键
     * @return ImageExtendEntity
     */
    ImageExtendEntity getById(@Param("id") long id);

    /**
     * 插入新镜像标记
     *
     * @param imageExtend
     */
    void insert(ImageExtendEntity imageExtend);

    /**
     * 更新镜像标记信息
     * 只更新iValue，updateBy，updateTime。
     *
     * @param imageExtend
     */
    void update(ImageExtendEntity imageExtend);

    /**
     * 删除镜像标记信息
     *
     * @param id 主键
     */
    void delete(@Param("id") long id);

    /**
     * 根据imageId删除
     * @param imageExtend
     */
    void deleteByImageId(@Param("imageId") long imageId, @Param("updateBy") String updateBy);

}
