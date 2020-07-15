package com.ppdai.dockeryard.admin.cleanup.remote;

import com.ppdai.dockeryard.admin.AbstractUnitTest;
import com.ppdai.dockeryard.core.po.ImageEntity;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;

/**
 * Created by chenlang on 2020/5/14
 **/
public class ImageRepositoryServerTest extends AbstractUnitTest {

    @Mock
    private ImageRepositoryApiClient client;
    @InjectMocks
    private ImageRepositoryServer repositoryServer;



    @Test
    public void remoteDeleteImages_normal() throws IOException {
        ImageEntity entity = new ImageEntity();
        entity.setIsActive(true);
        repositoryServer.remoteDeleteImages(entity);
        Assert.assertFalse(entity.getIsActive());
    }


    @Test
    public void remoteDeleteImages_error() throws IOException {
        ImageEntity entity = new ImageEntity();
        entity.setIsActive(true);
        when(client.getDigest(anyString(),anyString())).thenThrow(IOException.class);
        repositoryServer.remoteDeleteImages(entity);
        Assert.assertTrue(entity.getIsActive());
    }

    @Test
    public void remoteDeleteImages_error_not_found() throws IOException {
        IOException ioException = mock(IOException.class);
        when(ioException.getMessage()).thenReturn("Not Found");
        ImageEntity entity = new ImageEntity();
        entity.setIsActive(true);
        when(client.getDigest(anyString(),anyString())).thenThrow(ioException);
        repositoryServer.remoteDeleteImages(entity);
        Assert.assertFalse(entity.getIsActive());
    }
}
