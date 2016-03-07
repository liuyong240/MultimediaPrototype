/**
 * Created by bian17888 on 15/10/20.
 */
var render = require('../module/render');
var parse = require('co-body');
var db = require('../module/db');

var utils = require('../module/utils');

/*
 * 登录页
 * */
function *show() {
	var data = {};
	this.body = yield render('login', data);
}

/**
 * 登录
 */
function *login() {
	var user = yield parse(this);
	var res = yield utils._request('post', this.url, user);

	// tofix : node帮忙做兼容处理 -> java端返回规范, 此处 res.body = undefined
	if (res.status === 200) {
		res.body = {code: 0, data: null, error: null};

		// 写入后端返回的 JSESSIONID cookie
		this.cookies.set('JSESSIONID',utils.getSessionId(res.headers['set-cookie'][0]));
		this.session.username = user.username;


	} else {
		res.body = {code: res.status, data: null, error: '用户名和密码输入不正确'};
	}

	this.status = 200;
	this.body = res.body;

}

/**
 * 登出
 */
function *logout() {
	this.cookies.set('JSESSIONID','');
	this.session = null;
	this.response.redirect('/');
}

/**
 * 注册
 */
function *register() {
	var user = yield parse(this);
	var res = yield utils._request('post', this.url, user);

	// tofix : node帮忙做兼容处理 -> java端返回规范, 此处 res.body = undefined
	if (res.status === 200) {
		res.body = res.body;
	} else {
		res.body = {code: res.status, data: null, error: '注册失败'};
	}
	//this.session.username = user.username;
	this.status = 200;
	this.body = res.body;

}

module.exports = {
	show    : show,
	login   : login,
	logout  : logout,
	register: register
}
