package com.baidu.ueditor.hunter;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.MultiState;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.spring.EditorController;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class FileManager {

    /**
     * config.json 中的 imageManagerListPath 或 fileManagerListPath 指定要列出图片/文件的目录
     */
    private String dir;
    /**
     * 存储文件的绝对路径
     */
    private String rootPath;
    private String[] allowFiles;
    private int count;
    private String contextPath;

    public FileManager(Map<String, Object> conf) {
        this.rootPath = PathFormat.format(EditorController.properties.getLocal().getPhysicalPath());
        this.dir = (String) conf.get("dir");
        this.allowFiles = this.getAllowFiles(conf.get("allowFiles"));
        this.count = (Integer) conf.get("count");
        this.contextPath = (String) conf.get("contextPath");
    }

    public State listFile(int index) {
        File dir = new File(PathFormat.format(this.rootPath + this.dir));
        State state;
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
        BaseState fileState;
        File file;
        for (Object obj : files) {
            if (obj == null) {
                break;
            }
            file = (File) obj;
            fileState = new BaseState(true);
            fileState.putInfo("url", PathFormat.format(contextPath + "/" + EditorController.properties.getLocal().getUrlPrefix() + "/" + PathFormat.format(file.getPath()).replaceFirst(this.rootPath, "")));
            state.addState(fileState);
        }
        return state;
    }

    private String[] getAllowFiles(Object fileExt) {
        String[] exts;
        String ext;
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
