(function () {
    'use strict';

    angular
        .module('tmsApp')
        .controller('CourseInfoController', CourseInfoController);

    /** @ngInject */
    function CourseInfoController($scope, $stateParams, courseAPI) {
        var vm = this;

        vm.courseInfo = {};
        vm.courseInfo.files = [];
        vm.courseInfo.commentList = [];
        vm.courseInfo.participantsList = [];
        vm.courseInfo.timetable = [];

        vm.getShortInfo = getShortInfo;
        vm.subscribe = subscribe;

        $scope.courseInfo = vm.courseInfo;

        vm.getShortInfo();

        function getShortInfo() {
            courseAPI.getShortInfo($stateParams.courseId).then(function(data) {
                $scope.courseInfo = angular.copy(data);
                console.log($scope.courseInfo);
            });
        }

        function subscribe() {
            courseAPI.subscribe($stateParams.courseId).then(function(data) {
                console.log('Subscribed successfully');
            })
        }

    }
})();