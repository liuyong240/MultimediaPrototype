/**
 * Created by bian17888 on 15/10/20.
 */
var views = require('co-views');


// setup views mapping .html
// to the swig template engine
module.exports = views(__dirname + './../view', {
	map: { html: 'swig' },
	cache : false
});