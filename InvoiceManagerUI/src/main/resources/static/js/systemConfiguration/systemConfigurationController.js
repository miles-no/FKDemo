const StateConfigController = ($scope, $q, $http,ModalService) => {
  var newState = {
    name: '',
    value: ''
  }

  $scope.alerts =[];
  $scope.selectedKey = ''

  $scope.onPropSelect = (item) => {
    $scope.alldata = []
    angular.forEach($scope.stateConfigs,function(key){
      if(key.name === item){
        $scope.selectedKey = item
        $scope.alldata.push(key)
      }
    })
  }

  $scope.closeAlert = (index) => {
    $scope.alerts.splice(index,1)
  }


  $scope.$watch('alldata',(newVal,oldVal) => {
    if (newVal !== oldVal && !newVal.length){
        $scope.alldata = $scope.stateConfigs
    }
  })

  $scope.configName = []
  $scope.getStatesConfig =  ()  => {
    $http.get('/invoicemanager/api/config').then(function (response) {
      $scope.stateConfigs = response.data.config;
      $scope.configName = []
      $scope.alldata = angular.copy($scope.stateConfigs)
      angular.forEach($scope.alldata,(key) => {
       $scope.configName.push(key.name)
      })
      $scope.configName = _.uniqBy($scope.configName,(e) => {
        return e
      })
    })
  }

  let addStateConfig = (state) => {
    let queryParams = {
      key : state.name,
      value : state.value
    }
    $http({url : '/invoicemanager/api/config',method: 'POST',params:queryParams} ).then((response) => {
      if(response.status === 200){
        $scope.getStatesConfig()
        $scope.alerts.push({ type: 'success', msg: 'Record added successfully' })
      }
      else{
        $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
      }
    },(err) => {
      $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
    })
  }

  let updateStateConfig = (state) => {
    var data = angular.copy(state)
    let queryParams = {
      value : data.value
    }
    $http({url:'/invoicemanager/api/config/'+data.name,method: 'PUT', params:queryParams}).then((response) => {
      if(response.status === 200){
        $scope.getStatesConfig()
        $scope.alerts.push({ type: 'success', msg: 'Record updated successfully' })
      }
      else{
        $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
      }
    },(err) => {
      $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
    })
  }

  $scope.deleteStateConfig = (state)  => {
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
    }).then((modal) => {
      modal.element.modal();
      modal.close.then((result) => {
        if(result=='Delete'){
          $http.delete('/invoicemanager/api/config/'+state.name).then((response) => {
            if(response.status === 200){
              $scope.getStatesConfig()
              $scope.alerts.push({ type: 'success', msg: 'Record deleted successfully' })
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

  let showModal = (stateInfo, type) => {
    ModalService.showModal({
      templateUrl: 'templates/system-configurations/systemConfigurationsPopup.html',
      controller: 'systemConfigurationPopupController',
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
    }).then((modal) => {
      modal.element.modal();
      modal.close.then((response) => {
        if (response === 'Add') {
          addStateConfig(stateInfo)
        } else if (response === 'Update') {
          updateStateConfig(stateInfo)
        }
      });
    });
  }
  $scope.addStateConfig =  () => {
    var stateInfo = angular.copy(newState);
    showModal(stateInfo, 'Add')
  }
  $scope.updateStateConfig =  (stateInfo) => {
   var updateStateInfo = angular.copy(stateInfo);
    showModal(updateStateInfo, 'Update')
  }

  $scope.init = () => {
    $scope.pageSize = '';
    $scope.getStatesConfig();
  }

};

export {StateConfigController}
