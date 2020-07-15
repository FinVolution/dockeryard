package com.ppdai.dockeryard.admin.cleanup.model;

import lombok.Data;

@Data
public class DigestConfig {
    private String mediaType;
    private Long size;
    private String digest;
}
