app.controller('listCtrl',function($scope,ModalService,$http){

  //$scope.layouts =[];
  var newLayout = {
      brand: '',
      legalPartClass: '',
      accountCategory: '',
      distributionMethod: '',
      creditLimit: ''
  }


  //$scope.selectedLayouts = []
  //$scope.allLayouts = []
  //function showSelectedLayouts () {
  //    $scope.allLayouts = []
  //    if ($scope.selectedLayouts.length > 0) {
  //        angular.forEach($scope.layouts, function (layoutItem) {
  //            angular.forEach($scope.selectedLayouts, function (selectedLayout) {
  //                if(layoutItem.layout === selectedLayout.layout) {
  //                    $scope.allLayouts.push(layoutItem)
  //                }
  //            })
  //        })
  //    } else {
  //        $scope.allLayouts =  $scope.layouts;
  //    }
  //}
  //
  //$scope.onLayoutSelect = function(item,model){
  //    console.log('onLayoutSelect ',item,model,$scope.layouts)
  //    $scope.selectedLayouts.push(item);
  //    showSelectedLayouts()
  //}
  //$scope.onLayoutRemoval = function(item,model){
  //    console.log('onLayoutRemoval ',item,model,$scope.layouts)
  //    _.remove($scope.selectedLayouts,function(eachSelectedLayout){
  //        return eachSelectedLayout === item;
  //    });
  //    showSelectedLayouts()
  //}


  //function addLayout (layout) {
  //    $http.post('/layout/config',layout).then(function (response) {
  //        $scope.getLayouts()
  //    })
  //}
  //
  //function updateLayout(layout) {
  //    $http.put('/layout/config',layout).then(function (response) {
  //        $scope.getLayouts()
  //    })
  //}
  //
    function showModal (layoutInfo, type) {
      console.log(layoutInfo)
        ModalService.showModal({
            templateUrl: 'js/templates/listPopUp.html',
            controller: 'listPopupController',
            inputs: {
                options: {
                    body:{
                        bodyContent :layoutInfo,
                        layouts: $scope.layouts
                    },
                    header: type === 'Add' ? 'Add new Layout' : 'Update '+ layoutInfo.layout,
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
                if (response === 'Add') {
                    addLayout(layoutInfo)
                } else if (response === 'Update') {
                    updateLayout(layoutInfo)
                }
            });
        });
    }
    $scope.updateTemplate = function (layoutInfo) {
        showModal(layoutInfo.layoutRule, 'Update')
    }

    $scope.addTemplate = function () {
          var layout = angular.copy(newLayout);
          showModal(layout, 'Add')
      }


   $scope.getLayouts = function () {
      $http.get('/layout/template/all').then(function (response) {
          $scope.allLayouts =  response.data;
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