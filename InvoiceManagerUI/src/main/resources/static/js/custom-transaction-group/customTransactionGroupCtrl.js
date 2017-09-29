const customTransactionGroupController = ($scope, $http, ModalService) => {
    $scope.allTransactions = []
    let transactionCopy = []
    $scope.selectedBrand = []
    let transcationModal = {
        name : '',
        transactionType : '',
        brand : '',
        transactionCategories: '',
    }

    var categoryList = ''
    $scope.transactionInit = () => {
        getCustomRecords()
        getBrands()
    }
    let getCustomRecords = () => {
        $http.get('/invoicemanager/api/custom/transaction/group/all').then((response) => {
            $scope.allTransactions = []
            angular.forEach(response.data , (value) => {
                categoryList = ''
                angular.forEach(value.transactionCategories , (categoryKey) => {
                    //categoryList.append(categoryKey.category)
                    categoryList +=  categoryKey.category + ', '
                })
                 var correctString = categoryList.substring(0,categoryList.length-2)
                 value.categoryString = correctString
                 $scope.allTransactions.push(value)
            })
            transactionCopy = angular.copy($scope.allTransactions)
       })
    }

    let showSelectedBrands  = () => {
        $scope.allTransactions = []
        if($scope.selectedBrand.length > 0){
            angular.forEach(transactionCopy, (tableKey) => {
                angular.forEach($scope.selectedBrand , (selectedKey) => {
                    if (tableKey.brand === selectedKey) {
                        $scope.allTransactions.push(tableKey)
                    }
                })
            })
        } else {
            $scope.allTransactions = transactionCopy
        }
    }

    $scope.onBrandSelect = (item) => {
        $scope.selectedBrand.push(item);
        showSelectedBrands()
    }

    $scope.onBrandRemoval = (item) => {
        _.remove($scope.selectedBrand,(eachSelectedBrand) => {
            return eachSelectedBrand === item;
    });
        showSelectedBrands()
    }
    let getBrands = () => {
        $http.get('/invoicemanager/api/brand/config/brand').then((response) => {
            $scope.allBrands = response.data
        })
    }

    $scope.deleteTransaction = (data) => {
        ModalService.showModal({
            templateUrl: 'js/modals/confirmDelete.html',
            controller:'popupController',
            inputs:{
                options:{
                    body:{
                        bodyContent: 'Please confirm to delete '+ data.name
                    },
                    header: "Delete Transaction",
                    conFirmBtnText : [
                        {name: 'cancel'},
                        {name: "Delete" }
                    ],
                    classes: {
                        modalBody: '',
                        body: 'manage-brand'
                    }
                }
            }
        }).then((modal) => {
            modal.element.modal();
        modal.close.then((result) => {
            if(result=='Delete'){
            $http.delete('/invoicemanager/api/custom/transaction/group/'+data.id).then((response) => {
                if (response.status === 200){
                    getCustomRecords();
                }
            },(err) => {
                        console.log('Some error Occured')
                    })
                }
            })
        })
    }

    let showModal = (transactionInfo, type) =>  {
        ModalService.showModal({
            templateUrl: '/invoicemanager/api/templates/custom-transaction-group/customTransactionGroupPopup.html',
            controller: 'customTransactionGroupPopupCtrl',
            inputs: {
                options: {
                    body:{
                        bodyContent :transactionInfo
                    },
                    header: type === 'Add' ? 'Add new transaction' : 'Update Transaction',
                    conFirmBtnText : [
                        {name: 'cancel'},
                        {name: type }
                    ],
                    type: type,
                    classes: {
                        modalBody: '',
                        body: 'manage-brand'
                    }
                }
            }
        }).then((modal) => {
            modal.element.modal();
            modal.close.then((response) => {
                if (response.operation === 'Add') {
                    addTransactionCategory(response.data)
                } else if (response.operation === 'Update') {
                    updateTransactionCategory(response.data ,transactionInfo.id)
                }
            });
        });
    }

    let updateTransactionCategory = (updatedTransaction,id) => {
     $http.put('/invoicemanager/api/custom/transaction/group/' + id , updatedTransaction).then((response) => {
         getCustomRecords()
     })
    }
    let addTransactionCategory = (data) => {
        $http.post('/invoicemanager/api/custom/transaction/group',data).then((response) => {
            getCustomRecords()
        })
    }
    $scope.addTransaction = () => {
        var transaction = angular.copy(transcationModal);
        showModal(transaction, 'Add')
    }

    $scope.updateTransaction = (transactionData) => {
        var transaction = angular.copy(transactionData)
        showModal(transaction, 'Update')
    }

}

export {customTransactionGroupController}