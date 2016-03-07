/**
 * Created by bian17888 on 15/11/16.
 */
var fs = require('fs');
var join = require('path').join;
var superagent = require('superagent');

var root = process.cwd();
var config = require('../conf/config');


/**
 * 假数据
 */
function importMock(_path) {
	var path = root + '/mock' + _path + '.json',
		datas = fs.readFileSync(path, 'utf-8', function (err) {
			if (err) {
				console.error(err);
				datas = {};
				datas.data = {};
				datas.message = 'error in reading file ';
			} else {
				datas = JSON.parse(datas);
			}
		});
	return datas;
};

/**
 * 授权中间件 : 访问页面需要登录 ( 除登录,注册页 )
 */
function *authorization (next) {
	var url = this.request.url;
	if (url === '/') {
		if (this.session && this.session.userid) {
			yield next;
		} else {
			this.status = 301;
			this.redirect('/login');
		}
	}
	yield next;
}

/**
 * 请求转发
 * @param type, 请求类型"get post"
 * @param url, 请求地址
 * @param data, 请求数据
 * @returns {Function}
 * @private
 */
function _request(type, url, data){
	return function (callback) {
		var _url = config.server.domain + ':' + config.server.port + url;
		// get post 请求不同处理方式
		if (type === 'get') {
			superagent
				.get(_url)
				.end(function(err, res){
					// tofix : 此处添加异常处理
					if (err) {
						// tofix : 此处添加异常处理
						callback(null, res);
					} else {
						callback(err, res)
					}
				})
		} else {
			superagent
				.post(_url)
				.send(data)
				.type('form')
				.end(function(err, res){
					if (err) {
						// tofix : 此处添加异常处理
						callback(null, res);
					} else {
						callback(err, res)
					}
				})
		}
	}
}


/**
 * get 请求转发 + cookie, 仅用于我的上传页
 * @param url, 请求地址
 * @returns {Function}
 */
function _requestCookie(url, cookieStr){
	return function (callback) {
		var _url = config.server.domain + ':' + config.server.port + url;
		superagent
			.get(_url)
			.set('Cookie',cookieStr)
			.end(function(err, res){
				if (err) {
					callback(null, res);
				} else {
					callback(err, res)
				}
			})
	}

}


/**
 * @func _getSessionId
 * @desc 私有方法 : 登录成功后, 获取后台返回的JSESSIONID;
 * @param {string} strSetCookie - 字符串JSESSIONID
 * @returns {string} - 后台返回的JSESSIONID
 * @private
 */
function getSessionId(strSetCookie){
	var sessionTemp = strSetCookie.split(';')[0].split('=');
	var sessionIdValue = sessionTemp[1];
	return sessionIdValue;
}

module.exports = {
	importMock : importMock,
	authorization : authorization,
	getSessionId : getSessionId,
	_request : _request,
	_requestCookie : _requestCookie
}