/**
 * Created by dx.yang on 15/11/20.
 */

angular.module('admin')
    .controller('MtsJobTemplateListCtrl', [
        '$scope',
        '$ajax',
        '$state',
        'dialogService',
        '$filter',
        function(
            $scope,
            $ajax,
            $state,
            dialogService,
            $filter
        ) {

            function load(page, size) {
                //page = page || 1;
                //size = size || 10;
                $ajax.get('/admin/api/mts/jt', {
                    //pageNumber: page,
                    //pageSize: size
                }).fail(function() {
                }).done(function(data) {
                    //$scope.options.pagination.current = page;
                    //$scope.options.pagination.itemsPerPage = size;
                    //$scope.options.pagination.totalItems = data.count;
                    $scope.rows = data;
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
                            $state.go('mts.jobTemplateForm', {
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
                            $state.go('mts.jobTemplateForm', {
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
                                $ajax.delete('/admin/api/mts/jt/' + row.id).fail(function(err) {
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
                    sort: 'number'
                }, {
                    key: 'name',
                    title: '名称',
                    sort: 'string'
                }, {
                    key: 'desc',
                    title: '描述'
                }, {
                    key: 'pipelineId',
                    title: '管道ID'
                }, {
                    key: 'using',
                    title: '使用中',
                    sort: 'string',
                    valueHandler: function(v) {
                        if (v) {
                            return '<span class="text-success">是</span>';
                        }
                        return '<span class="text-muted">否</span>';
                    }
                }, {
                    key: 'adminId',
                    title: '操作人',
                    valueHandler: function(v) {
                        return v;
                    }
                }, {
                    key: 'lastUpdate',
                    title: '最后更新时间',
                    valueHandler: function(v) {
                        v = $filter('date')(v * 1000, 'yyyy-MM-dd HH:mm:ss');
                        return v;
                    }
                }];
                $scope.rows = [];
                load();

            };
        }
    ]);
