app.controller('gridPopupController',function($scope,options,close){
  $scope.options = options;
  $scope.grids = options.body.bodyContent
  $scope.dismissModal = function(result){
    close(result,200);
  }
  $(document).on('shown.bs.modal', '.modal', function() {
    $(this).find('[autofocus]').focus();
  });
});
