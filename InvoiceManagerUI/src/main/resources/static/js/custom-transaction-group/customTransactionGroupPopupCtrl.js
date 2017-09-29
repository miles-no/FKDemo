const customTransactionGroupPopupCtrl = ($scope,options, close, $http,ModalService) => {
    $scope.options = options
    let bodyContent = options.body.bodyContent
    $scope.transactionBrands = []
    $scope.transactionsCategory = []


    let getBrands = () => {
        $http.get('/invoicemanager/api/brand/config/brand').then((response) => {
            $scope.transactionBrands = response.data
        })
    }
    let getTransactionCategory = () => {
        $http.get('/invoicemanager/api/custom/transaction/category').then((response) => {
            $scope.transactionsCategory = response.data
        })
    }

    $scope.onItemSelect = (item) => {
        $scope.selectedTransactionCategory.push(item)
    }


    $scope.onItemRemoval = (item) => {
        _.remove($scope.selectedTransactionCategory,(eachSelectedTemplate) => {
            return eachSelectedTemplate === item;
        });
    }

    $scope.onBrandSelect = (item,model) => {
    $scope.selectedBrand = item
    }

    let prepareModel = () => {
        let model = {
            name : $scope.transactionName,
            brand : $scope.selectedBrand,
            transactionCategories : $scope.selectedTransactionCategory
        }
        return  model
    }
    $scope.dismissModal = (result) => {
        close({operation : result, data :prepareModel()},200)
    }

    let init = () => {
        getBrands()
        getTransactionCategory()
        bodyContent.name !== '' ?  $scope.transactionName = bodyContent.name : $scope.transactionName = ''
        bodyContent.brand !== '' ?  $scope.selectedBrand = bodyContent.brand : $scope.selectedBrand = ''
        bodyContent.transactionCategories.length !== 0 ? $scope.selectedTransactionCategory = bodyContent.transactionCategories : $scope.selectedTransactionCategory = []
    }
    init();

}
export {customTransactionGroupPopupCtrl}