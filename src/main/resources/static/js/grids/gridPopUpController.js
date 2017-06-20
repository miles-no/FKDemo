app.controller('popupController',function($scope,options,close){
  $scope.options = options;
  $scope.items = options.body.bodyContent
  $scope.dismissModal = function(result){
    close(result,200);
  }
});
