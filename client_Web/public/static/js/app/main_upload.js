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
      validFrom();
    };
    function bindEvent() {
      //$('.navbar-right').on('click', 'a.dropdown-toggle', toggleLogin);
    }

    /**
     * 上传视频表单验证
     * @param event
     * @returns {boolean}
     */
    function validFrom() {

      var validUpload = {
        rules        : {
          //title    : {
          //	required: true
          //},
          //desc     : {
          //	required: true
          //},
          //picFile  : {
          //	required: true
          //},
          //mediaFile: {
          //	required: true
          //}
        },
        message      : {},
        submitHandler: function (form) {

          var fd = new FormData();
          var mediaFile = $('#mediafile').get(0).files[0];
          var picFile = $('#picfile').get(0).files[0];
          var title = $('#title').val();
          var desc = $('#desc').val();

          fd.append('mediaFile', mediaFile);
          fd.append('picFile', picFile);

          // 此处通过 nginx 进行转发, 转发到:8081
          $.ajax({
            url        : "/api/oss/putMultiAndPic?title=" + title + "&desc=" + desc + "&isTranscode=" + "true",
            type       : "POST",
            cache      : false,
            data       : fd,
            processData: false,
            contentType: false,
            beforeSend : function(){
              ajaxLoading(true);
            }
          }).done(function (data) {
            var code = data.code;
            if (code === 0) {
              utils.infoTip('success-tip', '上传成功,等待管理员审核! (4s后刷新当前页)');
              setTimeout(function () {
                window.location.href = '/upload';
              }, 4000)
            } else {
              utils.infoTip('error-tip', '上传失败, 请更换资源重试!');
            }
          }).fail(function (error) {
            var info = error.statusText;
            utils.infoTip('error-tip', '上传失败, 原因:' + info);
          }).always(function(){
            ajaxLoading(false);
          });

        }
      };

      $('.form-upload').validate(validUpload);

      return false;
    }

    /**
     * ajaxLoading
     * @param {Object} isUploading - true表示'上传中'
     */
    function ajaxLoading (isUploading) {
      var $btn = $('#btn_upload_file');

      if (isUploading) {
        $btn.attr({disabled : 'disabled'});
        $btn.text('上传中...');
      } else {
        $btn.removeAttr('disabled');
        $btn.text('提 交');
      }
    }

  })
})