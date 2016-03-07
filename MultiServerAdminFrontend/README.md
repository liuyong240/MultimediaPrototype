多媒体样板间Admin前端
===

## 配置开发环境

安装nvm, 详见nvm项目的[github](https://github.com/creationix/nvm)

	curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.31.0/install.sh | bash
	
安装node(>=0.12)

	nvm install v0.12
	
安装项目依赖
	
	npm install
	
安装gulp

	npm install --global gulp-cli
	
启动前端开发环境

	gulp serve
	
## 构建前端资源，上传到Java环境

修改前端版本号

	修改package.json的version字段，打包前端文件时，会把该字段值写入前端js和css文件名
	
	例如 version值为0.0.2
	
	生成的js和css文件名会变为
	xxx-0.0.2.js
	xxx-0.0.2.css
	
执行构建命令

	gulp build
			
构建结果会放在dist目录下，将该目录下的文件复制到以下目录下：
	
	MultiServer/WebContent/admin
	
修改index.jsp引用文件的版本号

	例如 前端版本从0.0.1升级到0.0.2
	index.jsp中引用的资源本件，也需要修改版本号

重新部署，完成前端更新

			

