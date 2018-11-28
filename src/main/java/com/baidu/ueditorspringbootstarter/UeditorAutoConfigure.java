package com.baidu.ueditorspringbootstarter;

import com.baidu.ueditorspringbootstarter.baidu.ueditor.ActionEnter;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.PathFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 百度编辑器请求
 *
 * @author lihy
 * @version 2018/11/28
 */
@RestController
@EnableConfigurationProperties(UdeitorProperties.class)
public class UeditorAutoConfigure {

    public static UdeitorProperties properties;

    @Autowired
    public UeditorAutoConfigure(UdeitorProperties properties) {
        UeditorAutoConfigure.properties = properties;
    }


    /**
     * 百度编辑器请求
     *
     * @param request 请求
     * @return 处理结果
     * @author lihy
     */
    @RequestMapping({"${ue.server-url}"})
    public String server(HttpServletRequest request) {
        return new ActionEnter(request, properties.getConfigFile()).exec();
    }

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
}
