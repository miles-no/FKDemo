app.controller('ManageBrandsPopupController',function($scope,options,close){
  $scope.options = options;
  $scope.brand = options.body.bodyContent
  $scope.dismissModal = function(result){
    close(result,200);
  }
});