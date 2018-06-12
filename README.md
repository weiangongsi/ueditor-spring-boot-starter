# ueditor-spring-boot-starter
ueditor-spring-boot-starter
maven package or install<br>
in your project

pom.xml add
```
<dependency>
  <groupId>com.baidu</groupId>
  <artifactId>ueditor-spring-boot-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```
application.yml
```
ue:
  root-path: src/main/resources/static/ueditor/jsp
  server-url: /ueditor
  url-prefix: /files
 ```
copy ueditor1_4_3_3-utf8-jsp to resources/static
