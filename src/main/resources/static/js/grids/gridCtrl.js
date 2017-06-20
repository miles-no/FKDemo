app.controller('GridsController',function($scope, $q, $http,ModalService){
  $scope.brands =[];
  var newGrid = {
    brand: '',
    email: '',
    phone: ''
  }
  $scope.selectedGrids = []

  function showSelectedGrids () {
    $scope.allGrids = []
    if ($scope.selectedGrids.length > 0) {
      angular.forEach($scope.grids, function (gridItem) {
        angular.forEach($scope.selectedGrids, function (selectedGrid) {
          if(gridItem.brand === selectedGrid.brand) {
            $scope.allGrids.push(gridItem)
          }
        })
      })
    } else {
      $scope.allGrids =  $scope.grids;
    }
  }

  $scope.onGridSelect = function(item,model){
    console.log('onGridSelect ',item,model,$scope.brands)
    $scope.selectedGrids.push(item);
    showSelectedGrids()
  }
  $scope.onGridRemoval = function(item,model){
    console.log('onGridRemoval ',item,model,$scope.grids)
    _.remove($scope.selectedGrids,function(eachSelectedGrid){
      return eachSelectedGrid === item;
    });
    showSelectedGrids()
  }
  $scope.getGrids = function () {
    $http.get('/grid/config').then(function (response) {
      $scope.allGrids =  $scope.grids = response.data;;
    })
  }

  function addGrid (grid) {
    $http.post('/grid/config',grid).then(function () {
      $scope.getGrids()
    })
  }

  function updateGrid(grid) {
    $http.put('/grid/config',grid).then(function () {
      $scope.getGrids()
    })
  }

  $scope.deleteGrid = function (grid, $event) {
    $event.stopPropagation();
    $http.delete('/grid/config', grid.id).then(function () {
      $scope.getGrids()
    })
  }

  function showModal (gridInfo, type) {
    ModalService.showModal({
      templateUrl: 'js/grids/manage-grid.html',
      controller: 'popupController',
      inputs: {
        options: {
          body:{
            bodyContent :gridInfo
          },
          header: type === 'Add' ? 'Add new brand' : 'Update '+ gridInfo.brand,
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
          addGrid(gridInfo)
        } else if (response === 'Update') {
          updateGrid(gridInfo)
        }
      });
    });
  }
  $scope.addGrid = function () {
    var grid = angular.copy(newGrid);
    showModal(grid, 'Add')
  }
  $scope.updateGrid = function (gridInfo) {
    showModal(gridInfo, 'Update')
  }
});