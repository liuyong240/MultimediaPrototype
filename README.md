## 关于

多媒体样板间是基于阿里云云产品（包括OSS、MTS、MNS及RDS）搭建的基于点播的端到端的解决方案。

在阿里云各云产品的基础上，打通音视频行业用户基本的产品流程，以开源的方式提供从客户端到服务端的整体解决方案。

通过OSS上传文件包括通过应用服务器透传和通过客户端直传两种方案，1.0版本采取的是第一种，通过应用服务器把文件上传到OSS的private bucket。

-**OSS**:对象存储（Object Storage Service)

-**RDS**:云数据库（Relational Database Service)

-**MNS**:分布式消息和通知服务(Message And Notification Service)

-**MTS**:多媒体转码服务（Multimedia Transcoding Service)

## 功能介绍

Web客户端Web音视频上传、列表查看、视频播放

服务端音视频上传及转码服务

服务端后台管理

## 版本

当前版本：1.1.0

## 代码结构说明

MultiServer：样板间服务端代码

MultiServerAdminFrontend：样板间管理控制台前端代码 

client_Web：样板间web段代码  

 
deploy：部署脚本

docs：样板间说明文档 

## 服务端代码说明
src：系统公用模块

src-oss模块：对外提供文件存储相关服务

src-mns模块：消息通知服务

src-mts模块：对外提供视频转码相关服务

src-admin模块：对外提供水印模板管理用户信息获取服务

src-auth模块：对外提供登录、注册服务

src-test：系统单元测试模块

resources模块：系统配置文档

WebContent模块：后台管理前端模块


## 帮助文档
1. [多媒体样板间-使用文档](docs/多媒体样板间-使用文档.pdf)
2. [多媒体样板间-部署文档](docs/多媒体样板间-部署文档.pdf)
3. [多媒体样板间-体验文档](docs/多媒体样板间-体验文档.pdf)


## Q&A
[常见问题](常见问题.md)


## 联系我们
- [阿里云官方介绍网址](https://media.aliyun.com/video/?spm=5176.1890350.1002.4.JEwp6r)  
- 旺旺交流群：1642710247
  

