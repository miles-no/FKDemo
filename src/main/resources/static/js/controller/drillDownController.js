/**
 * Created by Kshitij Bahul on 17-05-2017.
 */

var app = angular.module('invoiceManagerApp');

app.controller('drillDownController',function($scope,$http,moment,$rootScope,$stateParams){
    
    let dummyData = {"firstName": "Name ","lastName":"Last Name ","birthDate":moment().format('LLL'),"balance": 22.5};
    $scope.rowCollection = [];
    $scope.searchResults = [];
    $scope.states =[];
    $scope.datepickerConfig ={
        "dateFormat" :'YYYY-MM-DD HH:mm:ss',
        "minDate" : moment().subtract(20,'years'),
        "maxDate" : moment()

    }
    $scope.onStateSelect = function(item,model){
        console.log('onStateSelect ',item,model,$scope.states)
        $scope.states.push(item);
        $scope.getOverviewDetails();
    }
    $scope.onStateRemoval = function(item,model){
        console.log('onStateRemoval ',item,model,$scope.states)
        $scope.states = _.remove($scope.states,function(eachSelectedState){
            return eachSelectedState.$$hashKey === item.$$hashKey;
        });
    }
    $scope.getOverviewDetails = function(){
        let queryParams = {
            "states":_.join($scope.states,','),
            "fromDate" :$scope.fromDate,
            "toDate" : $scope.toDate
        }
        $http.get('/findOverview',queryParams).then(function success(result){
            $scope.searchResults = result.data
        },function error(error){
            console.log('Error in getting skill results');
        });
    }
    $scope.init = function(){
        console.log($rootScope,$stateParams);
        $scope.fromDate = moment().hour(0).minute(0).second(0).format('YYYY-MM-DD HH:mm:ss');
        $scope.toDate= moment().hour(23).minute(59).second(59).format('YYYY-MM-DD HH:mm:ss');
        $scope.possibleStates = $rootScope.states;
        $scope.possibleStates = [{"name":"PENDING","value":0},{"name":"PRE-PROCESSING","value":0},{"name":"PROCESSING","value":0},{"name":"MERGING","value":1},{"name":"READY","value":166},{"name":"FAILED","value":0},{"name":"Total","value":167}];
        $stateParams && $stateParams.processingState ? $scope.states.push($stateParams.processingState) :'';
        for(var i =0; i<100; i++){
            $scope.rowCollection.push({"firstName":`${dummyData.firstName} ${i}`,"lastName":`${dummyData.lastName}  ${i}`,"birthDate":moment().format('LLL'),"balance": 22.5})
        }
    }
});