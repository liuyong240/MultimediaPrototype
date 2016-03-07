/**
 * Created by dx.yang on 15/11/27.
 */


angular.module('admin')
    .controller('MtsJobHistoryListCtrl', [
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
                page = page || 1;
                size = size || 100;
                $ajax.get('/api/mts/jobhistory/list', {
                    pageNumber: page,
                    pageSize: size
                }).fail(function() {
                }).done(function(data) {
                    //$scope.options.pagination.current = page;
                    //$scope.options.pagination.itemsPerPage = size;
                    //$scope.options.pagination.totalItems = data.count;
                    $scope.rows = data;
                });
            }

            function urlParse(v) {
                return '<a href="' + v + '">' + v + '</a>';
                //v = new URL(v);
                //var host = v.host.split('.');
                //var bucket = host[0];
                //var location = host[1];
                //var tem = [
                //    location,
                //    bucket,
                //    v.pathname
                //];
                //return tem.join('<br>');
            }

            function dateParse(v) {
                return $filter('date')(v, 'yyyy-MM-dd HH:mm:ss');
            }

            $scope.init = function() {

                $scope.options = {
                    search: false,
                    checkbox: false,
                    pagination: {
                        itemsPerPage: 10,
                        current: 1
                        //action: function(n) {
                        //    load(n, 3);
                        //}
                    },
                    topButtons: [],
                    buttons: []
                };
                $scope.fields = [{
                    key: 'jobAction',
                    title: 'jobAction'
                }, {
                    key: 'inputUrl',
                    title: 'input',
                    valueHandler: urlParse
                }, {
                    key: 'outputUrl',
                    title: 'outputUrl',
                    valueHandler: urlParse
                }, {
                    key: 'status',
                    title: 'status'
                }, {
                    key: 'gmtCreated',
                    title: 'created',
                    valueHandler: dateParse
                }, {
                    key: 'gmtModified',
                    title: 'modified',
                    valueHandler: dateParse
                }, {
                    key: 'deleted',
                    title: '已删除'
                }];
                $scope.rows = [];
                load();

            };
        }
    ]);
