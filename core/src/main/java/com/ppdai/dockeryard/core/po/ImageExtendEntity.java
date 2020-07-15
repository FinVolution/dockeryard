package com.ppdai.dockeryard.core.po;

import lombok.Data;

@Data
public class ImageExtendEntity extends BaseEntity{
    private Long id;
    private Long imageId;
    private String iKey;
    private String iValue;

}
