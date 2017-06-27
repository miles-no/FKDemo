app.controller('StateConfigController',function($scope, $q, $http,ModalService){
  var newState = {
    name: '',
    value: ''
  }
  
  $scope.getStatesConfig = function () {
    $http.get('/config').then(function (response) {
      $scope.stateConfigs = response.data;
    })
  }

  function addStateConfig (state) {
    $http.post('/config',state).then(function (response) {
      $scope.getStatesConfig()
    })
  }

  function updateStateConfig(state) {
    $http.put('/config',state).then(function (response) {
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
            brand: state
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
          $http.delete('/state/config/'+state.id).then(function (response) {
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