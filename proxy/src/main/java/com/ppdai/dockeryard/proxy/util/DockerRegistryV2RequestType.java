package com.ppdai.dockeryard.proxy.util;

public enum DockerRegistryV2RequestType {

    /**
     * API_VERSION_CHECK
     */
    API_VERSION_CHECK,
    PULL_IMAGE_MANIFEST,
    CHECK_IMAGE_MANIFEST,
    PUSH_MANIFEST,
    PULL_LAYER,
    PUSH_LAYER,
    CHECK_LAYER,
    GET_UPLOAD_PROCESS,
    MONOLITHIC_UPLOAD,
    CHUNKED_UPLOAD,
    CANCEL_UPLOAD,
    DELETE_LAYER,
    DELETE_IMAGE,
    OTHER

}
