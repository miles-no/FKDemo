app.controller('ManageRulePopupController',function($scope,options,close){
  $scope.options = options;
  $scope.type = ['STRING','FLOAT','INTEGER']
  $scope.rule = options.body.bodyContent
  console.log($scope.options)
  $scope.dismissModal = function(result){
    close(result,200);
  }
});