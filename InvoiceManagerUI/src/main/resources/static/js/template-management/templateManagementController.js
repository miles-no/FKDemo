const listCtrl = ($scope,ModalService,$http) => {
  var newLayout = {
      brand: '',
      legalPartClass: '',
      accountCategory: '',
      distributionMethod: '',
      creditLimit: ''
  }

  $scope.selectedTemplate = []
  $scope.templatelist = []
  $scope.alerts =[];
  $scope.pageSize = ''

  $scope.onTemplateSelect = (item) => {
    $scope.selectedTemplate.push(item)
    showSelectedTemplate()
  }

  $scope.onTemplateRemoval = (item) => {
    _.remove($scope.selectedRule,(eachSelectedTemplate) => {
      return eachSelectedTemplate === item;
    });
    showSelectedTemplate()
  }

  let showSelectedTemplate = () => {
    $scope.tableRule = []
    if($scope.selectedTemplate.length > 0){
      angular.forEach($scope.allLayouts,(item) => {
        angular.forEach($scope.selectedTemplate,(selectedItems) => {
          if(selectedItems === item.brand){
            $scope.tableRule.push(item)
          }
        })
      })
    }else{
      $scope.tableRule = $scope.allLayouts
    }
  }

  $scope.onTemplateSelect = (item) => {
    $scope.selectedTemplate.push(item)
    showSelectedTemplate()
  }
  $scope.onTemplateRemoval = (item) => {
    _.remove($scope.selectedTemplate,(eachSelectedTemplate) => {
      return eachSelectedTemplate === item;
    });
    showSelectedTemplate()
  }

  $scope.closeAlert = (index) => {
    $scope.alerts.splice(index,1)
  }

  let setActiveLayout =  (active) => {
    var layoutId = active.layoutID
    var activeId = active.version
    $http.put('/invoicemanager/api/layout/activate/'+layoutId+'/'+activeId).then((response) => {
      $scope.getLayouts();
    })
  }

  let deActivateLayout = (active) => {
    var layoutId = active.layoutID
    var deActiveId = active.version
    $http.put('/invoicemanager/api/layout/deActivate/'+layoutId+'/'+deActiveId).then((response) => {
      $scope.getLayouts();
    })
  }

  let addLayout = (layout) => {
     $http.post('/invoicemanager/api/layout/rule',layout).then((response) => {
       if(response.status === 200){
         $scope.getLayouts()
         $scope.alerts.push({ type: 'success', msg: 'Record added successfully' })
       }
       else{
         $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
       }
     },(err) => {
       $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
     })
  }
  
  let updateLayout = (layout) => {
     $http.put(`/invoicemanager/api/layout/rule/${layout.id}`,layout).then((response) => {
      if(response.status === 200){
        $scope.getLayouts()
        $scope.alerts.push({ type: 'success', msg: 'Record update successfully' })
      }
      else{
        $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
      }
    },(err) => {
      $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
    })
  }

  let toggleLayoutModal = (layoutInfo) => {
    console.log(layoutInfo)
    if (layoutInfo.active === true){
      var bodyTitle = 'Are you sure you want to deactivate '
      var headerText = 'Confirm Layout deactivation'
    }else{
      var bodyTitle = 'Are you sure you want to activate '
      var headerText = 'Confirm Layout activation'
    }
    ModalService.showModal({
      templateUrl: 'js/modals/confirmDelete.html',
      controller:'popupController',
      inputs:{
        options:{
          body:{
            bodyContent: bodyTitle +layoutInfo.name ,
            brand: layoutInfo
          },
          header: headerText,
          conFirmBtnText : [
            {name: 'Cancel'},
            {name: 'Confirm' }
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
        if (response === 'Confirm') {
          var activate = {}
          activate.layoutID = layoutInfo.layoutID
          activate.version = layoutInfo.version
          if(layoutInfo.active === false){
            setActiveLayout(activate)
          }else{
            deActivateLayout(activate)
          }
        }
      });
    })
  }
  
  let showModal  = (layoutInfo, type) => {
    console.log(layoutInfo)
      ModalService.showModal({
          templateUrl: 'templates/template-management/templateManagementPopup.html',
          controller: 'listPopupController',
          inputs : {
              options: {
                  body:{
                      bodyContent :layoutInfo,
                      layouts: $scope.layouts
                  },
                  header: type === 'Add' ? 'Add new Layout' : 'Update '+ layoutInfo.name,
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
              if (response.operation === 'Add') {
                  addLayout(response.data)
              } else if (response.operation === 'Update') {
                updateLayout(response.data)
              }
          });
      });
  }
  $scope.updateTemplate = (layoutInfo) => {
      var layoutDetails = angular.copy(layoutInfo)
      showModal(layoutDetails, 'Update')
  }

  $scope.addTemplate = () => {
        //var layout = angular.copy(newLayout);
        showModal(null, 'Add')
  }
  $scope.loading = false
  $scope.previewLayout = (layout) => {
    var layoutId = layout.layoutID
    var version = layout.version;
    let qp = {layoutId: layoutId, version: version};
    $scope.loading = true
    $http.get('/im-pdfgenerator/api/preview',{params: qp,responseType: 'arraybuffer'}).success((response,status,headers) => {
      $scope.loading = false
      var file = new Blob([response], {type: 'application/pdf'});
      var fileURL = URL.createObjectURL(file);
      ModalService.showModal({
        templateUrl: 'templates/pdf-modal/pdf-display-modal.html',
        controller: 'pdfDisplayController',
        inputs:{
          pdfUrl :fileURL
        }
      }).then((modal) => {
        modal.element.modal();
        modal.close.then((result) => {
          console.log('The modal got closed');
        });
      });
    }).error((error) => {
        $scope.loading = false
        console.log(error);
      }
    )
  }

  $scope.downLoadLayout = (layout) => {
    var id = layout.layoutID
    $http({
      method : 'GET',
      url : '/invoicemanager/api/layout/rptdesign?id='+id,
    }).then((response,status,headers) => {
      var file = new Blob([response.data], {type: 'application/xml'});
      var downloadLink = angular.element('<a></a>');
      downloadLink.attr('href',window.URL.createObjectURL(file));
      downloadLink.attr('download', layout.name+'.rptdesign');
      downloadLink[0].click();
    })
  }
  $scope.getLayouts =  () => {
    $http.get('/invoicemanager/api/layout/template/all').then( (response)  => {
      $scope.tableRule = $scope.allLayouts =  response.data;
      angular.forEach($scope.allLayouts,(item) => {
        $scope.templatelist.push(item.brand)
      })
     $scope.templatelist =  _.uniqBy($scope.templatelist,(e) => {
       return e
     })
    }).catch((data) => {
      $scope.allLayouts  = data;
    })
 }

  $scope.getBrands = () => {
    $http.get('/invoicemanager/api/layout/rules').then((response) => {
      $scope.brands = response.data
    }).catch((data) => {
      $scope.brands = data;
    })
  }

  $scope.toggleLayout = (rule) => {
      toggleLayoutModal(rule)
  }

  $scope.deleteTemplate = (template) => {
    var id = template.layoutID
    ModalService.showModal({
      templateUrl: 'js/modals/confirmDelete.html',
      controller:'popupController',
      inputs:{
        options:{
          body:{
            bodyContent: 'Please confirm to delete '+template.name
          },
          header: 'Delete Template',
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
            $http.delete('/invoicemanager/api/layout/template/'+id).then((response) => {
              if (response.status === 200){
                $scope.alerts.push({ type: 'success', msg: 'Record deleted successfully' })
                $scope.getLayouts();
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


  $scope.dropdown = {}
  var pre = {}
  $scope.dropDownList= (user) => {
    if(pre === user){
      $scope.dropDown = {}
      pre = {}
    } else {
      $scope.dropDown = user;
      pre = user
    }
  }
};

export {listCtrl}