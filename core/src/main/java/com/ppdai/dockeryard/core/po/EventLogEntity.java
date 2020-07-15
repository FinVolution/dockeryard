package com.ppdai.dockeryard.core.po;

import lombok.Data;

import java.util.Date;

@Data
public class EventLogEntity {

    private Long id;
    private String eventName;
    private String eventAddition;
    private String optionType;
    private String operator;
    private Date operateTime;

}
