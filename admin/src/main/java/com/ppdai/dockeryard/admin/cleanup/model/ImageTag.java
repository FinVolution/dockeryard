package com.ppdai.dockeryard.admin.cleanup.model;

import lombok.Data;

import java.util.List;

@Data
public class ImageTag {
    private String name;
    private List<String> tags;
}
