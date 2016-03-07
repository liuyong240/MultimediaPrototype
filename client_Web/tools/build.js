({
	appDir        : '../public',
	mainConfigFile: '../public/static/js/pages/common.js',
	dir           : '../dist',
	baseUrl       : './static',
	optimizeCss   : 'standard',
	paths         : {
		"utils": "js/app/common/utils",
		"app"  : 'js/app',
		"pages": 'js/pages'
	},
	shim          : {
		"underscore"  : {
			exports: "_"
		},
		"mediaelement": {
			deps: ['jquery']
		}
	},
	modules       : [
		//First set up the common build layer.
		{
			//module names are relative to baseUrl
			name   : 'pages/common',
			//List common dependencies here. Only need to list
			//top level dependencies, "include" will find
			//nested dependencies.
			include: ['utils']
		},

		//Now set up a build layer for each page, but exclude
		//the common one. "exclude" will exclude
		//the nested, built dependencies from "common". Any
		//"exclude" that includes built modules should be
		//listed before the build layer that wants to exclude it.
		//"include" the appropriate "app/main*" module since by default
		//it will not get added to the build since it is loaded by a nested
		//requirejs in the page*.js files.
		{
			//module names are relative to baseUrl/paths config
			name   : 'pages/login',
			include: ['app/main_login'],
			exclude: ['utils', 'pages/common']
		},

		{
			//module names are relative to baseUrl
			name   : 'pages/home',
			include: ['app/main_home'],
			exclude: ['utils', 'pages/common']
		},
		{
			//module names are relative to baseUrl
			name   : 'pages/upload',
			include: ['app/main_upload'],
			exclude: ['utils', 'pages/common']
		},
		{
			//module names are relative to baseUrl
			name   : 'pages/video',
			include: ['app/main_video'],
			exclude: ['utils', 'pages/common']
		}
	]
})
