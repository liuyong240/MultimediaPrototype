多媒体样板间iOS客户端demo
===

需要xcode 7.0以上版本

在项目目录下执行安装依赖

	pod install
	
配置后端服务地址

	打开 /TakeThat/Service/APIService.swift 文件
	修改 APIService类，将backendHost的值改为后端服务的IP和端口
	
构建项目，发布到测试机，即可使用本项目的Demo

如果没有iOS开发者帐号，可以进入[苹果开发者的官方网页](https://developer.apple.com/programs/enroll/cn/)，根据提示进行注册