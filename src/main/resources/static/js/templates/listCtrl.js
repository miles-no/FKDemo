app.controller('listCtrl',function($scope,ModalService,$http){

  //$scope.layouts =[];
  //var newLayout = {
  //    brand: '',
  //    legalPartClass: '',
  //    accountCategory: '',
  //    distributionMethod: '',
  //    creditLimit: ''
  //}
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
  $scope.getLayouts = function () {
    $http.get('/layout/template/all').then(function (response) {
      $scope.allLayouts =  $scope.layouts = response.data;
    })
  }
  //
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
  //$scope.deleteLayouts = function (layout, $event) {
  //    $event.stopPropagation();
  //    ModalService.showModal({
  //        templateUrl: 'js/modals/confirmDelete.html',
  //        controller:'listPopupController',
  //        inputs:{
  //            options:{
  //                body:{
  //                    bodyContent: 'Please confirm to delete ',
  //                    layout: layout
  //                },
  //                header: "Delete Layout",
  //                conFirmBtnText : [
  //                    {name: 'cancel'},
  //                    {name: "Delete" }
  //                ],
  //                classes: {
  //                    modalBody: '',
  //                    body: 'manage-brand'
  //                }
  //            }
  //        }
  //    }).then(function(modal){
  //        modal.element.modal();
  //        modal.close.then(function(result){
  //            if(result=='Delete'){
  //                $http.delete('/layout/config', layout.id).then(function (response) {
  //                    $scope.getLayouts()
  //                })
  //            }
  //        })
  //    })
  //}
  //
  //function showModal (layoutInfo, type) {
  //    ModalService.showModal({
  //        templateUrl: 'js/templates/listPopUp.html',
  //        controller: 'listPopupController',
  //        inputs: {
  //            options: {
  //                body:{
  //                    bodyContent :layoutInfo,
  //                    layouts: $scope.layouts
  //                },
  //                header: type === 'Add' ? 'Add new layout' : 'Update '+ layoutInfo.layout,
  //                conFirmBtnText : [
  //                    {name: 'cancel'},
  //                    {name: type }
  //                ],
  //                classes: {
  //                    modalBody: '',
  //                    body: 'manage-brand'
  //                }
  //            }
  //        }
  //    }).then(function(modal){
  //        modal.element.modal();
  //        modal.close.then(function(response){
  //            if (response === 'Add') {
  //                addLayout(layoutInfo)
  //            } else if (response === 'Update') {
  //                updateLayout(layoutInfo)
  //            }
  //        });
  //    });
  //}
  //$scope.addLayout = function () {
  //    var layout = angular.copy(newLayout);
  //    showModal(layout, 'Add')
  //}
  //$scope.updateLayout = function (layoutInfo) {
  //    showModal(layoutInfo, 'Update')
  //}



   $scope.getLayouts = function () {
      $http.get('/layout/template/all').then(function (response) {
          $scope.allLayouts =  $scope.layouts = response.data;
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