package com.ppdai.dockeryard.admin.configuration;

import com.ppdai.dockeryard.admin.constants.CleanPolicyType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashSet;
import java.util.Set;

@ConfigurationProperties(prefix = "dockeryard.image.cleanup.policy")
public class RepositoryPolicyProperties {

    public static final String ALL_REPOSITORIES_TAG = "*";

    private int keepFatTime = 2;
    private int keepFatNumber = 10;
    private int keepProNumber = 100;
    private Set<String> repositories = new LinkedHashSet<>();

    public int getKeepFatTime() {
        return keepFatTime;
    }

    public void setKeepFatTime(int keepFatTime) {
        if (keepFatTime < 1) {
            return;
        }
        this.keepFatTime = keepFatTime;
    }

    public int getKeepFatNumber() {
        return keepFatNumber;
    }

    public void setKeepFatNumber(int keepFatNumber) {
        if (keepFatNumber < 10) {
            return;
        }
        this.keepFatNumber = keepFatNumber;
    }

    public int getKeepProNumber() {
        return keepProNumber;
    }

    public void setKeepProNumber(int keepProNumber) {
        if (keepProNumber < 100) {
            return;
        }
        this.keepProNumber = keepProNumber;
    }

    public Set<String> getRepositories() {
        return repositories;
    }

    public void setRepositories(Set<String> repositories) {
        this.repositories = repositories;
    }
}
