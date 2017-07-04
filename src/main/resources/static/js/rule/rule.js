app.controller('ruleController',function($scope,$http,ModalService){


  var newRule = {
    name: '',
    type: '',
    fieldMapping: ''
  }
  $scope.getRules = function (){
    $http.get('/layout/attribute').success(function(response){
      $scope.ruleData = response
    })
  }

  function addRule (rule) {
    $http.post('/layout/attribute',rule).then(function () {
      $scope.getRules()
    })
  }

  function updateRule(rule) {
    $http.put('/layout/attribute',rule).then(function () {
      $scope.getRules()
    })
  }

  $scope.deleteRule = function (rule, $event) {
    $event.stopPropagation();
    console.log(rule);
    ModalService.showModal({
      templateUrl: 'js/modals/confirmDelete.html',
      controller:'popupController',
      inputs:{
        options:{
          body:{
            bodyContent: 'Please confirm to delete ',
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
    }).then(function(modal){
      modal.element.modal();
      modal.close.then(function(result){
        if(result=='Delete'){
          $http.delete('/layout/attribute/'+rule.id).then(function () {
            $scope.getRules();
          })
        }
      })
    })
  }


  function showModal (ruleInfo, type) {
    ModalService.showModal({
      templateUrl: 'js/rule/rulePopUp.html',
      controller: 'ManageRulePopupController',
      inputs: {
        options: {
          body:{
            bodyContent :ruleInfo
          },
          header: type === 'Add' ? 'Add new brand' : 'Update '+ ruleInfo.name,
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
    }).then(function(modal){
      modal.element.modal();
      modal.close.then(function(response){
        if (response === 'Add') {
          addRule(ruleInfo)
        } else if (response === 'Update') {
          updateRule(ruleInfo)
        }
      });
    });
  }
  $scope.addRule = function () {
    var rule = angular.copy(newRule);
    showModal(rule, 'Add')
  }
  $scope.updateRule = function (ruleInfo) {
    var ruleData = angular.copy(ruleInfo)
    showModal(ruleData, 'Update')
  }
});

