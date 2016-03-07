/**
 * Created by dx.yang on 15/11/26.
 */

angular.module('admin')
    .controller('kai.Player', [
        '$scope',
        'Upload',
        function(
            $scope,
            Upload
        ) {

            //console.log($scope.source);

            $scope.$watch('source', function(v) {
                //console.log(arguments);
                if (v !== null) {
                    //console.log(location.host);
                    v = '//' + location.host + '/' + v;
                    $scope.player = new prismplayer({
                        id: "J_prismPlayer", // 容器id
                        source: v,
                        //source: "http://multimedia-input.oss-cn-hangzhou.aliyuncs.com/input/14495453112529.mp4?Expires=1449732527&OSSAccessKeyId=HYmwRNeXFrEMLXYp&Signature=SElVkOs3BDJ73w05B9n7OulI4nI%3D",         // 视频url 支持互联网可直接访问的视频地址
                        autoplay: false,      // 自动播放
                        width: "400px",       // 播放器宽度
                        height: "300px"      // 播放器高度
                    });

                }
            });

            $scope.init = function() {

                //$scope.submit = function() {
                //
                //    console.log($scope.file);
                //    if ($scope.file) {
                //        $scope.upload($scope.file);
                //    }
                //};
                //
                //// upload on file select or drop
                //$scope.upload = function (file) {
                //    Upload.upload({
                //        url: '/api/oss/putObject',
                //        data: {multiFile: file}
                //    }).then(function (resp) {
                //        console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
                //    }, function (resp) {
                //        console.log('Error status: ' + resp.status);
                //    }, function (evt) {
                //        var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                //        console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
                //    });
                //};




                //new MediaElement('player1', {
                //    // shows debug errors on screen
                //    enablePluginDebug: false,
                //    // remove or reorder to change plugin priority
                //    plugins: ['flash','silverlight'],
                //    // specify to force MediaElement to use a particular video or audio type
                //    //type:
                //    type: ['video/mp4', 'video/webm', 'video/flv'],
                //    // path to Flash and Silverlight plugins
                //    pluginPath: '/modules/MediaElement/',
                //    // name of flash file
                //    flashName: 'flashmediaelement.swf',
                //    // name of silverlight file
                //    silverlightName: 'silverlightmediaelement.xap',
                //    // default if the <video width> is not specified
                //    defaultVideoWidth: 480,
                //    // default if the <video height> is not specified
                //    defaultVideoHeight: 270,
                //    // overrides <video width>
                //    pluginWidth: -1,
                //    // overrides <video height>
                //    pluginHeight: -1,
                //
                //    features: ['playpause','progress','current','duration','tracks','volume','fullscreen'],
                //    // rate in milliseconds for Flash and Silverlight to fire the timeupdate event
                //    // larger number is less accurate, but less strain on plugin->JavaScript bridge
                //    timerRate: 250,
                //    // method that fires when the Flash or Silverlight object is ready
                //    success: function (mediaElement, domObject) {
                //
                //        // add event listener
                //        mediaElement.addEventListener('timeupdate', function(e) {
                //
                //            document.getElementById('current-time').innerHTML = mediaElement.currentTime;
                //
                //        }, false);
                //
                //        // call the play method
                //        mediaElement.play();
                //
                //    },
                //    // fires when a problem is detected
                //    error: function () {
                //
                //    }
                //});
            };
        }
    ]);
