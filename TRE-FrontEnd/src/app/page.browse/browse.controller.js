(function () {
    'use strict';

    angular
        .module('tmsApp')
        .controller('BrowseController', BrowseController);

    /** @ngInject */
    function BrowseController(courseAPI) {
        var vm = this;
        vm.courseList = [];
        vm.tagList = [];
        vm.selectedTagIds = [];
        vm.isContentLoaded = false;

        vm.getTagList = getTagList;
        vm.getTrainingList = getTrainingList;
        vm.isTagSelected = isTagSelected;
        vm.selectTag = selectTag;

        vm.getTagList();
        vm.getTrainingList();

        function getTagList() {
            courseAPI.getAllTags().then(function(data) {
                vm.tagList = angular.copy(data);
                console.log('Received tags: ', data);
            });
        }

        function getTrainingList() {
            //false stands for isActual
            courseAPI.getCourseList(false).then(function(data) {
                vm.courseList = angular.copy(data);
                vm.isContentLoaded = true;
                console.log('Received courses: ');
                console.log(data);
            });
        }

        function isTagSelected(tagId) {
            return (vm.selectedTagIds.indexOf(tagId) > -1);
        }

        function selectTag(tagId) {
            var tagIndex = vm.selectedTagIds.indexOf(tagId);
            if(tagIndex > -1) {
                vm.selectedTagIds.splice(tagIndex, 1);
            }
            else {
                vm.selectedTagIds.push(tagId);
            }
        }
    }
})();