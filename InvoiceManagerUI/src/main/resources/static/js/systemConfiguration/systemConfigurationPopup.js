const systemConfigurationPopupController = ($scope,options,close) => {
  $scope.options = options;
  $scope.systemConfig = options.body.bodyContent
  $scope.dismissModal = (result) => {
    close(result,200);
  }
  $(document).on('shown.bs.modal', '.modal',() => {
    $(this).find('[autofocus]').focus();
  });
};
export {systemConfigurationPopupController}
