/**
 * Created by dx.yang on 15/12/1.
 */

/**
 * Created by dx.yang on 15/4/5.
 */

angular.module('kai')
    .directive('kaiAjaxTable', function() {
        return {
            restrict: 'A',
            scope: {
                options: '=',
                fields: '=',
                rows: '='
            },
            templateUrl: 'app/component/table/ajaxTable.html',
            controller: 'kai.AjaxTableCtrl'
        };
    });

