/**
 * Created by dx.yang on 15/11/20.
 */


angular.module('admin').controller('MtsJobTemplateFormCtrl', [
    '$scope',
    '$stateParams',
    '$ajax',
    'dialogService',
    '$state',
    'pipelineService',
    function(
        $scope,
        $stateParams,
        $ajax,
        dialogService,
        $state,
        pipelineService
    ) {
        var method = $stateParams.method;
        var tid = $stateParams.id;
        $scope.loaded = false;


        pipelineService.getPipelines().done(function(data) {
            $scope.pipelines = data;
        });

        $scope.method = method;
        $scope.baseInfo = {
            add: {
                title: '新建转码作业模板'
            },
            edit: {
                title: '编辑转码作业模板'
            }
        }[method];

        $scope.formBasic = {
            referPos: [ 'TopRight', 'TopLeft', 'BottomRight', 'BottomLeft' ]
        };
        function initJSONEditor() {
            // create the editor
            var container = document.getElementById("jsoneditor");

            // set json
            //var json = {
            //    "Array": [1, 2, 3],
            //    "Boolean": true,
            //    "Null": null,
            //    "Number": 123,
            //    "Object": {"a": "b", "c": "d"},
            //    "String": "Hello World"
            //};
            var json = JSON.parse($scope.template.outputs);
            $scope.editor = new JSONEditor(container, {
                mode: 'code'
            }, json);
            //eo.set(json);

            // get json
            //var json = editor.get();
        }

        $scope.initJSONEditor = initJSONEditor;

        var defaultTemplate = {
            name: null,
            desc: null,
            outputs: null,
            pipelineId: null,
            outputBucket: null,
            outputLocation: 'oss-cn-hangzhou',
            adminId: null,
            lastUpdate: null,
            using: false
        };


        if (method === 'edit' && tid) {
            $ajax.get('/admin/api/mts/jt', {
                id: tid
            }).fail(function() {
            }).done(function(data) {
                if (!data || !data.length) {
                    dialogService.alert({
                        icon: 'fa fa-error',
                        content: '模板不存在!'
                    });
                    return;
                }
                $scope.template = _.merge(defaultTemplate, data[0]);
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

                t.lastUpdate = ~~(new Date() / 1000);

                var outputs = $scope.editor.get();
                outputs = JSON.stringify(outputs);
                t.outputs = outputs;

                //t.outputs = JSON.stringify(t.outputs);

                //// key首字母大写
                //t = capitalizeKeys(t);
                //
                //// 转为 Map<String, String>
                //_.each(t, function(v, k) {
                //    if (_.isObject(v)) {
                //        t[k] = JSON.stringify(v);
                //    }
                //});

                $ajax.post('/admin/api/mts/jt', t).fail(function(err) {
                    dialogService.alert({
                        icon: 'error',
                        content: err
                    });
                }).done(function() {
                    dialogService.alert({
                        icon: 'success',
                        content: '保存成功'
                    }).done(function() {
                        $state.go('mts.jobTemplateList');
                    });
                });

            });

        };

     }
]);
