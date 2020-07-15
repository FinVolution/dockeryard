package com.ppdai.dockeryard.core.dto;

import com.ppdai.dockeryard.core.po.UserEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto extends UserEntity implements Serializable {

    private static final long serialVersionUID = -6237122757292528826L;

    private Integer pageNo;

    private Integer pageSize;

    private Integer startRecord;

}
