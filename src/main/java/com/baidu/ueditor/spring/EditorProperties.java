package com.baidu.ueditor.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 百度编辑器配置
 *
 * @author lihy
 * @version 2018/6/12
 */
@Data
@ConfigurationProperties("ue")
public class EditorProperties {

    /**
     * config.json 路径
     */
    private String configFile;

    /**
     * ueditor服务器统一请求接口路径
     */
    private String serverUrl;

    private Qiniu qiniu;

    private Local local;

    /**
     * 上传到本地参数
     */
    @Data
    public static class Local {
        /**
         * 资源访问前缀
         */
        private String urlPrefix;
        /**
         * 存储文件的绝对路径 必须使用标准路径"/"作为分隔符
         * 默认为"/"即当前项目所在磁盘根目录
         */
        private String physicalPath = "/";
    }

    /**
     * 七牛上传参数
     */
    @Data
    public static class Qiniu {
        private String accessKey;
        private String secretKey;
        private String cdn;
        private String bucket;
        private String zone;
    }

}
