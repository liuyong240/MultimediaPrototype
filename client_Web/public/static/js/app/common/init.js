/**
 * 项目初始化模块
 * 包含 : 库文件, 初始化视频插件, 验证插件, 顶部导航等
 */
define(['jquery', 'lazyload', 'underscore', 'validate', 'jquery.ui', 'mediaelement'], function ($, _) {

	// 项目初始化
	init();

	/**
	 * 初始化页面, 加载所需框架 + 各配置参数
	 */
	function init() {
		// jquery validate 配置参数
		initValidate();
		initMediaElement();
		initToggleLogin();
	}

	/**
	 * 初始化 jQuery validate 组件
	 */
	function initValidate() {
		jQuery.extend(jQuery.validator.messages, {
			required   : "必须填写",
			remote     : "请修正此栏位",
			minlength  : jQuery.validator.format("最少{0}个字符"),
			maxlength  : jQuery.validator.format("最多{0}个字符"),
			rangelength: jQuery.validator.format("请输入长度为 {0} 至 {1} 之间的字符"),
			min        : jQuery.validator.format("请输入不小于 {0} 的数值"),
			max        : jQuery.validator.format("请输入不大于 {0} 的数值"),
			range      : jQuery.validator.format("请输入 {0} 至 {1} 之间的数值"),
			email      : "请输入有效的电子邮件",
			url        : "请输入有效的网址",
			date       : "请输入有效的日期",
			dateISO    : "请输入有效的日期 (YYYY-MM-DD)",
			number     : "请输入正确的数字",
			digits     : "只可输入数字",
			creditcard : "请输入有效的信用卡号码",
			equalTo    : "两次输入密码不一致",
			extension  : "请输入有效的后缀"
		});

		// 密码格式验证
		jQuery.validator.addMethod("isPassword", function(value, element) {
			var password = /((?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})/;
			return this.optional(element) || (password.test(value));
		}, "必须包含大小写字母和数字, 长度6-20");

	}

	/**
	 * 初始化 MediaElement 组件
	 */
	function initMediaElement() {
		$('video,audio').mediaelementplayer({
			// if the <video width> is not specified, this is the default
			defaultVideoWidth: 800,
			// if the <video height> is not specified, this is the default
			defaultVideoHeight: 600,
			// if set, overrides <video width>
			videoWidth: '100%',
			// if set, overrides <video height>
			videoHeight: '600'
		});

		//var player = new MediaElementPlayer("video,audio");
		//player.play();
	}

	/**
	 * 初始化顶部登入登出菜单
	 */
	function initToggleLogin() {
		$('.navbar-right').on('click', 'a.dropdown-toggle', function () {
			$(this).next('.dropdown-menu').toggle();
		});
	}

});