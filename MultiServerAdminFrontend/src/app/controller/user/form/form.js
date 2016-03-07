/**
 * Created by dx.yang on 15/11/26.
 */

angular.module('admin').controller('UserFormCtrl', [
    '$scope',
    '$stateParams',
    '$ajax',
    'dialogService',
    '$state',
    function(
        $scope,
        $stateParams,
        $ajax,
        dialogService,
        $state
    ) {
        var method = $stateParams.method;
        var uid = $stateParams.id;
        $scope.loaded = false;

        $scope.method = method;
        $scope.baseInfo = {
            add: {
                title: '新建用户'
            },
            edit: {
                title: '编辑用户'
            }
        }[method];


        var defaultTemplate = {
            auth: {}
        };


        if (method === 'edit' && uid) {
            $ajax.get('/admin/api/user/' + uid).fail(function() {
            }).done(function(data) {

                var auth = data.authorities.split(',');
                data.auth = {};
                if (auth.indexOf('ROLE_USER') != -1) {
                    data.auth.ROLE_USER = true;
                }
                if (auth.indexOf('ROLE_ADMIN') != -1) {
                    data.auth.ROLE_ADMIN = true;
                }


                $scope.template = _.merge(defaultTemplate, data);


                //console.log(t);
                $scope.loaded = true;
            });
        } else {
            $scope.template = defaultTemplate;
            $scope.loaded = true;
        }

        function capitalizeKeys(obj) {
            var tem = {};
            _.each(obj, function(v, k) {
                k = _.capitalize(k);
                if (_.isPlainObject(v)) {
                    tem[k] = capitalizeKeys(v);
                } else {
                    tem[k] = v;
                }
            });
            return tem;
        }

        $scope.submitHandler = function() {

            dialogService.prompt({
                icon: 'info',
                content: '确定保存?'
            }).done(function() {
                var t = _.cloneDeep($scope.template);

                var auth = [];
                _.each(t.auth, function(v, k) {
                    if (v) {
                        auth.push(k);
                    }
                });
                t.authorities = auth.join(',');

                delete t.auth;

                $ajax.post('/admin/api/user/update', {
                    id: t.id,
                    authorities: t.authorities,
                    enabled: t.enabled
                }).fail(function(err) {
                    dialogService.alert({
                        icon: 'error',
                        content: err
                    });
                }).done(function() {
                    dialogService.alert({
                        icon: 'success',
                        content: '保存成功'
                    }).done(function() {
                        $state.go('user.list');
                    });
                });
            });

        };
    }
]);
