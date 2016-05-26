/**
 * Created by bian17888 on 15/10/22.
 */

 define(['init'], function(){

	 /**
	  * 通用 ajax 方法
	  * @param params
	  * @param fnOk
	  * @param fnError
	  * @constructor
	  */
	 function gbAjax (params, fnOk, fnError) {
		 $.ajax({
			 type      : params.type || 'get',
			 url       : params.url,
			 dataType  : 'json',
			 data      : params.data || {},
			 success   : fnOk || function(data){console.log(data)},
			 error     : function (error) {
				 console.log(error);
				 infoTip('error-tip','请求失败');
			 },
			 beforeSend: function () {
				 var dom = '<div style="opacity: 0" class="ajax-loading-wrap"><img class="ajax-loading" src="/static/images/loading.gif" /></div>';
				 $('#' + params.id).html(dom);
				 $('.ajax-loading-wrap').animate({'opacity':1},1500);
			 },
			 complete  : function () {
				 $('.ajax-loading-wrap').remove();
			 }
		 })
	 }


	 /**
	  * 顶部 Tip 弹窗 : 用于提示错误信息
	  * @params className, 弹窗类型(error-tip. success-tip)
	  */
	 function infoTip(className, msg){
		 var dom = '<div class="'+ className + '"></div>';
		 $('body').append(dom);

		 $('.'+className).text(msg);
		 $('.'+className).slideDown();
		 setTimeout(function(){
			 $('.'+className).slideUp(function(){
				 $('.'+className).remove();
			 });
		 },3000)
	 }


	 /**
	  * 导出模块
	  */
	 return {
		 gbAjax : gbAjax,
		 infoTip : infoTip
	 }

 })