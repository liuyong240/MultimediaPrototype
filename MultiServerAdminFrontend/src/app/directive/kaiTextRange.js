/**
 * Created by dx.yang on 15/11/18.
 */

angular.module('admin').directive('textRange', function() {
    return {
        require: 'ngModel',
        scope: {
            'textMax': '=',
            'textMin': '='
        },
        link: function(scope, elem, attrs, ctrl) {
            var max = attrs.textMax * 1;
            var min = attrs.textMin * 1;
            ctrl.$parsers.unshift(function(val) {
                if (max) {
                    if (val * 1 > max) {
                        ctrl.$setValidity('max', false);
                    } else {
                        ctrl.$setValidity('max', true);
                    }
                }
                if (min) {
                    if (val * 1 < min) {
                        ctrl.$setValidity('min', false);
                    } else {
                        ctrl.$setValidity('min', true);
                    }
                }
                return val;
            });
        }
    };
});