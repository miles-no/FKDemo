const templateAttributeController = ($scope,$http,ModalService) => {

  var newRule = {
    name: '',
    type: '',
    fieldMapping: ''
  }
  $scope.ruleList = []
  $scope.selectedRule = []
  $scope.tableRule = []
  $scope.alerts =[];
  $scope.pageSize = ''

  $scope.getRules = () => {
    $http.get('/invoicemanager/api/layout/attribute').success((response) => {
      $scope.tableRule = $scope.ruleData = response
      $scope.ruleList = []
      angular.forEach($scope.ruleData,(item) => {
        $scope.ruleList.push(item.name)
      })
      $scope.ruleList = _.uniqBy($scope.ruleList,(e) => {
        return e
      })
    })
  }

  let showSelectedRules = () => {
    $scope.tableRule = []
    if($scope.selectedRule.length > 0){
      angular.forEach($scope.ruleData,(item) => {
        angular.forEach($scope.selectedRule,(selectedItems) => {
          if(selectedItems === item.name){
            $scope.tableRule.push(item)
          }
        })
      })
    }else{
      $scope.tableRule = $scope.ruleData
    }
  }

  $scope.closeAlert = (index) => {
    $scope.alerts.splice(index,1)
  }


  $scope.onRuleSelect = (item,modal) => {
    $scope.selectedRule.push(item)
    showSelectedRules()
  }
  $scope.onRuleRemoval = (item,modal) => {
    _.remove($scope.selectedRule,(eachSelectedRule) => {
      return eachSelectedRule === item;
    });
    showSelectedRules()
  }

  let addRule = (rule) => {
    $http.post('/invoicemanager/api/layout/attribute',rule).then((response) => {
      if(response.status === 200){
        $scope.alerts.push({ type: 'success', msg: 'Record added successfully' })
        $scope.getRules()
      }
      else{
        $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
      }
    },(err) => {
      $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
    })
  }

  let updateRule = (rule) => {
    $http.put('/invoicemanager/api/layout/attribute/'+rule.id,rule).then((response) => {
      if(response.status === 200){
        $scope.alerts.push({ type: 'success', msg: 'Record updated successfully' })
        $scope.getRules()
      }
      else{
        $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
      }
    },(err) => {
      $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
    })
  }

  $scope.deleteRule = (rule) => {
    console.log(rule);
    ModalService.showModal({
      templateUrl: 'js/modals/confirmDelete.html',
      controller:'popupController',
      inputs:{
        options:{
          body:{
            bodyContent: 'Please confirm to delete '+rule.name,
            brand: {brand: rule.name}
          },
          header: "Delete Rule",
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
          $http.delete('/invoicemanager/api/layout/attribute/'+rule.id).then((response) => {
            if(response.status === 200){
              $scope.alerts.push({ type: 'success', msg: 'Record deleted successfully' })
              $scope.getRules()
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


  let showModal = (ruleInfo, type) => {
    ModalService.showModal({
      templateUrl: 'templates/template-attributes/templateAttributesPopup.html',
      controller: 'templateAttributePopupController',
      inputs: {
        options: {
          body:{
            bodyContent :ruleInfo
          },
          header: type === 'Add' ? 'Add Rule' : 'Update '+ ruleInfo.name,
          conFirmBtnText : [
            {name: 'cancel'},
            {name: type }
          ],
          type: type,
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
          addRule(ruleInfo)
        } else if (response === 'Update') {
          updateRule(ruleInfo)
        }
      });
    });
  }
  $scope.addRule = () => {
    var rule = angular.copy(newRule);
    showModal(rule, 'Add')
  }
  $scope.updateRule = (ruleInfo) => {
    var ruleData = angular.copy(ruleInfo)
    showModal(ruleData, 'Update')
  }
};

export {templateAttributeController};
