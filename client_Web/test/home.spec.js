/**
 * Created by bian17888 on 15/10/20.
 */
var app = require('../app');
var request = require('supertest').agent(app.listen());
var should = require('should');
var co = require('co');

var config = require('../conf/config');
var host;

before(function(){
	host = config.server.domain + ':' + config.server.port;
});

describe('The home page', function () {

	it('displays nicely without errors', function (done) {
		request
			.get('/')
			.expect(200)
			.expect('Content-Type',/html/)
			.end(done)
	});

	it('Lists all the video info in the database', function (done) {
		request
			.get('/')
			.expect(200)
			.expect(function(res){
				res.text.should.containEql('rrr');
			})
			.end(done)
	});



})