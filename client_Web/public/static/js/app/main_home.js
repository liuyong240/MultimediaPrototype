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
			bindEvent();
			// 延迟加载图片
			$("img.lazy").lazyload();
		};
		function bindEvent() {
			$('.home-wrap')
				.on('click', '.r-btn-close', delVideo)
				.on('click', '.tool-bar button', toogleVideoList)
		}

		/**
		 * 删除视频
		 */
		function delVideo() {
			var $li = $(this).closest('li'),
				id = $li.data('data').id,
				params = {},
				data = {};

			params.url = '/api/oss/deleteMedia';
			data.id = id;
			params.data = data;

			utils.gbAjax(params,fnOk);

			function fnOk(data) {
				var status = data.code;

				if (status === 0) {
					//utils.errorTip('删除成功');
					$li.hide('normal', function(){
						$li.remove();
					})
				} else {
					utils.infoTip('error-tip','删除失败');
				}
			}

		}

		/**
		 *
		 */
		function toogleVideoList () {
			var $btn = $(this),
				$btns = $btn.closest('p').find('button'),
				$ul_wrap = $('.ul-wrap'),
				$ul = $('.videos ul'),
				$li = $ul.find('li'),
				type = $btn.data('type');

			$btns.removeClass('btn-info').addClass('btn-default');
			$btn.addClass('btn-info').removeClass('btn-default');

			// 图片列表模式
			if (type === 'view_thumb') {
				$ul_wrap.removeClass('ul-wrap-list').addClass('ul-wrap-thumb');
				$ul.addClass('row');
				$li.addClass('col-md-4');
				$li.removeClass('row');
				$li.find('.r-p-pic').removeClass('col-md-4');
				$li.find('.r-div-cont').removeClass('col-md-8');
			} else {
				$ul_wrap.removeClass('ul-wrap-thumb').addClass('ul-wrap-list');
				$ul.removeClass('row');
				$li.removeClass('col-md-4');
				$li.addClass('row');
				$li.find('.r-p-pic').addClass('col-md-4');
				$li.find('.r-div-cont').addClass('col-md-8');
			}

		}

	})
})