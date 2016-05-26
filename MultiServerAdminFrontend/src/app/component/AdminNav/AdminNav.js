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
        '$cookies',
        function (
            $scope,
            $location,
            mainNavList,
            mainNavTitle,
            mainNavUser,
            $cookies
        ) {
            $scope.list = mainNavList;
            $scope.title = mainNavTitle;
            $scope.username = $cookies.get('username') || 'defaultUser';

            // 显示登录用户信息
            $scope.isShow = false;
            $scope.showMenu = showMenu;

            $scope.$on('$routeChangeStart', function () {
                changeTab();
            });
            $scope.$on('$stateChangeSuccess', function () {
                changeTab();
            });
            changeTab();

            function changeTab() {
                var p = $location.path();
                if (!p) {
                    return;
                }
                p = p.match(/^\/([^/]+)/)[1];
                $scope.currentTab = p;
            }

            function showMenu (){
                $scope.isShow = $scope.isShow ? false : true;
            };
        }]);
