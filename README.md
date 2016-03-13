## 关于

多媒体样板间是基于阿里云云产品（包括OSS、MTS、MNS及RDS）搭建的基于点播的端到端的解决方案。 在阿里云各云产品的基础上，打通音视频行业用户基本的产品流程，以开源的方式提供从客户端到服务端的整体解决方案。 通过OSS上传文件包括通过应用服务器透传和通过客户端直传两种方案，1.0版本采取的是第一种，通过应用服务器把文件上传到OSS的private bucket。

-**OSS**:对象存储（Object Storage Service)

-**RDS**:云数据库（Relational Database Service)

-**MNS**:分布式消息和通知服务(Message And Notification Service)

-**MTS**:多媒体转码服务（Multimedia Transcoding Service)

## 功能介绍

客户端(Web/IOS)音视频上传、列表查看、视频播放

服务端音视频上传及转码服务

服务端后台管理

## 版本

当前版本：1.0.0

## 服务端代码说明
src：系统公用模块

src-sts模块：提供安全凭证，用来授予临时的访问权限（服务端接口已经完成，使用案例可以参考src-test/multimedia/sts目录）

src-oss模块：对外提供文件存储相关服务

src-ocs模块：系统缓存服务

src-mns模块：消息通知服务

src-mts模块：对外提供视频转码相关服务

src-admin模块：对外提供水印模板管理用户信息获取服务

src-auth模块：对外提供登录、注册服务

src-test：系统单元测试模块

resources模块：系统配置文档

WebContent模块：后台管理前端模块

## 服务端安装步骤

请参见docs下多媒体样板间-使用文档文档等帮助文档

## 帮助文档
1. [多媒体样板间(Web客户端)_开发部署文档](docs/多媒体样板间(Web客户端)_开发部署文档.pdf)
2. [多媒体样板间(Web客户端)_使用文档](docs/多媒体样板间(Web客户端)_使用文档.pdf)
3. [多媒体样板间(服务端)_开发部署文档](docs/多媒体样板-服务端文档.pdf)
4. [多媒体样板间使用文档](docs/多媒体样板-服务端文档.pdf)



