package com.ppdai.dockeryard.core.dto;

import com.ppdai.dockeryard.core.po.ImageExtendEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class ImageExtendDto extends ImageExtendEntity implements Serializable {

    private static final long serialVersionUID = 4937933317233465354L;

    private Integer pageNo;

    private Integer pageSize;

    private Integer startRecord;

}
