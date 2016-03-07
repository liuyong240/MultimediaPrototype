/**
 * Created by dx.yang on 15/11/26.
 */

angular.module('admin')
    .service('pipelineService', [
        '$ajax',
        function(
            $ajax
        ) {

            var self = this;
            self.getPipelines = function() {
                return $ajax.get('/api/mts/pipeline/user/searchpipelinebystate');
            };
        }
    ]);