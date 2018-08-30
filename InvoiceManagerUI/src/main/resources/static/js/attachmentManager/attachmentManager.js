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

    $scope.loading = false
    $scope.previewLayout = (attachment) => {
        var id = attachment.attachmentId
        $scope.loading = true
        $http({
            method : 'GET',
            url : '/invoicemanager/api/attachment/content/'+id,
            responseType : 'arraybuffer'
        }).then((response,status,headers) => {
            $scope.loading = false
            if((attachment.attachmentType).toLowerCase() == 'image'){
            if((attachment.fileExtension).toLowerCase() == 'jpg'){
                var file = new Blob([response.data], {type: 'image/jpeg'});
            } else if ((attachment.fileExtension).toLowerCase() == 'png'){
                var file = new Blob([response.data], {type: 'image/png'});
            } else {
                var file = new Blob([response.data], {type: 'image/'+`${attachment.fileExtension}`});
        }
        } else
        {
            var file = new Blob([response.data], {type: 'application/pdf'});
        }
        var fileURL = window.URL.createObjectURL(file);
        if((attachment.attachmentType).toLowerCase() == 'image'){
            ModalService.showModal({
                templateUrl: 'templates/image-modal/image-display-modal.html',
                controller: 'imageDisplayController',
                inputs:{
                    ImageUrl :fileURL,
                }
            }).then((modal) => {
                modal.element.modal();
            modal.close.then((result) => {
            });
        })
        } else{
            ModalService.showModal({
                templateUrl: 'templates/pdf-modal/pdf-display-modal.html',
                controller: 'pdfDisplayController',
                inputs:{
                    pdfUrl :fileURL
                }
            }).then((modal) => {
                modal.element.modal();
            modal.close.then((result) => {
            });
        })
        };
    }).error((error) => {
        $scope.loading = false
    }
    )
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
        if((attachment.attachmentType).toLowerCase() == 'image'){
            if((attachment.fileExtension).toLowerCase() == 'jpg'){
                var file = new Blob([response.data], {type: 'image/jpeg'});
            } else if ((attachment.fileExtension).toLowerCase() == 'png'){
                var file = new Blob([response.data], {type: 'image/png'});
            } else {
                var file = new Blob([response.data], {type: 'image/'+`${attachment.fileExtension}`});
        }
    } else {
        var file = new Blob([response.data], {type: 'application/pdf'});
    }

    var  testLink = window.URL.createObjectURL(file)
    console.log(testLink)
    var downloadLink = angular.element('<a></a>');
    downloadLink.attr('href',window.URL.createObjectURL(file));
    if((attachment.attachmentType).toLowerCase() == 'image'){
        if((attachment.fileExtension).toLowerCase() == 'jpg'){
            downloadLink.attr('download', attachment.attachmentTypeName+'_'+attachment.brandName+'.jpg');
        } else if ((attachment.fileExtension).toLowerCase() == 'png'){
            downloadLink.attr('download', attachment.attachmentTypeName+'_'+attachment.brandName+'.png');
        } else {
            downloadLink.attr('download', attachment.attachmentTypeName+'_'+attachment.brandName+attachment.fileExtension);
        }
    } else {
        downloadLink.attr('download', attachment.attachmentTypeName+'_'+attachment.brandName);
    }
    downloadLink[0].click();
    })
    }

    let showModal  = (attachmentObj, type) => {
        ModalService.showModal({
            templateUrl: 'templates/attachmentManager/attachmentManagerPopup.html',
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
               if(response){
                   $scope.alerts.push({ type: 'success', msg: 'Attachment Added successfully' })
                   $scope.getAttachments()
               } else {
                   $scope.alerts.push({ type: 'danger', msg: 'Set Configuration Already Exists' })
               }
        } else if (type === 'Update') {
            $scope.alerts.push({ type: 'success', msg: 'Attachment Updated successfully' })
            $scope.getAttachments()
        }
    })
    })
    }
}

export {attachmentManagerController}