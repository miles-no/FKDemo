
const ManageBrandsPopupController = ($scope,options,close) =>{
  $scope.options = options;
  $scope.brand = options.body.bodyContent
  $scope.dismissModal = (result) => {
    close(result,200);
  }
  $(document).on('shown.bs.modal', '.modal', () => {
    $(this).find('[autofocus]').focus();
  });
};

export {ManageBrandsPopupController};
