const popupController = ($scope,options,close) => {
  $scope.options = options;
  $scope.items = options.body.bodyContent
  $scope.dismissModal = (result) => {
    close(result,200);
  }
};

export {popupController};
