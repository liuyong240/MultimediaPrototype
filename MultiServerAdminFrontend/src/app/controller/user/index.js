/**
 * Created by dx.yang on 15/11/26.
 */


angular.module('admin')
    .controller('UserIndexCtrl', [
        '$scope',
        function(
            $scope
        ) {
            $scope.siderNavList = [{
                key: 'list',
                title: '用户列表',
                //icon: 'fa fa-table',
                url: '#/user/list'
            }];

        }
    ]);