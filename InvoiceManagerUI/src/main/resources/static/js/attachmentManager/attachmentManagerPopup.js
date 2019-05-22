const attachmentManagerPopupController = ($scope,options,$http,close) => {
    $scope.options = options
    $scope.modalData = options.body.bodyContent
    $scope.configList = []
    $scope.brandList = []
    $scope.selectedBrand = ''
    $scope.attachmentTypeList = [
        'PDF','IMAGE'
    ]

    let intializeController = () => {
        getAllConfig()
        getAllBrands()
    }

    let getAllConfig = () => {
        $http.get('/invoicemanager/api/attachment/config/all').then((response) => {
            $scope.configList = response.data
        })
    }

    let getAllBrands = () => {
        $http.get('/invoicemanager/api/brand/config/brand').then((response) => {
            $scope.brandList = response.data
        })
    }

    $scope.uploadAttachment = () => {
        var fd = new FormData()
        fd.append('attachmentConfigId',$scope.modalData.attachmentConfigId)
        fd.append('name',$scope.modalData.brandName)
        fd.append('type',$scope.modalData.attachmentType)
        fd.append('file', $scope.file)
        $http.post('/invoicemanager/api/attachment/attachment',fd,{
                transformRequest: angular.identity,
                headers: { 'Content-Type': undefined}
            }).then((response) => {
                if(response.data !== 'Attachment Already Exists'){
                    $scope.file = null
                    $scope.dismissModal(true)
                } else {
                    $scope.dismissModal(false)
                }
        })

    }

    $scope.updateAttachment = () => {
        var fd = new FormData()
        fd.append('attachmentConfigId',$scope.modalData.attachmentConfigId)
        fd.append('name',$scope.modalData.brandName)
        fd.append('type',$scope.modalData.attachmentType)
        fd.append('file', $scope.file)
        $http.put('/invoicemanager/api/attachment/content/'+$scope.modalData.attachmentId,fd,{
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined}
        }).then((response) => {
            if(response.status == 200){
            $scope.file = null
            $scope.dismissModal()
        }
    })

    }

    $scope.checkType = () => {
        if($scope.file){

            if($scope.file.type == 'application/pdf' && $scope.modalData.attachmentType == 'PDF'){
                return false
            }
            else if($scope.file.type.startsWith('image/') && $scope.modalData.attachmentType == 'IMAGE'){
                return false
            }
        }
        return true
    }

    $scope.dismissModal = (result) => {
        close(result,200);
    }

    $scope.onConfigSelect = (item,model) => {
        $scope.modalData.attachmentConfigId = item.id
    }

    $scope.onBrandSelect = (item,model) => {
        $scope.modalData.brandName = item
    }

    $scope.onTypeSelect = (item,model) => {
        $scope.modalData.attachmentType = item
    }


    intializeController()
}

export {attachmentManagerPopupController}
