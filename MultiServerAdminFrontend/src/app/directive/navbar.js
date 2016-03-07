/**
 * Created by dx.yang on 16/1/7.
 */


angular.module('admin')
    .directive('adminNavbar', function() {
        return {
            restrict: 'A',
            scope: {
                list: '=list'
            },
            templateUrl: 'app/component/AdminNav/AdminNav.html',
            controller: 'admin.AdminNavCtrl'
        };
    });
