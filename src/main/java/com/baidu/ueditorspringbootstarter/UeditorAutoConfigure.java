package com.baidu.ueditorspringbootstarter;

import com.baidu.ueditorspringbootstarter.baidu.ueditor.ActionEnter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author lihy
 * @version 2018/6/12
 */
@Configuration
@Controller
@EnableConfigurationProperties(UdeitorProperties.class)
public class UeditorAutoConfigure extends WebMvcConfigurerAdapter {

    @Autowired
    UdeitorProperties properties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                ServletOutputStream out = null;
                try {
                    out = response.getOutputStream();
                    if (request.getRequestURI().equals(properties.getServerUrl())) {
                        out.print(new ActionEnter(request, properties.getRootPath()).exec());
                    } else if (request.getRequestURI().contains(properties.getUrlPrefix())) {
                        response.setHeader("Pragma", "no-cache");
                        response.setHeader("Cache-Control", "no-cache");
                        response.setDateHeader("Expires", 0);
                        response.setContentType("image/jpeg");
                        String filename = request.getRequestURI().substring(properties.getUrlPrefix().length(), request.getRequestURI().length());
                        BufferedImage buffImg = ImageIO.read(new File(filename));
                        ImageIO.write(buffImg, "png", out);
                    } else {
                        out.print(200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
                // false直接返回response
                return false;
            }
        }).addPathPatterns(properties.getServerUrl(), properties.getUrlPrefix() + "/**");
    }
}
