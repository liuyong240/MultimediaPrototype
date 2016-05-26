<%--
  Created by IntelliJ IDEA.
  User: haihong.xiahh
  Date: 2015/11/17
  Time: 16:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>多媒体样板间</title>
    <link rel="stylesheet" href="javadoc/github-markdown-css.css"/>
</head>


<body class="markdown-body" >
<div class="box-wrap" style="width:60%; padding : 0px 0px 30px; margin: 0px auto;">
    <h1> 多媒体样板间项目背景 </h1> 多媒体样板间是基于阿里云云产品（包括OSS、MTS、MNS及RDS）搭建的基于点播的端到端的解决方案。
    <BR> 在强绑定阿里云各云产品的基础上，打通音视频行业用户基本的产品流程，以开源的方式提供从客户端到服务端的整体解决方案。
    <h1>多媒体样板间1.0</h1> 通过OSS上传文件包括通过应用服务器透传和通过客户端直传两种方案，1.0版本采取的是第一种，通过应用服务器把文件上传到OSS的private bucket。
    <BR>
    <h2>功能</h2> 1. Web客户端音视频上传、列表查看、视频播放
    <BR> 2. 服务端音视频上传及转码服务
    <BR> 3. 服务端后台管理
    <BR>
    <h2>设计文档</h2>
    <BR> 1. <a href="/info/deploy_index_simple.html">架构图</a>
    <BR> 2. <a href="/info/deploy_index.html">数据流程图</a>
    <BR> 3. <a href="/info/code_index.html">代码结构图</a>
    <BR> 4. <a href="/swagger/index.html">api使用文档</a>
    <BR> 5. <a href="/javadoc/apidocs/index.html">javadoc</a>
        <BR>
    <h2><a href="http://127.0.0.1:3000">PC客户端</a></h2>
    <h2><a href="/admin">管理后台</a></h2>
    <h2>使用的阿里云云产品</h2> 1. <a href="https://www.aliyun.com/product/oss" target="_blank"> OSS（对象存储服务)</a>
    <BR> 2. <a href="https://www.aliyun.com/product/mts" target="_blank"> MTS（媒体转码服务)</a>
    <BR> 3. <a href="https://www.aliyun.com/product/mns" target="_blank"> MNS（消息通知服务)</a>
    <BR> 4. <a href="https://www.aliyun.com/product/rds" target="_blank"> RDS（云数据库)</a>
    <BR> 5. <a href="https://www.aliyun.com/product/ecs" target="_blank"> ECS（云服务器)</a>
    <BR>
</div>
</body>
</html>
