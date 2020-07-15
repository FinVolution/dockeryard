package com.ppdai.dockeryard.admin.cleanup.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.ppdai.dockeryard.admin.cleanup.model.ImageTag;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Slf4j
public class ImageRepositoryApiClient implements Closeable {

    private static final String DOCKER_REGISTER_HEADER = "application/vnd.docker.distribution.manifest.v2+json";

    private CloseableHttpClient client;
    private String url;
    private HttpResponseValidator validator;
    private ObjectMapper mapper;
    private Integer connectTimeout;
    private int socketTimeout;
    private int requestConnTimeout;

    public ImageRepositoryApiClient(String url) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5_000)
                .setSocketTimeout(10_000)
                .setConnectionRequestTimeout(5_000)
                .build();
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(5_000)
                .build();
        this.client = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultSocketConfig(socketConfig)
                .setMaxConnPerRoute(20) //该参数最好和线程数对应
                .setMaxConnTotal(20)
                .build();
        this.url = url;
        this.validator = new HttpResponseValidator();
        this.mapper = getDefaultMapper();
    }

    public ImageRepositoryApiClient(String url, int connectTimeout, int socketTimeout, int requestConnTimeout) {
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
        this.requestConnTimeout = requestConnTimeout;
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(this.connectTimeout)
                .setSocketTimeout(this.socketTimeout)
                .setConnectionRequestTimeout(this.requestConnTimeout)
                .build();
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(this.connectTimeout)
                .build();
        this.client = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultSocketConfig(socketConfig)
                .setMaxConnPerRoute(20) //该参数最好和线程数对应
                .setMaxConnTotal(20)
                .build();
        this.url = url;
        this.validator = new HttpResponseValidator();
        this.mapper = getDefaultMapper();
    }

    /**
     * 删除镜像
     *
     * @param repository
     * @param digest
     * @throws IOException
     */
    public void deleteImage(String repository, String digest) throws IOException {
        HttpDelete delete = new HttpDelete(url + "/v2/" + repository + "/manifests/" + digest + "");
        delete.addHeader("Accept", DOCKER_REGISTER_HEADER);
        HttpResponse response = client.execute(delete);
        try {
            validator.validateResponse(response);
        } finally {
            EntityUtils.consume(response.getEntity());
            delete.reset();
        }
    }

    /**
     * 获取digest
     *
     * @param repository
     * @param tag
     * @return
     * @throws IOException
     */
    public String getDigest(String repository, String tag) throws IOException {
        //Docker-Content-Digest
        HttpGet get = new HttpGet(url + "/v2/" + repository + "/manifests/" + tag + "");
        get.addHeader("Accept", DOCKER_REGISTER_HEADER);
        HttpResponse response = client.execute(get);
        try {
            validator.validateResponse(response);
            Header[] headers = response.getHeaders("Docker-Content-Digest");
            return headers[0].getValue();
        } finally {
            EntityUtils.consume(response.getEntity());
            get.reset();
        }
    }

    /**
     * 获取对应仓库的所有镜像
     *
     * @param repository
     * @return
     * @throws IOException
     */
    public ImageTag getImages(String repository) throws IOException {
        HttpGet get = new HttpGet(url + "/v2/" + repository + "/tags/list");
        HttpResponse response = client.execute(get);
        try {
            validator.validateResponse(response);
            return objectFromResponse(ImageTag.class, response);
        } finally {
            EntityUtils.consume(response.getEntity());
            get.reset();
        }
    }

    private <T> T objectFromResponse(Class<T> cls, HttpResponse response) throws IOException {
        InputStream content = response.getEntity().getContent();
        byte[] bytes = ByteStreams.toByteArray(content);
        return mapper.readValue(bytes, cls);
    }

    private ObjectMapper getDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    @Override
    public void close() {
        try {
            client.close();
        } catch (final IOException ex) {
            log.debug("I/O exception closing client", ex);
        }
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setRequestConnTimeout(int requestConnTimeout) {
        this.requestConnTimeout = requestConnTimeout;
    }
}
