# ueditor-spring-boot-starter
1. #### 文件导入<br>
   * 新建springboot项目
   * 不需要下载本项目，jar包已经上传到maven仓库
   * pom文件引入 
      ``` 
           <dependency>
                    <groupId>com.dcssn</groupId>
                    <artifactId>ueditor-spring-boot-starter</artifactId>
                    <version>1.0.0</version>
           </dependency>
      ```
   * 下载百度编辑器源码 链接：[最新版本1.4.3.3 Jsp UTF-8版本](https://ueditor.baidu.com/build/build_down.php?n=ueditor&v=1_4_3_3-utf8-jsp)<br>
   * 创建ueditor目录 resources > static > ueditor ，将源码拷贝到目录中
   * jsp目录只保留 config.json 文件即可
2. #### 项目配置<br>
   * application.yml
      ```application.yml
         ue:
           config-file: static/ueditor/jsp/config.json #resources目录下配置文件的位置
           server-url: /ueditor.do #服务器统一请求接口路径
           url-prefix: /file/ #"/"结尾
           physical-path: C:/upload/ #存储文件的绝对路径 必须使用标准路径"/"作为分隔符 默认为"/"即当前项目所在磁盘根目录
      ```
   * static/ueditor/ueditor.config.js <br>
      将serverUrl 改为application.yml 中server.servlet.context-path(如果你设置了此值则加上) + ue.server-url 的值
   * config.json <br>
        <font color=#FF4040>图片访问路径前缀（imageUrlPrefix）、视频访问路径前缀、文件访问路径前缀不要赋值，会影响回显，其余参数可以按照百度文档修改</font>
   * 上传文件大小 <br>
      spring上传文件默认最大1MB，上传文件大小会先被spring限制，config.json文件大小限制要小于spring的设置，我们可以将spring的限制设大点
      ```
        spring:
          servlet:
            multipart:
              max-file-size: 100MB
      ```
3. #### 测试     
   * 新建Controller 添加mapping
      ```
         @GetMapping("/ue")
         public String index() {
             return "ue";
         }
      ```
   * 在templates下新建页面ue.html
      ```ue.html
         <!DOCTYPE html>
         <html lang="UTF-8" xmlns:th="http://www.springframework.org/schema/jdbc">
         <head>
             <meta charset="UTF-8"/>
             <title>ueditor</title>
             <style>
                 #editor {
                     width: 1024px;
                     height: 500px;
                 }
             </style>
         </head>
         <body>
         <div id="editor" type="text/plain"></div>
         <script th:src="@{/ueditor/ueditor.config.js}"></script>
         <script th:src="@{/ueditor/ueditor.all.min.js}"></script>
         <script th:src="@{/ueditor/lang/zh-cn/zh-cn.js}"></script>
         <script>
             UE.getEditor('editor');
         </script>
         </body>
         </html>
      ```
      如有问题可以加群：806893930 ，我第一次建群，里面就几个人，欢迎你的加入
4. #### 参考百度文档
    代码只修改了上传和获取文件列表的方法，添加了服务器统一请求接口路径的拦截器，没有别的改动，[百度文档](http://fex.baidu.com/ueditor/)
5. #### 项目案例
   [https://github.com/weiangongsi/ueditor-spring-boot-starter-example](https://github.com/weiangongsi/ueditor-spring-boot-starter-example)