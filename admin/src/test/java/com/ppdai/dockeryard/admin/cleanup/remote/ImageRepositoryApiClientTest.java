package com.ppdai.dockeryard.admin.cleanup.remote;

import com.ppdai.dockeryard.admin.cleanup.model.ImageTag;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by chenlang on 2020/4/9
 **/
public class ImageRepositoryApiClientTest {

    private String dockerRegistryUrl = "http://localhost:5000";

    @Test
    public void getImages() throws IOException {
        ImageRepositoryApiClient imageApi = new ImageRepositoryApiClient(dockerRegistryUrl);
        ImageTag images = imageApi.getImages("hello.test.com");
        System.out.println(images.getTags());
    }

    @Test
    public void deleteImage() throws IOException {
        ImageRepositoryApiClient imageApi = new ImageRepositoryApiClient(dockerRegistryUrl);
        String digest = imageApi.getDigest("hello.test.com", "9_1");
        imageApi.deleteImage("hello.test.com",digest);
    }

    @Test
    public void getDigest() throws IOException {
        ImageRepositoryApiClient imageApi = new ImageRepositoryApiClient(dockerRegistryUrl);
        String digest = imageApi.getDigest("hello.test.com", "9_1");
        System.out.println(digest);
    }

    @Test
    public void getDigestConfig() {
    }
}
