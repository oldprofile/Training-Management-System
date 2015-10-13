(function () {
    'use strict';

    angular
        .module('tmsApp')
        .config(routeConfig);

    /** @ngInject */
    function routeConfig($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('admin', {
                url: '/admin',
                templateUrl: 'app/page.admin/admin.html',
                controller: 'AdminController',
                controllerAs: 'admin'
            }).state('browse', {
                url: '/browse',
                templateUrl: 'app/page.browse/browse.html',
                controller: 'BrowseController',
                controllerAs: 'browse'
            }).state('courseinfo', {
                url: '/courseinfo',
                templateUrl: 'app/page.courseinfo/courseinfo.html',
                controller: 'CourseInfoController',
                controllerAs: 'courseinfo'
            }).state('managecourse', {
                abstract: true,
                url: '/managecourse',
                templateUrl: 'app/page.managecourse/managecourse.html',
                controller: 'ManageCourseController',
                controllerAs: 'managecourse',
                params: {
                    courseId: null,
                    type: null
                }
            }).state('mycourses', {
                url: '/',
                templateUrl: 'app/page.mycourses/mycourses.html',
                controller: 'MyCoursesController',
                controllerAs: 'mycourses'
            })
        ;

        $urlRouterProvider.when('/createcourse', '/createcourse/step1');
        $urlRouterProvider.otherwise('/');
    }

})();
