/**
 * Created by bian17888 on 15/10/20.
 */
var app = require('../app');
var request = require('supertest').agent(app.listen());
var expectJS = require('expect.js');
var co = require('co');

/**
 * 查询相关的测试用例
 */
describe('The login and register pages ', function () {

	/**
	 * 登录页面
	 */
	it('login sucessfully!', function (done) {
		var user = {
			username :'test',
			password : 'test'
		}
		request
			.post('/auth/api/login')
			.send(user)
			.expect(200)
			.expect(function(res){
				res.body.code.should.equal(0)
			})
			.end(done)
	});

	/**
	 * 注册页面
	 */
	it('register sucessfully!', function (done) {
		var timestamp = new Date().getTime();
		var user = {
			username :'test'+timestamp,
			password : 'Test123',
			repassword : 'Test123'
		}
		request
			.post('/auth/api/signup')
			.send(user)
			.expect(200)
			.expect(function(res){
				console.log('==========');
				console.log(res.body);
				res.body.code.should.equal(0);
			})
			.end(done)
	});




})