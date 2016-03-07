/**
 * Created by dx.yang on 15/11/13.
 */

angular.module('admin')
    .controller('MtsIndexCtrl', [
        '$scope',
        function(
            $scope
        ) {
            $scope.siderNavList = [{
                key: 'template',
                title: '转码模板',
                //icon: 'fa fa-table',
                url: '#/mts/template/list'
            }, {
                key: 'watermark',
                title: '水印模板',
                //icon: 'fa fa-table',
                url: '#/mts/watermark/list'
            }, {
                key: 'jt',
                title: '转码作业模板',
                //icon: 'fa fa-table',
                url: '#/mts/jt/list'
            }, {
                key: 'video',
                title: '视频文件管理',
                url: '#/mts/video/list'
            }, {
                key: 'jh',
                title: '转码作业记录',
                url: '#/mts/jh/list'
            }];

        }
    ]);
