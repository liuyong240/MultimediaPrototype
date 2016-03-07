/**
 * Created by dx.yang on 15/4/27.
 */

var username_short = '';
var username = '';
document.cookie.split(';').forEach(function(v) {
    if(v.indexOf('jpaUser') !== -1) {
        username = v.split('=')[1];
        username = decodeURIComponent(username);
        username_short = username.split('|')[0];
    }
});

angular.module('admin', [
    'kai',
    'ui.router',
    'ngFileUpload'
])
    .constant('kai.mainNavTitle', '<h1 style="margin:0;padding;0;font-size:20px;font-weight:100;">后台管理<sup>pre</sup></h1>')
    .constant('kai.mainNavUser', username_short)
    .constant('kai.mainNavList', [{
        key: 'mts',
        title: '媒体转码',
        icon: 'fa fa-ship',
        url: '#/mts/template/list'
    }, {
        key: 'user',
        title: '用户管理',
        icon: 'fa fa-group',
        url: '#/user/list'
    //}, {
    //    key: 'resource',
    //    title: '资源管理',
    //    icon: 'fa fa-folder',
    //    url: '#/resource/video/list'
    }])
    .config(function($stateProvider, $urlRouterProvider) {
        $stateProvider
            // [ MTS ] ===============================================
            .state('mts', {
                abstract: true,
                url: '/mts',
                templateUrl: 'app/controller/mts/index.html',
                controller: 'MtsIndexCtrl'
            })
            // [ MTS.模板 ] ===============================================
            .state('mts.list', {
                url: '/template/list',
                data: {
                    sideNav: 'template'
                },
                templateUrl: 'app/controller/mts/template/list.html',
                controller: 'MtsListCtrl'
            })
            .state('mts.form', {
                url: '/template/:method/:id',
                data: {
                    sideNav: 'template'
                },
                templateUrl: 'app/controller/mts/template/form.html',
                controller: 'MtsModalCtrl'
            })
            // [ MTS.水印 ] ===============================================
            .state('mts.wmList', {
                url: '/watermark/list',
                data: {
                    sideNav: 'watermark'
                },
                templateUrl: 'app/controller/mts/watermark/list.html',
                controller: 'MtsWaterMarkListCtrl'
            })
            .state('mts.wmForm', {
                url: '/watermark/:method/:id',
                data: {
                    sideNav: 'watermark'
                },
                templateUrl: 'app/controller/mts/watermark/form.html',
                controller: 'MtsWaterMarkFormCtrl'
            })
            // [ MTS.作业模板 ] ===============================================
            .state('mts.jobTemplateList', {
                url: '/jt/list',
                data: {
                    sideNav: 'jt'
                },
                templateUrl: 'app/controller/mts/jobTemplate/list.html',
                controller: 'MtsJobTemplateListCtrl'
            })
            .state('mts.jobTemplateForm', {
                url: '/jt/:method/:id',
                data: {
                    sideNav: 'jt'
                },
                templateUrl: 'app/controller/mts/jobTemplate/form.html',
                controller: 'MtsJobTemplateFormCtrl'
            })
            // [ MTS.转码记录 ] ===============================================
            .state('mts.jobHistory', {
                url: '/jh/list',
                data: {
                    sideNav: 'jh'
                },
                templateUrl: 'app/controller/mts/jobHistory/list.html',
                controller: 'MtsJobHistoryListCtrl'
            })
            // [ MTS.视频 ] ===============================================
            .state('mts.videoList', {
                url: '/video/list',
                data: {
                    sideNav: 'video'
                },
                templateUrl: 'app/controller/mts/video/list.html',
                controller: 'MtsVideoListCtrl'
            })
            .state('mts.videoForm', {
                url: '/video/form/:id',
                data: {
                    sideNav: 'video'
                },
                templateUrl: 'app/controller/mts/video/form.html',
                controller: 'MtsVideoFormCtrl'
            })
            // [ MTS.用户 ] ===============================================
            .state('user', {
                abstract: true,
                url: '/user',
                templateUrl: 'app/controller/user/index.html',
                controller: 'UserIndexCtrl'
            })
            .state('user.list', {
                url: '/list',
                data: {
                    sideNav: 'list'
                },
                templateUrl: 'app/controller/user/list/list.html',
                controller: 'UserListCtrl'
            })
            .state('user.form', {
                url: '/form/:method/:id',
                data: {
                    sideNav: 'list'
                },
                templateUrl: 'app/controller/user/form/form.html',
                controller: 'UserFormCtrl'
            });
            //.state('resource', {
            //    abstract: true,
            //    url: '/resource',
            //    templateUrl: 'app/controller/resource/index.html',
            //    controller: 'ResourceIndexCtrl'
            //})
            //.state('resource.videoList', {
            //    url: '/video/list',
            //    data: {
            //        sideNav: 'video'
            //    },
            //    templateUrl: 'app/controller/resource/video/list.html',
            //    controller: 'ResourceVideoListCtrl'
            //});


        $urlRouterProvider.otherwise("/mts/template/list");
    });
