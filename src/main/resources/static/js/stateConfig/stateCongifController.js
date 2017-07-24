app.controller('StateConfigController',function($scope, $q, $http,ModalService){
  var newState = {
    name: '',
    value: ''
  }

  $scope.selectedKey = ''

  $scope.showSelectedKey = function() {
    $scope.alldata = []
    if ($scope.selectedKey !== '') {
      angular.forEach($scope.stateConfigs, function (key) {
        if (key.name === $scope.selectedKey){
          $scope.alldata.push(key)
        }
      })
    } else {
      $scope.alldata =  $scope.stateConfigs
    }
  }

  $('.glyphicon-remove').addClass('fa fa-times').removeClass('glyphicon glyphicon-remove')


  $scope.clearUiSelect = function($event){
    $event.stopPropagation();
    $scope.selectedKey.selected = undefined
    $scope.alldata =  $scope.stateConfigs
  }

  $scope.onPropSelect = function(item){
    $scope.alldata = []
    angular.forEach($scope.stateConfigs,function(key){
      if(key.name === item){
        $scope.selectedKey = item
        $scope.alldata.push(key)
      }
    })
  }
  $scope.configName = []
  $scope.getStatesConfig = function () {
    $http.get('/config').then(function (response) {
      $scope.stateConfigs = response.data.config;
      $scope.alldata = angular.copy($scope.stateConfigs)
     angular.forEach($scope.alldata,function(key){
       $scope.configName.push(key.name)
     })
    })
  }

  $scope.onGridSelect = function(item,model){
    $scope.selectedKey = ''
    $scope.selectedKey = item.name;
    showSelectedKey()
  }
  
  function addStateConfig (state) {
    let queryParams = {
      key : state.name,
      value : state.value
    }
    $http({url : '/config',method: 'POST',params:queryParams} ).then(function (response) {
      $scope.getStatesConfig()
    })
  }

  function updateStateConfig(state) {
    var data = angular.copy(state)
    console.log(data);
    let queryParams = {
      value : data.value
    }
    $http({url:'/config/'+data.name,method: 'PUT', params:queryParams}).then(function (response) {
      $scope.getStatesConfig()
    })
  }

  $scope.deleteStateConfig = function (state) {
    ModalService.showModal({
      templateUrl: 'js/modals/confirmDelete.html',
      controller:'popupController',
      inputs:{
        options:{
          body:{
            bodyContent: 'Please confirm to delete '+state.name,
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