/**
 * Created by dx.yang on 15/11/17.
 */

angular.module('admin')
    .controller('admin.AdminSiderNavCtrl', [
        '$scope',
        '$location',
        '$state',
        function(
            $scope,
            $location,
            $state
        ) {
            $scope.init = function() {
                //console.log($scope.list);
            };
            function changeTab() {
                $scope.currentTab = $state.current.data.sideNav;
            }
            $scope.$on('$stateChangeSuccess', function () {
                changeTab();
            });
            changeTab();
        }
    ]);