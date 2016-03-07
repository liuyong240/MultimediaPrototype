/**
 * Created by dx.yang on 15/11/26.
 */

angular.module('admin')
    .controller('MtsVideoListCtrl', [
        '$scope',
        '$ajax',
        '$filter',
        '$timeout',
        '$state',
        function(
            $scope,
            $ajax,
            $filter,
            $timeout,
            $state
        ) {

            function load(page, size) {
                page = page || 1;
                size = size || 10;
                $ajax.get('/admin/api/video/list', {
                    pageNumber: page,
                    pageSize: size
                }).fail(function() {
                }).done(function(data) {
                    //$scope.options.pagination.current = page;
                    //$scope.options.pagination.itemsPerPage = size;
                    //$scope.options.pagination.totalItems = data.count;
                    $scope.rows = data.list;
                    $timeout(function() {
                        $scope.options.pagination.totalItems = data.count;
                    });
                });
            }

            function urlParse(v) {
                v = new URL(v);
                var host = v.host.split('.');
                var bucket = host[0];
                var location = host[1];
                var tem = [
                    location,
                    bucket,
                    v.pathname
                ];
                return tem.join('<br>');
            }

            function dateParse(v) {
                return $filter('date')(v, 'yyyy-MM-dd HH:mm:ss');
            }

            $scope.init = function() {

                $scope.options = {
                    pagination: {
                        itemsPerPage: 10,
                        current: 1,
                        action: function(v) {
                            load(v, 10);
                        }
                    },
                    search: false,
                    checkbox: false,
                    topButtons: [{
                        className: 'btn btn-primary btn-sm',
                        icon: 'fa fa-plus',
                        title: '上传视频',
                        action: function(row) {
                            $state.go('mts.videoForm', {
                                id: -1
                            }, {
                                inherit: true
                            });
                        }
                    }],
                    buttons: [{
                        className: 'text-primary',
                        icon: 'fa fa-edit',
                        title: '编辑',
                        action: function(row) {
                            $state.go('mts.videoForm', {
                                id: row.id
                            }, {
                                inherit: true
                            });
                        }
                    }]
                };
                $scope.fields = [{
                    key: 'id',
                    title: 'id'
                }, {
                    key: 'title',
                    title: 'Title'
                }, {
                    key: 'description',
                    title: 'DESC'
                }, {
                    key: 'isDelete',
                    title: '文件状态',
                    valueHandler: function(v) {
                        if (v) {
                            v = '<span class="text-danger">Deleted</span>';
                        } else {
                            v = '<span class="text-success">Normal</span>';
                        }
                        return v;
                    }
                }, {
                    key: 'gmt_created',
                    title: '创建时间',
                    valueHandler: function(v) {
                        return dateParse(v);
                    }
                }, {
                    key: 'father',
                    title: '级别',
                    valueHandler: function(v) {
                        //console.log(111);
                        if (!v || v == 0) {
                            return '原始文件';
                        } else {
                            return '转码文件, 源文件id:' + v;
                        }
                    }
                }, {
                    key: 'status',
                    title: '审核状态',
                    valueHandler: function(v) {
                        v = v * 1;
                        switch (v) {
                            case 0:
                                v = '<span class="text-warning">待审核</span>';
                                break;
                            case 1:
                                v = '<span class="text-success">通过</span>';
                                break;
                            case 2:
                                v = '<span class="text-danger">不通过</span>';
                                break;
                            case 3:
                                v = '<span class="text-info">转码中</span>';
                                break;
                            case 4:
                                v = '<span class="text-info">转码失败</span>';
                                break;
                            case 5:
                                v = '<span class="text-info">执行转码</span>';
                                break;
                            default:
                                break;
                        }
                        return v;
                    }
                }];
                $scope.rows = [];
                load();

            };
        }
    ]);