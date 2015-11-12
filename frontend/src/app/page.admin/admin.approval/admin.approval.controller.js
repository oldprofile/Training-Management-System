(function () {
    'use strict';

    angular
        .module('tmsApp')
        .controller('ApprovalController', ApprovalController);

    /** @ngInject */
    function ApprovalController($scope, $state, adminAPI) {
        var vm = this;
        //var stompClient = null;

        vm.approveList = [];
        //vm.disconnect = disconnect;
        vm.getApproveList = getApproveList;
        vm.makeText = makeText;
        vm.seeDetails = seeDetails;
        //vm.send = send;
        //vm.connect = connect;

        vm.getApproveList();

        //vm.connect();
        //
        //$scope.$on('$stateChangeStart', function() {
        //    vm.disconnect();
        //})
        //
        //function connect() {
        //    var socket = new SockJS('http://localhost:8080/approve_list');
        //    stompClient = Stomp.over(socket);
        //    stompClient.connect({}, {}, function(frame) {
        //        console.log('Connected: ' + frame);
        //        stompClient.subscribe('/pipe/approve_list', function(greeting){
        //            console.log(greeting);
        //        });
        //
        //        vm.send();
        //    });
        //}

        //function disconnect() {
        //    if (stompClient != null) {
        //        stompClient.disconnect();
        //    }
        //    console.log("Disconnected");
        //}
        //
        //function send() {
        //    debugger;
        //    stompClient.send("/approve_list", {}, JSON.stringify({ 'page': 0, 'page_size': 10 }))
        //}

        function getApproveList() {
            adminAPI.getApproveList().then(function(data) {
                vm.approveList = angular.copy(data);
            });
        }

        function makeText(type, tableName) {
            switch(type) {
                case 'CREATE':
                    return (tableName == 'APPROVE_TRAINING') ? ' wants to create training ' : ' wants to add lesson to training ';
                    break;
                case 'EDIT':
                    return (tableName == 'APPROVE_TRAINING') ? ' wants to edit training ' : ' wants to edit lesson in training ';
                    break;
                case 'REMOVE':
                    return (tableName == 'APPROVE_TRAINING') ? ' wants to delete training ' : ' wants to delete lesson from training ';
                default:
                    return 'unknown type';
            }
        }

        function seeDetails(item) {
            var type = item.type.toString();
            var tableName = item.tableName;
            switch(type) {
                case 'CREATE':
                    if(tableName == 'APPROVE_TRAINING') {
                        $state.go('managecourse', {courseId: item.trainingId, edit: true, id: item.id, type: type});
                    }
                    break;
                //case 'EDIT':
                //    return (tableName == 'APPROVE_TRAINING') ? ' wants to edit training ' : ' wants to edit lesson in training ';
                //    break;
                //case 'REMOVE':
                //    return (tableName == 'APPROVE_TRAINING') ? ' wants to delete training ' : ' wants to delete lesson from training ';
                default:
                    return '';
            }
        }
    }
})();