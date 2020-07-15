package com.ppdai.dockeryard.core.dto;

import com.ppdai.dockeryard.core.po.RepositoryEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class RepositoryDto extends RepositoryEntity implements Serializable {

    private static final long serialVersionUID = 7798912215390272558L;

    private Integer pageNo;

    private Integer pageSize;

    private Integer startRecord;


}
