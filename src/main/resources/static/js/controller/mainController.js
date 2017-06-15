/**
 * Created by Kshitij Bahul on 16-05-2017.
 */

var app = angular.module('invoiceManagerApp');

app.controller('mainController',function($scope,$rootScope){
    $scope.init = function(){
      console.log('Here in mainController')  ;
    };
    $scope.isDashbordLive = true;
    $rootScope.isDashbordLive = $scope.isDashbordLive;
    $scope.toggleDashbordLive = function(){
      $scope.isDashbordLive = !$scope.isDashbordLive;
    }
});
