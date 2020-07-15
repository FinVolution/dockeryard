package com.ppdai.dockeryard.admin.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于前台页面显示分页插件
 *
 */
@Data
public class PageVO<T> {
    private List<T> content;
    private Integer totalElements;
}