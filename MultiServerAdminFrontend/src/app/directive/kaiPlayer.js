/**
 * Created by dx.yang on 15/12/1.
 */


angular.module('admin')
    .directive('kaiPlayer', function() {
        return {
            restrict: 'A',
            scope: {
                source: '='
            },
            templateUrl: 'app/component/player/player.html',
            controller: 'kai.Player'
        };
    });