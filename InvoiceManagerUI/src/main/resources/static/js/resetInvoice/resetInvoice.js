const resetInvoiceController = ($scope,$http,$state) => {
    $scope.navigateTo = (url) => {
            if (! url){
              $state.go('home');
            }else{
              $state.go(url);
            }
          }

    $scope.resetInvoice = () => {
        $http({
                   method : 'PUT',
                   url : '/invoicemanager/api/reset/invoice',
               }).then( function success(result){
                                    $scope.navigateTo('home');
                                })
              }
    }

export {resetInvoiceController}