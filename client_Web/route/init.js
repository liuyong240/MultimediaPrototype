/**
 * Created by bian17888 on 15/11/19.
 */

var _ = require('koa-route');

var routes = [
	{
		path      : '/',
		method    : 'get',
		controller: require('../controller/homeRoutes').home
	},
	{
		path      : '/myvideo',
		method    : 'get',
		controller: require('../controller/homeRoutes').myVideo
	},
	{
		path      : '/upload',
		method    : 'get',
		controller: require('../controller/homeRoutes').showUpload
	},
	{
		path      : '/api/oss/putMultiAndPicDemo',
		method    : 'post',
		controller: require('../controller/homeRoutes').upload
	},
	{
		path      : '/video/get/:id',
		method    : 'get',
		controller: require('../controller/homeRoutes').video
	},
	{
		path      : '/api/oss/deleteMedia',
		method    : 'get',
		controller: require('../controller/homeRoutes').delVideo
	},
	{
		path      : '/login',
		method    : 'get',
		controller: require('../controller/loginRoutes').show
	},
	{
		path      : '/auth/api/login',
		method    : 'post',
		controller: require('../controller/loginRoutes').login
	},
	{
		path      : '/logout',
		method    : 'get',
		controller: require('../controller/loginRoutes').logout
	},
	{
		path      : '/auth/api/signup',
		method    : 'post',
		controller: require('../controller/loginRoutes').register
	}
];

exports.bind = function (app) {

	routes.forEach(function (value, key) {
		app.use(_[value.method](value.path, value.controller))
	})

}