package com.ppdai.dockeryard.core.dto;

import com.ppdai.dockeryard.core.po.EventLogEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class EventLogDto extends EventLogEntity implements Serializable {

    public static final long serialVersionUID = 8073994741114283155L;

    private String startDateTime;

    private String endDateTime;

    private Integer pageNo;

    private Integer pageSize;

    private Integer startRecord;

}
