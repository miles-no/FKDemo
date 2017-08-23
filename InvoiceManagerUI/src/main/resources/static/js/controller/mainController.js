
app.controller('mainController',function($scope,$rootScope, $state){
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
});