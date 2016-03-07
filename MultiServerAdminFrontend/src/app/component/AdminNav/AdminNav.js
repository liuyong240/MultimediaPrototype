/**
 * Created by dx.yang on 16/1/7.
 */

angular.module('admin')
    .controller('admin.AdminNavCtrl', [
        '$scope',
        '$location',
        'kai.mainNavList',
        'kai.mainNavTitle',
        'kai.mainNavUser',
        //'$cookies',
        function (
            $scope,
            $location,
            mainNavList,
            mainNavTitle,
            mainNavUser
            //$cookies
        ) {
            $scope.list = mainNavList;
            $scope.title = mainNavTitle;
            $scope.username = mainNavUser;//$cookies.get('kaiUsername');

            function changeTab() {
                var p = $location.path();
                if (!p) {
                    return;
                }
                p = p.match(/^\/([^/]+)/)[1];
                $scope.currentTab = p;
            }

            $scope.$on('$routeChangeStart', function () {
                changeTab();
            });
            $scope.$on('$stateChangeSuccess', function () {
                changeTab();
            });
            changeTab();
        }]);
