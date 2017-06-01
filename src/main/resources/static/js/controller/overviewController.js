/**
 * Created by Kshitij Bahul on 17-05-2017.
 */

var app = angular.module('invoiceManagerApp');

app.controller('overviewController',function($scope,$http,moment){
    $scope.date1;
    let dummyData = {"firstName": "Name ","lastName":"Last Name ","birthDate":moment().format('LLL'),"balance": 22.5};
    $scope.rowCollection = [
        
    ]
    $scope.init = function(){
        $scope.name='Kshitij';
        for(var i =0; i<100; i++){

            $scope.rowCollection.push({"firstName":`${dummyData.firstName} ${i}`,"lastName":`${dummyData.lastName}  ${i}`,"birthDate":moment().format('LLL'),"balance": 22.5})
        }
    }
});