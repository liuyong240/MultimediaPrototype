/**
 * Created by bian17888 on 16/1/4.
 */

define(['utils'], function (utils) {

	'use strict';

	$(function () {

		init();
		/**
		 * 初始化页面
		 */
		function init() {
			// 登录表单显示动画
			formAnimate('login_info', 'register_info');
			validFrom();
			bindEvent();
		};
		function bindEvent() {
			$('.login-wrap')
				.on('click', '.toogle-dialog', toogleDialog)
		}

		/**
		 * 登录/注册框, 显隐切换
		 * @param idShow
		 * @param idHide
		 */
		function formAnimate(idShow, idHide) {
			// 隐藏
			$('#' + idHide).hide();
			// 显示
			$('#' + idShow).css({"display": "block", "opacity": 0.5, "margin-top": "100px"});
			$('#' + idShow).animate({"opacity": 1, "margin-top": "150px"}, 700);
		}

		/**
		 * 注册登录按钮, 对应弹窗显隐
		 * @returns {*}
		 */
		function toogleDialog() {
			var type = $(this).data('type');
			if (type === 'login') {
				formAnimate('login_info', 'register_info')
			} else {
				formAnimate('register_info', 'login_info')
			}
		}

		function validFrom(event) {

			var validLogin = {
				rules        : {
					username: {
						required: true
					},
					password: {
						required: true
					}
				},
				message      : {},
				submitHandler: function (form) {
					var data = $(form).serialize();
					var params = {};
					params.type = 'post';
					params.url = '/auth/api/login';
					params.data = data;
					utils.gbAjax(params, fnOk);
					// 回调函数 : 返回数据成功
					function fnOk(data){
						var code = data.code,
							error = data.error;
						if (code === 0) {
							window.location.href = '/';
						} else {
							utils.infoTip('error-tip',error);
						}
					}
				}
			};
			var objReg = {
				rules: {
					username: {
						required: true
					},
					password: {
						required: true,
						isPassword : true
					},
					repassword   : {
						required: true,
						equalTo : '#password'
					}
				},
				submitHandler: function (form) {
					var data = $(form).serialize();
					var params = {};
					params.type = 'post';
					params.url = '/auth/api/signup';
					params.data = data;
					utils.gbAjax(params, fnOk);
					// 回调函数 : 返回数据成功
					function fnOk(data){
						var code = data.code,
							error = data.error;
						if (code === 0) {
							utils.infoTip('success-tip','注册成功, 请登录!');
							setTimeout(function(){
								window.location.href = '/login';
							},2000)
						} else {
							utils.infoTip('error-tip',error);
						}
					}
				}
			};
			var validReg = {};
			validReg = $.extend(validReg, validLogin, objReg);

			$('#login_info').find('form').validate(validLogin);
			$('#register_info').find('form').validate(validReg);

			return false;
		}
	})

})