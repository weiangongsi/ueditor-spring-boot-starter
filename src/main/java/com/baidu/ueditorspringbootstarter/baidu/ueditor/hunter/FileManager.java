package com.baidu.ueditorspringbootstarter.baidu.ueditor.hunter;

import com.baidu.ueditorspringbootstarter.UeditorAutoConfigure;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.PathFormat;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.define.AppInfo;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.define.BaseState;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.define.MultiState;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.define.State;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class FileManager {

    private String dir = null;
    private String rootPath = null;
    private String[] allowFiles = null;
    private int count = 0;
    private String contextPath = "";


    public FileManager(Map<String, Object> conf) {
        this.rootPath = PathFormat.format(UeditorAutoConfigure.properties.getPhysicalPath());
        this.dir = PathFormat.format(this.rootPath + (String) conf.get("dir"));
        this.allowFiles = this.getAllowFiles(conf.get("allowFiles"));
        this.count = (Integer) conf.get("count");
        this.contextPath = (String) conf.get("contextPath");
    }

    public State listFile(int index) {
        File dir = new File(this.dir);
        State state = null;
        if (!dir.exists()) {
            return new BaseState(false, AppInfo.NOT_EXIST);
        }
        if (!dir.isDirectory()) {
            return new BaseState(false, AppInfo.NOT_DIRECTORY);
        }
        Collection<File> list = FileUtils.listFiles(dir, this.allowFiles, true);
        if (index < 0 || index > list.size()) {
            state = new MultiState(true);
        } else {
            Object[] fileList = Arrays.copyOfRange(list.toArray(), index, index + this.count);
            state = this.getMyState(fileList);
        }
        state.putInfo("start", index);
        state.putInfo("total", list.size());
        return state;
    }

    private State getMyState(Object[] files) {
        MultiState state = new MultiState(true);
        BaseState fileState = null;
        File file = null;
        for (Object obj : files) {
            if (obj == null) {
                break;
            }
            file = (File) obj;
            String absolutePath = PathFormat.format(file.getAbsolutePath());
            fileState = new BaseState(true);
            if (this.rootPath.startsWith("/")) {
                fileState.putInfo("url", PathFormat.format(contextPath + "/" + UeditorAutoConfigure.properties.getUrlPrefix() + "/" + file.getPath()).replaceFirst(this.rootPath, ""));
            } else {
                fileState.putInfo("url", PathFormat.format(contextPath + "/" + UeditorAutoConfigure.properties.getUrlPrefix() + "/" + absolutePath).replaceFirst(this.rootPath, ""));
            }
            state.addState(fileState);
        }
        return state;
    }

    private String[] getAllowFiles(Object fileExt) {
        String[] exts = null;
        String ext = null;
        if (fileExt == null) {
            return new String[0];
        }
        exts = (String[]) fileExt;
        for (int i = 0, len = exts.length; i < len; i++) {
            ext = exts[i];
            exts[i] = ext.replace(".", "");
        }
        return exts;
    }

}
