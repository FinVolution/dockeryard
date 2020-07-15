package com.ppdai.dockeryard.core.po;

import lombok.Data;

@Data
public class UserEntity extends BaseEntity {

    private Long id;
    private String name;
    private String role;
    private Long orgId;
    private String orgName;
    private String realName;
    private String email;
    private String workNumber;
    private String department;

}
