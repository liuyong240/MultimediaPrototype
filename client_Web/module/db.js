/**
 * Created by bian17888 on 15/11/24.
 */
var monk = require('monk');
var wrap = require('co-monk');
var db = monk('localhost/multimediaPrototype');

var users = wrap(db.get('users'));
var videos = wrap(db.get('videos'));

module.exports = {
	users: users,
	videos: videos
}