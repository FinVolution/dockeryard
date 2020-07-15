package com.ppdai.dockeryard.admin.cleanup.model;

import lombok.Data;

import java.util.List;

@Data
public class Digest {
    private String schemaVersion;
    private String mediaType;
    private DigestConfig config;
    private List<DigestConfig> layers;

}
