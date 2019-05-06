package com.baidu.ueditor.spring.support.qiniu;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启七牛云上传
 *
 * @author lhy
 * @since 2019-05-06
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(QiniuUploader.class)
public @interface EnableQiniuUploader {
}
