package com.ppdai.dockeryard.core.po;

import lombok.Data;

@Data
public class RepositoryEntity extends BaseEntity {

    private Long id;
    private String name;
    private Long orgId;
    private String orgName;
    private String description;
    private int starCount;
    private int downloadCount;
}