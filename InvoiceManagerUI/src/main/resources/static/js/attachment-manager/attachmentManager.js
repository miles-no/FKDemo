const attachmentManagerController = ($scope,$http,ModalService) => {

    $scope.attachmentObj = {
        attachmentId: "",
        attachmentType: "",
        attachmentTypeId: "",
        attachmentTypeName: "",
        brandName: ""
    }
    $scope.pageSize = 10
    $scope.alerts = []
    $scope.attachmentdata = []
    $scope.attachmentCopy = []
    $scope.selectedBrands = []
    $scope.allCounts = [10,25,50,100]

    $scope.getAttachments = () =>{
        $scope.getBrands()
        $http.get('/invoicemanager/api/attachment').then((response) => {
            $scope.attachmentdata = $scope.attachmentCopy = response.data
        })
    }

    $scope.getBrands = () => {
        $http.get('/invoicemanager/api/brand/config/brand').then((response) => {
            $scope.dropDownData = response.data;
        })
    }

    let showBrands = () => {
        $scope.attachmentdata = []
        if($scope.selectedBrands.length > 0){
            angular.forEach($scope.attachmentCopy,(item) => {
                angular.forEach($scope.selectedBrands,(selectedItem) => {
                if (selectedItem === item.brandName){
                $scope.attachmentdata.push(item)
            }
        })
        $scope.attachmentdata = _.uniqBy($scope.attachmentdata,(e) => {
                return e
            })
    })
    }
    else{
        $scope.attachmentdata = $scope.attachmentCopy
    }
    }

    $scope.onCountSelect = (item, model) => {
        $scope.pageSize = item
    }

    $scope.onBrandSelect = (item) => {
        $scope.selectedBrands.push(item);
        showBrands()
    }
    $scope.onBrandRemoval = (item) => {
            _.remove($scope.selectedBrands,(eachSelectedBrand) => {
                return eachSelectedBrand === item;
        });
        showBrands()
    }

    $scope.closeAlert = (index) => {
        $scope.alerts.splice(index,1)
    }

    $scope.updateAttachment = (attachment) => {
        showModal(attachment,'Update')
    }

    $scope.deleteAttachment =  (attachment) => {
        ModalService.showModal({
            templateUrl: 'js/modals/confirmDelete.html',
            controller:'popupController',
            inputs:{
                options:{
                    body:{
                        bodyContent: 'Please confirm to delete ' + attachment.attachmentTypeName,
                        brand: attachment.brandName
                    },
                    header: "Delete Attachment",
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
        }).then(function(modal){
            modal.element.modal();
            modal.close.then((result) => {
                if(result=='Delete'){
                $http.delete('/invoicemanager/api/attachment/attachment/'+attachment.attachmentId).then((response) => {
                    if (response.status === 200){
                    $scope.alerts.push({ type: 'success', msg: 'Attachment deleted successfully' })
                    $scope.getAttachments();
                }
            else{
                    $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
                }
            },(err) => {
                $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
            })
        }
    })
    })
    }

    $scope.addAttachment =  () => {
        var newAttchmentObj = angular.copy($scope.attachmentObj);
        showModal(newAttchmentObj, 'Add')
    }

    $scope.downloadAttachment = (attachment) => {
        var id = attachment.attachmentId
        $http({
            method : 'GET',
            url : '/invoicemanager/api/attachment/content/'+id,
            responseType : 'arraybuffer'
        }).then((response,status,headers) => {
        if(attachment.attachmentType == 'image'){
            if(attachment.fileExtension == 'jpg'){
                var file = new Blob([response.data], {type: 'image/jpeg'});
            } else if (attachment.fileExtension == 'png'){
                var file = new Blob([response.data], {type: 'image/png'});
            } else {
                var file = new Blob([response.data], {type: 'image/'+`${attachment.fileExtension}`});
        }
    } else {
        var file = new Blob([response.data], {type: 'application/pdf'});
    }
    var downloadLink = angular.element('<a></a>');
    downloadLink.attr('href',window.URL.createObjectURL(file));
    if(attachment.attachmentType == 'image'){
        if(attachment.fileExtension == 'jpg'){
            downloadLink.attr('download', attachment.attachmentTypeName+'.jpeg');
        } else if (attachment.fileExtension == 'png'){
            downloadLink.attr('download', attachment.attachmentTypeName+'.png');
        } else {
            downloadLink.attr('download', attachment.attachmentTypeName+attachment.fileExtension);
        }
    } else {
        downloadLink.attr('download', attachment.attachmentTypeName);
    }
    downloadLink[0].click();
    })
    }

    let showModal  = (attachmentObj, type) => {
        ModalService.showModal({
            templateUrl: 'templates/attachment-manager/attachmentManagerPopup.html',
            controller: 'attachmentManagerPopupController',
            inputs : {
                options: {
                    body:{
                        bodyContent :attachmentObj
                    },
                    type : type,
                    header: type === 'Add' ? 'Add new Attachment' : 'Update Attachment',
                    conFirmBtnText : [
                        {name: 'cancel'},
                        {name: type }
                    ],
                    classes: {
                        modalBody: '',
                        body: 'manage-brand'
                    }
                }
            }
        }).then((modal) => {
        modal.element.modal();
        modal.close.then((response) => {
            if (type === 'Add') {
            $scope.alerts.push({ type: 'success', msg: 'Attachment Added successfully' })
            $scope.getAttachments()
        } else if (type === 'Update') {
            $scope.alerts.push({ type: 'success', msg: 'Attachment Updated successfully' })
            $scope.getAttachments()
        }
    })
    })
    }
}

export {attachmentManagerController}