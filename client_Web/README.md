多媒体样板间 : 客户端
===

#概述

客户端使用requirejs管理前端依赖模块，模板引擎使用 swig; 使用bower + r.js 做包管理和项目压缩

项目入口文件为view/home.html，由webserver渲染加载基本数据返回给用户。

webserver使用Koa搭建，pm2管理服务进程。

#开发环境搭建

## 一、前端部分：

### 1、安装NVM

详见 https://github.com/creationix/nvm

### 2、安装Node.js（ >= v0.12 ）

nvm install v5.3.0
nvm use 5.3.0

### 3、安装cnpm

由于npm资源在墙外，所以最好使用cnpm替换,详见 https://npm.taobao.org/

### 4、安装bower

    cnpm install -g bower

### 5、安装本地依赖

    cnpm install
    bower install

## 二、server部分

### 2、安装NVM、Node.js ( >= v0.12 )、CNPM

略（见前端部分）

### 3、安装NginX

详见 http://nginx.org/en/download.html

### 4、安装Node.js依赖

    cnpm install

### 5、安装pm2

    cnpm install -g pm2

## 三、配置

### koa配置

conf/config.js为server本地服务配置;  
conf/config.product.js为server线上服务配置


### nginx配置

用于将3000端口的请求转至3100
线上作用相同

## 四、开发环境启动

### 本地 node 工程, 联调线上 java 工程

    // 启动nginx
    nginx

    // 启动node server
    pm2 start app_pm2.json

### 本地 node 工程, 联调本地 java 工程

	// 启动nginxg
        nginx

	// 启动node server
		NODE_ENV=development node app/app.js

    // 启动 java 工程
    java 工程地址 : 

## 五、生产环境启动

### 登录信息


### 部署步骤
	cd /home/zhuzhen.bk/project/MultimediaPrototype &&
	nvm use 5.3 &&
	pm2 start app_pm2.json

