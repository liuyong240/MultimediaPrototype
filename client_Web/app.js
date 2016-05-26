var koa = require('koa');
var _ = require('koa-route');
var app = module.exports = koa();
var onerror = require('koa-onerror');
var serve = require('koa-static');
var session = require('koa-session');
var logger = require('koa-logger');

var utils = require('./module/utils');
var routes = require('./route/init');
var config = require('./conf/config');
var env = process.env.NODE_ENV;

/**
 * App configuration
 */
// 异常处理中间件  -> development
onerror(app);
// 生产环境 config
if (env === 'product') {
	app.use(serve(__dirname + '/dist'));
} else {
	app.use(serve(__dirname + '/public'));
}
app.keys = ['multimediaPrototype'];
app.use(session(app));
app.use(logger());

app.use(function *(next){
	if(!this.config){
		this.config = config;
	}
	yield next;
});


/**
 * 授权中间件 : 访问页面需要登录 ( 除登录,注册页 )
 */
//app.use(utils.authorization);

/**
 * Routes
 */
routes.bind(app);

/**
 * Start app up
 */
app.listen(3100);
console.log('app is running on port 3100');