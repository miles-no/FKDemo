app.controller('templateAttributePopupController',function($scope,options,close){
  $scope.options = options;
  $scope.type = ['STRING','FLOAT','INTEGER']
  $scope.rule = options.body.bodyContent
  var text = ''
  if ($scope.rule.options !== null){
    angular.forEach($scope.rule.options,function(item,index){
      text = text + item
      if (index !== ($scope.rule.options.length - 1)){
        text = text + ', '
      }
    })
    $scope.rule.options = text
  }
  $scope.dismissModal = function(result){
    close(result,200);
  }
  $(document).on('shown.bs.modal', '.modal', function() {
    $(this).find('[autofocus]').focus();
  });
});