package com.baidu.ueditor.spring;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.hunter.FileManager;
import com.baidu.ueditor.upload.Base64Uploader;
import com.baidu.ueditor.upload.BinaryUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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

    @RestController
    @ConditionalOnMissingBean(EditorUploader.class)
    class FileController {
        /**
         * 获取文件请求
         *
         * @param request  HttpServletRequest
         * @param response HttpServletResponse
         * @author lihy
         */
        @RequestMapping({"${ue.url-prefix}/**"})
        public void readFile(HttpServletRequest request, HttpServletResponse response) {
            ServletOutputStream out = null;
            InputStream in = null;
            try {
                out = response.getOutputStream();
                String uri = request.getRequestURI();
                String filename = uri.substring(uri.indexOf(properties.getUrlPrefix()) + properties.getUrlPrefix().length(), request.getRequestURI().length());
                in = new FileInputStream(PathFormat.format(properties.getPhysicalPath() + "/" + filename));
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
