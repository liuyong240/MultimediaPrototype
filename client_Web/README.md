多媒体样板间 - PC端
===

#概述

客户端使用requirejs管理前端依赖模块，模板引擎使用 swig; 使用bower + r.js 做包管理和项目压缩

项目入口文件为view/home.html，由webserver渲染加载基本数据返回给用户。

webserver使用Koa搭建，pm2管理服务进程。


#本地开发

## 一、环境搭建
### 1 安装

#### 1.1 安装NVM

详见 https://github.com/creationix/nvm

#### 1.2 安装Node.js（ v5.3 ）

nvm install v5.3

nvm use 5.3

#### 1.3 安装NginX

mac安装 : brew install nginx

其他详见 : http://nginx.org/en/download.html

### 2 配置

#### 2.1 nginx配置

    server {
    	listen 3000;
    	server_name 127.0.0.1;

    	location / {
    		proxy_set_header X-Real-IP $remote_addr;
    		proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    		proxy_buffering off;
    		proxy_pass http://127.0.0.1:3100;
    	}

    	location ~ /api/oss {
    		proxy_buffering off;
    		proxy_pass http://127.0.0.1:8081;
    	}

    }


## 二 项目启动

### 1 项目启动

    // 启动 "多媒体样板间" java 工程 (保证端口为 8081)

    // 启动nginx
    nginx

    // 在客户端目录下(client_Web/),启动server
    node app.js

    // 浏览器访问
    http://127.0.0.1:3000/login

    // 用户名和密码
    user : admin
    password : admin