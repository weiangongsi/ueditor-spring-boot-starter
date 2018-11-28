package com.baidu.ueditorspringbootstarter.baidu.ueditor.upload;

import com.baidu.ueditorspringbootstarter.UeditorAutoConfigure;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.PathFormat;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.define.AppInfo;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.define.BaseState;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.define.FileType;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.define.State;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BinaryUploader {

    public static final State save(HttpServletRequest request, Map<String, Object> conf) {
        boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;
        if (!ServletFileUpload.isMultipartContent(request)) {
            return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
        }
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        if (isAjaxUpload) {
            upload.setHeaderEncoding("UTF-8");
        }
        try {
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            Collection<MultipartFile> files = mRequest.getFileMap().values();
            // 没有上传数据
            if (files.size() == 0) {
                return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
            }
            // 获得文件
            MultipartFile file = files.iterator().next();
            // 文件名
            String originFileName = file.getOriginalFilename();
            // 文件扩展名
            String suffix = FileType.getSuffixByFilename(originFileName);
            // 不符合文件类型
            if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }
            long maxSize = ((Long) conf.get("maxSize")).longValue();
            // 文件大小超出限制
            if (maxSize < file.getSize()) {
                return new BaseState(false, AppInfo.MAX_SIZE);
            }
            String savePath = (String) conf.get("savePath");
            savePath = savePath + suffix;
            savePath = PathFormat.parse(savePath, originFileName);
            String physicalPath = (UeditorAutoConfigure.properties.getPhysicalPath() + savePath).replace("//", "/");
            State storageState = StorageManager.saveFileByInputStream(file.getInputStream(), physicalPath);
            if (storageState.isSuccess()) {
                storageState.putInfo("url", (UeditorAutoConfigure.properties.getUrlPrefix() + PathFormat.format(savePath)).replace("//", "/"));
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName + suffix);
            }
            return storageState;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

    private static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);

        return list.contains(type);
    }
}
