/**
 * Created by dx.yang on 15/11/17.
 */


angular.module('admin')
    .directive('adminSiderNav', function() {
        return {
            restrict: 'A',
            scope: {
                list: '=list'
            },
            templateUrl: 'app/component/AdminSiderNav/AdminSiderNav.html',
            controller: 'admin.AdminSiderNavCtrl'
        };
    });