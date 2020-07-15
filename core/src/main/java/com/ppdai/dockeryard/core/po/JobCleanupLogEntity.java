package com.ppdai.dockeryard.core.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCleanupLogEntity extends BaseEntity{

    private Long id;
    private String jobName;
    private String instance;
    private Long imageId;
    private String repoName;
    private String appName;
    private String tag;
    private String status;

    public static JobCleanupLogEntity apply(ImageEntity imageEntity) {
        JobCleanupLogEntity jobCleanupLogEntity = JobCleanupLogEntity
                .builder()
                .imageId(imageEntity.getId())
                .jobName("imageCleanupJobHandler")
                .repoName(imageEntity.getRepoName())
                .tag(imageEntity.getTag())
                .appName(imageEntity.getAppName())
                .instance("")
                .status("PENDING")
                .build();
        jobCleanupLogEntity.setInsertBy("job");
        return jobCleanupLogEntity;
    }

    public static List<JobCleanupLogEntity> apply(Collection<ImageEntity> images) {
        return images.stream()
                .map(JobCleanupLogEntity::apply)
                .collect(Collectors.toList());
    }
}
