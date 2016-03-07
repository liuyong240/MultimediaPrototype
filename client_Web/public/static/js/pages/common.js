/**
 * Created by bian17888 on 15/11/12.
 */

/**
 * require config
 */
require.config({
	baseUrl: '/static/',
	paths  : {
		"jquery"      : "libs/jquery/dist/jquery.min",
		"lazyload"    : "libs/jquery_lazyload/jquery.lazyload",
		"mediaelement": "libs/mediaelement/build/mediaelement-and-player.min",
		"underscore"  : "libs/underscore/underscore-min",
		"bootstrap"   : "libs/bootstrap/dist/js/bootstrap.min",
		"validate"    : "libs/jquery-validation/dist/jquery.validate.min",
		"jquery.ui"   : "libs/jquery-ui-redmond/jquery-ui.min",
		"init"        : "js/app/common/init",
		"utils"       : "js/app/common/utils",
		"app"         : 'js/app'
	},
	shim   : {
		"underscore"  : {
			exports: "_"
		},
		"mediaelement": {
			deps: ['jquery']
		},
		"lazyload" : {
			deps : ['jquery']
		},
		"jquery.ui" : {
			deps : ['jquery']
		}
	}
});

