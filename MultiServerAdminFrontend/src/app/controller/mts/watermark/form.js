/**
 * Created by dx.yang on 15/11/19.
 */

angular.module('admin').controller('MtsWaterMarkFormCtrl', [
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
        var tid = $stateParams.id;
        $scope.loaded = false;

        $scope.method = method;
        $scope.baseInfo = {
            add: {
                title: '新建水印模板'
            },
            edit: {
                title: '编辑水印模板'
            }
        }[method];

        $scope.formBasic = {
            referPos: [ 'TopRight', 'TopLeft', 'BottomRight', 'BottomLeft' ]
        };

        var defaultTemplate = {
            config: {
                "width":"10",
                "height":"30",
                "dx":"10",
                "dy":"5",
                "referPos":"TopLeft",
                "type":"Image"
            }
        };


        if (method === 'edit' && tid) {
            $ajax.get('/admin/api/mts/watermark/' + tid).fail(function() {
            }).done(function(data) {
                if (!data || !data.length) {
                    dialogService.alert({
                        icon: 'fa fa-error',
                        content: '模板不存在!'
                    });
                    return;
                }
                data = {
                    config: data[0]
                };
                data.id = data.config.id;
                data.name = data.config.name;
                delete data.config.id;
                delete data.config.name;
                delete data.config.state;

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

                // key首字母大写
                t = capitalizeKeys(t);

                // 转为 Map<String, String>
                _.each(t, function(v, k) {
                    if (_.isObject(v)) {
                        t[k] = JSON.stringify(v);
                    }
                });

                $ajax.post('/admin/api/mts/watermark', t).fail(function(err) {
                    dialogService.alert({
                        icon: 'error',
                        content: err
                    });
                }).done(function() {
                    dialogService.alert({
                        icon: 'success',
                        content: '保存成功'
                    }).done(function() {
                        $state.go('mts.wmList');
                    });
                });
            });

        };
    }
]);
