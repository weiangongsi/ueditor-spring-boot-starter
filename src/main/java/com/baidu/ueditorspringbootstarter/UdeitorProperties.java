package com.baidu.ueditorspringbootstarter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 百度编辑器配置
 *
 * @author lihy
 * @version 2018/6/12
 */
@ConfigurationProperties("ue")
public class UdeitorProperties {

    /**
     * config.json 路径
     */
    private String configFile;

    /**
     * ueditor服务器统一请求接口路径
     */
    private String serverUrl;

    /**
     * 资源访问前缀
     */
    private String urlPrefix;

    /**
     * 存储文件的绝对路径 必须使用标准路径"/"作为分隔符
     * 默认为"/"即当前项目所在磁盘根目录
     */
    private String physicalPath = "/";

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getPhysicalPath() {
        return physicalPath;
    }

    public void setPhysicalPath(String physicalPath) {
        this.physicalPath = physicalPath;
    }
}
