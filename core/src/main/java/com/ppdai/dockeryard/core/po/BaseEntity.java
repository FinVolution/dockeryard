package com.ppdai.dockeryard.core.po;

import lombok.Data;

import java.util.Date;


@Data
public class BaseEntity {

    private String insertBy;
    private String updateBy;
    private Date insertTime;
    private Date updateTime;
    private Boolean isActive;

}
