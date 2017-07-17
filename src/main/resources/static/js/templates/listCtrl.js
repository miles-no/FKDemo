app.controller('listCtrl',function($scope,ModalService,$http){

  var newLayout = {
      brand: '',
      legalPartClass: '',
      accountCategory: '',
      distributionMethod: '',
      creditLimit: ''
  }

  $scope.selectedTemplate = []
  $scope.templatelist = []

  $scope.onTemplateSelect = function(item){
    $scope.selectedTemplate.push(item)
    showSelectedTemplate()
  }

  $scope.onTemplateRemoval = function(item){
    _.remove($scope.selectedRule,function(eachSelectedTemplate){
      return eachSelectedTemplate === item;
    });
    showSelectedTemplate()
  }

  function showSelectedTemplate (){
    $scope.tableRule = []
    if($scope.selectedTemplate.length > 0){
      angular.forEach($scope.allLayouts,function(item){
        angular.forEach($scope.selectedTemplate,function(selectedItems){
          if(selectedItems === item.brand){
            $scope.tableRule.push(item)
          }
        })
      })
    }else{
      $scope.tableRule = $scope.allLayouts
    }
  }

  $scope.onTemplateSelect = function(item){
    $scope.selectedTemplate.push(item)
    showSelectedTemplate()
  }
  $scope.onTemplateRemoval = function(item){
    _.remove($scope.selectedTemplate,function(eachSelectedTemplate){
      return eachSelectedTemplate === item;
    });
    showSelectedTemplate()
  }


  function setActiveLayout (active){
    var layoutId = active.layoutID
    var activeId = active.version
    $http.put('/layout/activate/'+layoutId+'/'+activeId).then(function(response){
      $scope.getLayouts();
    })
  }

  function addLayout (layout) {
     $http.post('/layout/rule',layout).then(function (response) {
         $scope.getLayouts()
     })
  }
  
  function updateLayout(layout) {
     $http.put(`/layout/rule/${layout.id}`,layout).then(function (response) {
         $scope.getLayouts()
     })
  }

  function showActiveModal (layoutInfo){
    console.log(layoutInfo)
    ModalService.showModal({
      templateUrl: 'js/modals/confirmDelete.html',
      controller:'popupController',
      inputs:{
        options:{
          body:{
            bodyContent: 'Are you sure you want to Activate Layout '+layoutInfo.name ,
            brand: layoutInfo
          },
          header: "Confirm Layout Activation",
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
    }).then(function(modal){
      modal.element.modal();
      modal.close.then(function(response){
        if (response === 'Confirm') {
          var activate = {}
          activate.layoutID = layoutInfo.layoutID
          activate.version = layoutInfo.version
          setActiveLayout(activate)
        }
      });
    })
  }
  
  function showModal (layoutInfo, type) {
    console.log(layoutInfo)
      ModalService.showModal({
          templateUrl: 'js/templates/listPopUp.html',
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
      }).then(function(modal){
          modal.element.modal();
          modal.close.then(function(response){
              if (response.operation === 'Add') {
                  addLayout(response.data)
              } else if (response.operation === 'Update') {
                  updateLayout(response.data)
              }
          });
      });
  }
  $scope.updateTemplate = function (layoutInfo) {
      var layoutDetails = angular.copy(layoutInfo)
      showModal(layoutDetails, 'Update')
  }

  $scope.addTemplate = function () {
        //var layout = angular.copy(newLayout);
        showModal(null, 'Add')
  }
  $scope.loading = false
  $scope.previewLayout = function(layout, $event){
    $event.stopPropagation();
    var layoutId = layout.layoutID
    var version = layout.version;
    let qp = {layoutId: layoutId, version: version};
    $scope.loading = true
    $http.get('/layout/preview',{params: qp,responseType: 'arraybuffer'}).success(function(response,status,headers){
      $scope.loading = false
      var file = new Blob([response], {type: 'application/pdf'});
      var fileURL = URL.createObjectURL(file);
      ModalService.showModal({
        templateUrl: 'templates/pdf-display-modal.html',
        controller: 'pdfDisplayController',
        inputs:{
          pdfUrl :fileURL
        }
      }).then(function(modal){
        modal.element.modal();
        modal.close.then(function(result){
          console.log('The modal got closed');
        });
      });
    }).error(
      function(error){
        $scope.loading = false
        console.log(error);
      }
    )
  }


 $scope.getLayouts = function () {
    $http.get('/layout/template/all').then(function (response) {
      $scope.tableRule = $scope.allLayouts =  response.data;
      angular.forEach($scope.allLayouts, function(item){
        $scope.templatelist.push(item.brand)
      })
     $scope.templatelist =  _.uniqBy($scope.templatelist,function(e){
       return e
     })
    }).catch(function(data){
      $scope.allLayouts  = data;
    })
 }

  $scope.getBrands = function(){
    $http.get('/layout/rules').then(function(response){
      $scope.brands = response.data
    }).catch(function(data){
      $scope.brands = data;
    })
  }

  $scope.setActive = function(rule, $event){
      $event.stopPropagation();
      showActiveModal(rule)


  }

  $scope.deleteTemplate = function(template,$event){
    $event.stopPropagation();
    $http.delete('').then(function(){
      $scope.getLayouts();
    })
  }


  $scope.dropdown = {}
  var pre = {}
  $scope.dropDownList=function($event,user){
    $event.stopPropagation();
    if(pre === user){
      $scope.dropDown = {}
      pre = {}
    } else {
      $scope.dropDown = user;
      pre = user
    }
  }
});