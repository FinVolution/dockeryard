package com.ppdai.dockeryard.core.dto;

import com.ppdai.dockeryard.core.po.ImageEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class ImageDto extends ImageEntity implements Serializable {

    private static final long serialVersionUID = 3443344135414943164L;

    private String iKey;

    private Integer pageNo;

    private Integer pageSize;

    private Integer startRecord;
}
