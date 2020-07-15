package com.ppdai.dockeryard.core.dto;

import com.ppdai.dockeryard.core.po.EventLogEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class EnvironmentDto extends EventLogEntity implements Serializable {

    private static final long serialVersionUID = 1975327228080536037L;

    private Integer pageNo;

    private Integer pageSize;

    private Integer startRecord;

}
