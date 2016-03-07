/**
 * Created by dx.yang on 15/11/17.
 */

angular.module('admin')
    .controller('MtsListCtrl', [
        '$scope',
        '$ajax',
        '$state',
        'dialogService',
        function(
            $scope,
            $ajax,
            $state,
            dialogService
        ) {

            function load(page, size) {
                page = page || 1;
                size = size || 10;
                $ajax.get('/admin/api/mts/templates', {
                    pageNumber: page,
                    pageSize: size
                }).fail(function() {
                }).done(function(data) {
                    $scope.options.pagination.current = page;
                    $scope.options.pagination.itemsPerPage = size;
                    $scope.options.pagination.totalItems = data.count;
                    $scope.rows = data.list;
                });
            }

            $scope.init = function() {

                $scope.options = {
                    search: false,
                    checkbox: false,
                    pagination: {
                        itemsPerPage: 10,
                        current: 1
                    },
                    topButtons: [{
                        className: 'btn btn-primary btn-sm',
                        icon: 'fa fa-plus',
                        title: 'Add',
                        action: function() {
                            $state.go('mts.form', {
                                method: 'add',
                                id: new Date() * 1
                            }, {
                                inherit: true
                            });
                        }
                    }],
                    buttons: [{
                        className: 'text-primary',
                        icon: 'fa fa-edit',
                        title: 'Edit',
                        action: function(row) {
                            $state.go('mts.form', {
                                method: 'edit',
                                id: row.id
                            }, {
                                inherit: true
                            });
                        }
                    }, {
                        className: 'text-danger',
                        icon: 'fa fa-trash',
                        title: 'Del',
                        action: function(row) {
                            dialogService.prompt({
                                icon: 'warning',
                                content: '确定删除 ' + row.name + ' ?'
                            }).done(function() {
                                $ajax.delete('/admin/api/mts/template/' + row.id).fail(function(err) {
                                    dialogService.alert({
                                        icon: 'error',
                                        content: err
                                    });
                                }).done(function() {
                                    dialogService.alert({
                                        icon: 'success',
                                        content: '删除成功!'
                                    }).done(function() {
                                        //$state.reload();
                                        load();
                                    });
                                });
                            });
                        }
                    }]
                };
                $scope.fields = [{
                    key: 'id',
                    title: 'ID',
                    sort: 'string'
                }, {
                    key: 'name',
                    title: '名称',
                    sort: 'string'
                }, {
                    key: 'state',
                    title: '状态',
                    sort: 'string',
                    valueHandler: function(v) {
                        switch (v) {
                            case 'Normal':
                                v = '<span class="text-success">' + v + '</span>';
                                break;
                            case 'Deleted':
                                v = '<span class="text-danger">' + v + '</span>';
                                break;
                            default:break;
                        }
                        return v;
                    }
                }, {
                    key: 'container',
                    title: '格式',
                    valueHandler: function(v) {
                        return v.format;
                    }
                }];
                $scope.rows = [];
                load();

            };
        }
    ]);
