package com.ppdai.dockeryard.core.po;

import lombok.Data;

@Data
public class ImageEntity extends BaseEntity {

    private Long id;
    private String guid;
    /**
     * 镜像名称（repoName），不包含tag。
     */
    private String repoName;
    private String appName;
    private String tag;
    private Long repoId;
    private Long orgId;
    private String orgName;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("class ImageEntity {\n");
        sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
        sb.append("    guid: ").append(this.toIndentedString(this.guid)).append("\n");
        sb.append("    repoName: ").append(this.toIndentedString(this.repoName)).append("\n");
        sb.append("    appName: ").append(this.toIndentedString(this.appName)).append("\n");
        sb.append("    tag: ").append(this.toIndentedString(this.tag)).append("\n");
        sb.append("    repoId: ").append(this.toIndentedString(this.repoId)).append("\n");
        sb.append("    orgId: ").append(this.toIndentedString(this.orgId)).append("\n");
        sb.append("    orgName: ").append(this.toIndentedString(this.orgName)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null?"null":o.toString().replace("\n", "\n    ");
    }
}
