/**
 * Created by dx.yang on 15/11/26.
 */


angular.module('admin')
    .controller('UserListCtrl', [
        '$scope',
        '$ajax',
        '$state',
        function(
            $scope,
            $ajax,
            $state
        ) {
            $scope.fields = [{
                key: 'id',
                title: 'ID'
            }, {
                key: 'username',
                title: '用户名'
            }, {
                key: 'authorities',
                title: '权限'
            }, {
                key: 'enabled',
                title: '可用'
            }];
            $scope.options = {
                search: false,
                checkbox: false,
                pagination: {
                    itemsPerPage: 10,
                    current: 1
                },
                //topButtons: [{
                //    className: 'btn btn-primary btn-sm',
                //    icon: 'fa fa-plus',
                //    title: 'Add',
                //    action: function() {
                //    }
                //}],
                buttons: [{
                    className: 'text-primary',
                    icon: 'fa fa-edit',
                    title: 'Edit',
                    action: function(row) {
                        $state.go('user.form', {
                            method: 'edit',
                            id: row.id
                        }, {
                            inherit: true
                        });
                    }
                }
                //    , {
                //    className: 'text-danger',
                //    icon: 'fa fa-trash',
                //    title: 'Del',
                //    action: function(row) {
                //    }
                //}
                ]
            };
            $scope.rows = [];
            $ajax.get('/admin/api/user').done(function(d) {
                $scope.rows = d;
            });
        }
    ]);
