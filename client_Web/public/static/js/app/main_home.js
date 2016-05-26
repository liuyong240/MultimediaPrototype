/**
 * Created by bian17888 on 16/1/4.
 */

define(['utils'], function(utils) {
  'use strict';
  $(function() {

    init();

    /**
     * 初始化页面
     */
    function init() {
      bindEvent();
      initVideoHover();
    };
    function bindEvent() {
      // 延迟加载图片
      $("img.lazy").lazyload();

      $('.home-wrap')
        .on('click', '.btn-delete', delVideo)
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

      utils.gbAjax(params, fnOk);

      function fnOk(data) {
        var status = data.code;

        if (status === 0) {
          //utils.errorTip('删除成功');
          $li.hide('normal', function() {
            $li.remove();
          })
        } else {
          utils.infoTip('error-tip', '删除失败');
        }
      }
    }

    /**
     * 视频列表 hover 效果
     */
    function initVideoHover () {
      var $li = $('.videos li');
      $li.hover(
        function(){
          // 升起效果
          $(this).find('.div-cont').stop(true,true).animate({
            bottom : '0px'
          },'fast', 'swing');
          // 显示删除按钮
          $(this).find('.btn-delete').show();
        },function(){
          $(this).find('.div-cont').stop(true,true).animate({
            bottom : '-110px'
          },'fast', 'swing');
          $(this).find('.btn-delete').hide();
        }
      );
    }

  })
})