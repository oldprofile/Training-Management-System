(function () {
    'use strict';
        angular
            .module('tmsAPI')
            .factory('urlProvider', UrlProvider);

    /* @ngInject */
    function UrlProvider() {
        var urlProvider = {
            addComment: addComment,
            addFeedback: addFeedback,
            addParticipant: addParticipant,
            approveCourse: approveCourse,
            cancelCreate: cancelCreate,
            cancelEdit: cancelEdit,
            confirm: confirm,
            createCourse: createCourse,
            deleteLesson: deleteLesson,
            deleteParticipant: deleteParticipant,
            editCourse: editCourse,
            findTrainings: findTrainings,
            findUsers: findUsers,
            getApproveList: getApproveList,
            getComments: getComments,
            getCourseList: getCourseList,
            getCurrentCoursesForUser: getCurrentCoursesForUser,
            getEditedCourse: getEditedCourse,
            getFeedbacksOnUser: getFeedbacksOnUser,
            getNewsList: getNewsList,
            getParticipants: getParticipants,
            getPastCoursesForUser: getPastCoursesForUser,
            getProfileInfo: getProfileInfo,
            getShortInfo: getShortInfo,
            getTimetable: getTimetable,
            getWaitingCoursesForUser: getWaitingCoursesForUser,
            login: login,
            logout: logout,
            manageLesson: manageLesson,
            uploadFiles: uploadFiles
        };
        return urlProvider;
    }

    function addComment(courseId) {
        return '/api/training/' + courseId + '/add_comment';
    }

    function addFeedback() {
        return '/api/feedback_controller/add_feedback';
    }

    function addParticipant(courseId) {
        return '/api/training/' + courseId + '/add_listener'; //!!! CHECK
    }

    function approveCourse(actionId) {
        return '/api/training/confirm/' + actionId;
    }

    function cancelCreate(trainingId) {
        return '/api/training/cancel_create/' + trainingId;
    }

    function cancelEdit(trainingId) {
        return '/api/training/cancel_edit/' + trainingId;
    }

    function confirm(trainingId) {
        return '/api/training/confirm/' + trainingId;
    }

    function createCourse() {
        return '/api/training/create';
    }

    function deleteLesson(courseId, lessonId) {
        return '/api/training/' + courseId + '/lesson/' + lessonId;
    }

    function deleteParticipant(courseId, userId) {
        return '/api/training/' + courseId + '/leave/' + userId; //CHECK
    }

    function editCourse(trainingId) {
        return '/api/training/edit/' + trainingId;
    }

    function findTrainings(searchQuery) {
        return '/api/search_controller/search_training';
    }

    function findUsers(searchQuery) {
        return '/api/search_controller/search_user'
    }

    function getApproveList() {
        return '/api/approve_list';
    }

    function getComments(courseId) {
        return '/api/training/' + courseId + '/comment_list';
    }

    function getCourseList() {
        return '/api/training/training_list';
    }

    function getCurrentCoursesForUser(userId) {
        return '/api/user_controller/actualTraining/' + userId;
    }

    function getEditedCourse(trainingId) {
        return '/api/training/getApproveTraining/' + trainingId;
    }

    function getFeedbacksOnUser(userId) {
        return '/api/feedback_controller/feedbacks_of_user/' + userId;
    }

    function getNewsList() {
        return '/api/news';
    }

    function getParticipants(courseId) {
        return '/api/training/' + courseId + '/listener_list';
    }

    function getPastCoursesForUser(userId) {
        return '/api/user_controller/visitedTraining/' + userId;
    }

    function getProfileInfo(userId) {
        return '/api/user_controller/user_info/' + userId;
    }

    function getShortInfo(courseId) {
        return '/api/training/' + courseId;
    }

    function getTimetable(courseId) {
        return '/api/training/' + courseId + '/lesson_list';
    }

    function getWaitingCoursesForUser(userId) {
        //return;
    }

    function login() {
        return '/api/login';
    }

    function logout() {
        return '/api/logout';
    }

    function manageLesson(courseId) {
        return '/api/training/' + courseId + '/lesson';
    }

    function uploadFiles() {
        return '/api/file_controller/';
    }
})();