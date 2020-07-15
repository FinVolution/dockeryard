package com.ppdai.dockeryard.admin.constants;


public enum CleanPolicyType {

    /**
     * 仅仅做标记
     */
    ONLY_MARK,
    /**
     * 仅仅做清除
     */
    ONLY_CLEAN,
    /**
     * 标记清除
     */
    MARK_CLEAN,

    /**
     * 垃圾回收
     */
    GARBAGE_COLLECT


}
