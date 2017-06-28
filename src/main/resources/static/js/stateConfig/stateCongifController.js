app.controller('StateConfigController',function($scope, $q, $http,ModalService){
  var newState = {
    name: '',
    value: ''
  }

  $scope.selectedGrids = []

  function showSelectedGrids () {
    $scope.allGrids = []
    if ($scope.selectedGrids.length > 0) {
      angular.forEach($scope.grids, function (key) {
        angular.forEach($scope.selectedGrids, function (selectedGrid) {
          if(gridItem.brand === selectedGrid) {
            $scope.allGrids.push(key)
          }
        })
      })
    } else {
      $scope.allGrids =  $scope.stateConfigs;
    }
  }

  $scope.getStatesConfig = function () {
    $http.get('/config').then(function (response) {
      $scope.alldata = $scope.stateConfigs = response.data.config;
    })
  }

  $scope.onGridSelect = function(item,model){
    $scope.selectedGrids.push(item);
    showSelectedGrids()
  }

  $scope.onGridRemoval = function(item,model){
    _.remove($scope.selectedGrids,function(eachSelectedGrid){
      return eachSelectedGrid === item;
    });
    showSelectedGrids()
  }

  function addStateConfig (state) {
    $http.post('/config',state).then(function (response) {
      $scope.getStatesConfig()
    })
  }

  function updateStateConfig(state) {
    var data = angular.copy(state)
    console.log(data)
    $http.put('/config',data).then(function (response) {
      $scope.getStatesConfig()
    })
  }

  $scope.deleteStateConfig = function (state, $event) {
    $event.stopPropagation();
    ModalService.showModal({
      templateUrl: 'js/modals/confirmDelete.html',
      controller:'popupController',
      inputs:{
        options:{
          body:{
            bodyContent: 'Please confirm to delete ',
            brand: {brand: state.name}
          },
          header: "Delete Brand",
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
      modal.close.then(function(result){
        if(result=='Delete'){
          $http.delete('/config/'+state.name).then(function (response) {
            $scope.getStatesConfig();
          })
        }
      })
    })
  }



  function showModal (stateInfo, type) {
    ModalService.showModal({
      templateUrl: 'js/stateConfig/stateConfigPopup.html',
      controller: 'popupController',
      inputs: {
        options: {
          body:{
            bodyContent :stateInfo
          },
          header: type === 'Add' ? 'Add new State' : 'Update '+ stateInfo.name,
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
          addStateConfig(stateInfo)
        } else if (response === 'Update') {
          updateStateConfig(stateInfo)
        }
      });
    });
  }
  $scope.addStateConfig = function () {
    var stateInfo = angular.copy(newState);
    showModal(stateInfo, 'Add')
  }
  $scope.updateStateConfig = function (stateInfo) {
   var updateStateInfo = angular.copy(stateInfo);
    showModal(updateStateInfo, 'Update')
  }
});