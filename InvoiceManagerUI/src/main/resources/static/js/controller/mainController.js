/**
 * Created by Kshitij Bahul on 17-05-2017.
 */
'use strict';
const mainController = ($scope,$rootScope, $state) => {
  $scope.init = function(){
    console.log('Here in mainController')  ;
  };
  $scope.showMenu = false;
  $scope.activeMenu = 'overview'
  $scope.isDashbordLive = true;
  $rootScope.isDashbordLive = $scope.isDashbordLive;
  $scope.toggleDashbordLive = function(){
    $scope.isDashbordLive = !$scope.isDashbordLive;
  }

  $scope.navigateTo = function (url) {
    $scope.activeMenu = url
    $scope.showMenu = !$scope.showMenu
    $state.go(url)
  }
}
export {mainController};