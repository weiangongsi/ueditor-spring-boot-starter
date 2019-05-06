package com.baidu.ueditor.spring;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.hunter.FileManager;
import com.baidu.ueditor.upload.Base64Uploader;
import com.baidu.ueditor.upload.BinaryUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 百度编辑器请求
 *
 * @author lihy
 * @version 2018/11/28
 */
@RestController
@EnableConfigurationProperties(EditorProperties.class)
public class EditorController {

    public static EditorProperties properties;

    public static EditorUploader editorUploader;

    @Autowired
    public EditorController(EditorProperties properties, @Lazy EditorUploader editorUploader) {
        EditorController.properties = properties;
        EditorController.editorUploader = editorUploader;
    }

    /**
     * 百度编辑器请求
     *
     * @param request  请求
     * @param response 返回
     * @author lihy
     */
    @RequestMapping({"${ue.server-url}"})
    public void server(HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            out.print(new ActionEnter(request, properties.getConfigFile()).exec());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Service
    @ConditionalOnMissingBean(EditorUploader.class)
    class DefaultEditorConfig implements WebMvcConfigurer {

        /**
         * resource配置
         * @param registry registry
         */
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler(properties.getLocal().getUrlPrefix() + "**")
                    .addResourceLocations("file:" + properties.getLocal().getPhysicalPath())
                    .setCacheControl(CacheControl.maxAge(2, TimeUnit.DAYS));
        }

        @Component
        class DefaultEditorUploader implements EditorUploader {

            @Override
            public State binaryUpload(HttpServletRequest request, Map<String, Object> conf) {
                return BinaryUploader.save(request, conf);
            }

            @Override
            public State base64Upload(HttpServletRequest request, Map<String, Object> conf) {
                String filedName = (String) conf.get("fieldName");
                return Base64Uploader.save(request.getParameter(filedName), conf);
            }

            @Override
            public State listImage(int index, Map<String, Object> conf) {
                return new FileManager(conf).listFile(index);
            }

            @Override
            public State listFile(int index, Map<String, Object> conf) {
                return new FileManager(conf).listFile(index);
            }

        }
    }


}
