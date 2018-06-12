package com.baidu.ueditorspringbootstarter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lihy
 * @version 2018/6/12
 */
@ConfigurationProperties("ue")
public class UdeitorProperties {

    /**
     * config.json 路径
     */
    private String rootPath;

    /**
     * 文件保存路径
     */
    private String dir;

    /**
     * ueditor服务器统一请求接口路径
     */
    private String serverUrl;

    /**
     * 资源访问前缀
     */
    private String urlPrefix;

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
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
}
