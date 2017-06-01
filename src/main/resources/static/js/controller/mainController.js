/**
 * Created by Kshitij Bahul on 16-05-2017.
 */

var app = angular.module('invoiceManagerApp');

app.controller('mainController',function($scope,$http){
    $scope.init = function(){
      console.log('Here in mainController')  ;
    };
});
