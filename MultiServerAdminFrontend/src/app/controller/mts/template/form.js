/**
 * Created by dx.yang on 15/11/17.
 */

angular.module('admin').controller('MtsModalCtrl', [
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

        $scope.baseInfo = {
            add: {
                title: '新建转码模板'
            },
            edit: {
                title: '编辑转码模板'
            }
        }[method];

        $scope.formBasic = {
            fileType: [ 'flv', 'mp4', 'ts', 'm3u8', 'mp3', 'ogg', 'flac' ],
            videoCodec: function(type) {
                if (type === 'flv') {
                    return ['H.264'];
                }
                return [ 'H.264', 'H.265' ];
            },
            videoProfile: [ 'baseline', 'main', 'high'],
            videoPreset: [ 'veryfast', 'fast', 'medium', 'slow', 'slower' ],
            videoScanMode: [ 'interlaced', 'progressive' ],
            videoPixFmt: [ 'yuv420p', 'yuvj420p' ],
            audioCodec: [ 'AAC', 'MP3', 'VORBIS', 'FLAC' ],
            audioProfile: [ 'aac_low', 'aac_he', 'aac_he_v2', 'aac_ld', 'aac_eld' ],
            audioSamplerate: function(videoCodec, audioCodec) {
                if (videoCodec === 'flv' && audioCodec === 'MP3') {
                    return [ '22050', '44100' ];
                } else if (audioCodec === 'MP3') {
                    return [ '22050', '32000', '44100', '48000' ];
                }
                return [ '22050', '32000', '44100', '48000', '96000' ];
            },
            audioChannels: function(codec) {
                if (codec === 'AAC') {
                    return [1, 2, 4, 5, 6, 8];
                }
                return [1, 2];
            }
        };

        var defaultTemplate = {
            container: {
                format: 'mp4'
            },
            video: {
                codec: 'H.264',
                profile: 'high',
                bitrate: '500',
                crf: '15',
                width: null,//'256',
                height: null, //'800',
                fps: '25',
                gop: '10',
                preset: 'medium',
                bufsize: '6000',
                bitrateBnd: {
                    max: null,
                    min: null
                },
                pixFmt: 'yuv420p'
            },
            audio: {
                codec: 'AAC',
                samplerate: '44100',
                bitrate: '500',
                channels: '2'
            },
            transConfig: {
                transMode: 'fixCRF'
            },
            muxConfig: {
                segment: {
                    duration : '12'
                }
            }
        };


        if (method === 'edit' && tid) {
            $ajax.get('/admin/api/mts/template/' + tid).fail(function() {
            }).done(function(data) {
                if (!data || !data.length) {
                    dialogService.alert({
                        icon: 'fa fa-error',
                        content: '模板不存在!'
                    });
                    return;
                }
                $scope.template = _.merge(defaultTemplate, data[0]);
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

                $ajax.post('/admin/api/mts/template', t).fail(function(err) {
                    dialogService.alert({
                        icon: 'error',
                        content: err
                    });
                }).done(function() {
                    dialogService.alert({
                        icon: 'success',
                        content: '保存成功'
                    }).done(function() {
                        $state.go('mts.list');
                    });
                });
            });
        };
    }
]);
