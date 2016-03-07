/**
 * Created by dx.yang on 15/12/1.
 */

angular.module('admin')
    .controller('MtsVideoFormCtrl', [
        '$scope',
        '$ajax',
        '$stateParams',
        '$http',
        '$state',
        'dialogService',
        function ($scope,
                  $ajax,
                  $stateParams,
                  $http,
                  $state,
                  dialogService
        ) {

            var vid = $stateParams.id * 1;

            $scope.isNew = vid === -1;

            $scope.statusList = [{
                value: 0,
                title: '待审核'
            }, {
                value: 1,
                title: '通过'
            }, {
                value: 2,
                title: '不通过'
            }, {
                value: 3,
                title: '转码中'
            }, {
                value: 4,
                title: '转码失败'
            }, {
                value: 5,
                title: '执行转码'
            }];

            $scope.save2oss = function(e) {
                e.preventDefault();
                $ajax.get('/api/sts/assumeWriteRole').done(function(data) {
                    var ossUpload = new OssUpload({
                        bucket: 'multimedia-input',
                        // 根据你的 oss 实例所在地区选择填入
                        // 杭州：http://oss-cn-hangzhou.aliyuncs.com
                        // 北京：http://oss-cn-beijing.aliyuncs.com
                        // 青岛：http://oss-cn-qingdao.aliyuncs.com
                        // 深圳：http://oss-cn-shenzhen.aliyuncs.com
                        // 香港：http://oss-cn-hongkong.aliyuncs.com
                        endpoint: 'http://oss-cn-hangzhou.aliyuncs.com',
                        // 如果文件大于 chunkSize 则分块上传, chunkSize 不能小于 100KB 即 102400
                        chunkSize: 1048576,
                        // 分块上传的并发数
                        concurrency: 2,
                        stsToken: data
                    });
                    ossUpload.upload({
                        // 必传参数, 需要上传的文件对象
                        file: files[i],
                        // 必传参数, 文件上传到 oss 后的名称, 包含路径
                        key: 'user/01/file_key',
                        // 上传失败后重试次数
                        maxRetry: 3,
                        // OSS支持4个 HTTP RFC2616(https://www.ietf.org/rfc/rfc2616.txt)协议规定的Header 字段：
                        // Cache-Control、Expires、Content-Encoding、Content-Disposition。
                        // 如果上传Object时设置了这些Header，则这个Object被下载时，相应的Header值会被自动设置成上传时的值
                        // 可选参数
                        headers: {
                            'CacheControl': 'public',
                            'Expires': '',
                            'ContentEncoding': '',
                            'ContentDisposition': '',
                            // oss 支持的 header, 目前仅支持 x-oss-server-side-encryption
                            'ServerSideEncryption': ''
                        },
                        // 文件上传中调用, 可选参数
                        onprogress: function (evt) {
                            console.log(evt);
                        },
                        // 文件上传失败后调用, 可选参数
                        onerror: function (evt) {
                            console.log(evt);
                        },
                        // 文件上传成功调用, 可选参数
                        oncomplete: function (res) {
                            console.log(res);
                        }
                    });
                });
            };

            $scope.save = function (e) {

                e.preventDefault();

                dialogService.prompt({
                    icon: 'warning',
                    content: '确定保存修改?'
                }).done(function() {

                    var queryString = [];
                    var formData = new FormData();

                    var mediaFile = $('.mediaInput').get(0).files[0];
                    var picFile = $('.picInput').get(0).files[0];

                    formData.append('mediaFile', mediaFile);
                    formData.append('picFile', picFile);
                    queryString.push('desc=' + $scope.data.baseInfo.description);
                    queryString.push('title=' + $scope.data.baseInfo.title);
                    if ($scope.data.isTranscode) {
                        queryString.push('isTranscode=' + $scope.data.isTranscode);
                    }

                    var url = '';

                    if (vid !== -1) {
                        queryString.push('id=' + vid);
                        queryString.push('status=' + $scope.data.baseInfo.status);
                        url = '/admin/api/video/updateMediaAndPicStatus?';
                    } else {
                        url = '/admin/api/video/updateMediaAndPicStatus?';
                        //url = '/api/oss/putMultiAndPic?';
                    }

                    queryString = queryString.join('&');
                    url += queryString;

                    $http.post(url, formData, {
                        transformRequest: angular.identity,
                        headers: {'Content-Type': undefined}
                    }).error(function () {
                    }).success(function () {
                        dialogService.alert({
                            icon: 'success',
                            content: '保存成功'
                        }).done(function() {
                            $state.go('mts.videoList');
                        });
                    });
                });


            };

            function defaultValue(data) {
                if (!data.baseInfo) {
                    data.baseInfo = {};
                }
                if (data.baseInfo.status === undefined) {
                    data.baseInfo.status = $scope.statusList[0].value;
                }
                if (data.isTranscode === undefined) {
                    data.isTranscode = 'false';
                }
                return data;
            }

            $scope.init = function () {

                if (vid !== -1) {
                    $scope.baseInfo = {
                        title: '编辑视频'
                    };
                    $ajax.get('/admin/api/video/get/' + vid).done(function (data) {
                        $scope.data = defaultValue(data);
                    });
                } else {
                    $scope.baseInfo = {
                        title: '新上传视频'
                    };
                    $scope.data = defaultValue({});
                }


            };

        }
    ]);
