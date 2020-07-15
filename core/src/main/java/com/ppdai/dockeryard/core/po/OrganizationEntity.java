package com.ppdai.dockeryard.core.po;

import lombok.Data;

@Data
public class OrganizationEntity extends BaseEntity {

    private Long id;
    private String name;
    private String shortCode;
}
