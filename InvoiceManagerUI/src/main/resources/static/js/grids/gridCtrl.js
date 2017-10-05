const GridsController = ($scope, $q, $http,ModalService) => {
  $scope.brands =[];
  var newGrid = {
    brand: '',
    email: '',
    phone: ''
  }
  $scope.selectedGrids = []
  $scope.brands = []
  $scope.alerts =[];
  $scope.pageSize = 10
  $scope.allCounts = [10,25,50,100]

  let showSelectedGrids = () => {
    $scope.allGrids = []
    if ($scope.selectedGrids.length > 0) {
      angular.forEach($scope.grids, (gridItem) => {
        angular.forEach($scope.selectedGrids,(selectedGrid) => {
          if(gridItem.gridName === selectedGrid) {
            $scope.allGrids.push(gridItem)
          }
        })
        $scope.allGrids = _.uniqBy($scope.allGrids,(e) => {
          return e
        })
      })
    } else {
      $scope.allGrids =  $scope.grids;
    }
  }
  $scope.onCountSelect = (item, model) => {
    $scope.pageSize = item
  }
  $scope.syncCall =() => {
    $http.get('/invoicemanager/api/grid/config/brand').then((response) => {
      $scope.brands = response.data
    })
  }

  $scope.closeAlert = (index) => {
    $scope.alerts.splice(index,1)
  }


  $scope.onGridSelect =(item,model) => {
    console.log('onGridSelect ',item,model,$scope.brands)
    $scope.selectedGrids.push(item);
    showSelectedGrids()
  }
  $scope.onGridRemoval =(item,model) => {
    _.remove($scope.selectedGrids,(eachSelectedGrid) => {
      return eachSelectedGrid === item;
    });
    showSelectedGrids()
  }
  $scope.getGrids =() => {
    $http.get('/invoicemanager/api/grid/config').then((response) => {
      $scope.allGrids =  $scope.grids = response.data.Grid;
    })
  }

  let  addGrid = (grid) => {
    $http.post('/invoicemanager/api/grid/config',grid).then((response) => {
      if(response.status === 200){
        $scope.alerts.push({ type: 'success', msg: 'Record added successfully' })
        $scope.getGrids()
      }
      else{
        $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
      }
    },(err) => {
      $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
    })
  }

  let updateGrid = (grid) => {
    $http.put('/invoicemanager/api/grid/config',grid).then((response) => {
      if(response.status === 200){
        $scope.getGrids()
        $scope.alerts.push({ type: 'success', msg: 'Record updated successfully' })
      }
      else{
        $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
      }
    },(err) => {
      $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
    })
  }

  $scope.deleteGrid =  (grid) => {
    ModalService.showModal({
      templateUrl: 'js/modals/confirmDelete.html',
      controller:'popupController',
      inputs:{
        options:{
          body:{
            bodyContent: 'Please confirm to delete '+ grid.gridName,
            brand: grid
          },
          header: "Delete Grid",
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
          $http.delete('/invoicemanager/api/grid/config/'+grid.id).then(function (response) {
            if (response.status === 200){
              $scope.getGrids();
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


  let showModal = (gridInfo, type) =>  {
    ModalService.showModal({
      templateUrl: 'templates/grids/gridsPopup.html',
      controller: 'gridPopupController',
      inputs: {
        options: {
          body:{
            bodyContent :gridInfo
          },
          header: type === 'Add' ? 'Add new Grid' : 'Update '+ gridInfo.gridName,
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
          addGrid(gridInfo)
        } else if (response === 'Update') {
          updateGrid(gridInfo)
        }
      });
    });
  }
  $scope.addGrid = () => {
    var grid = angular.copy(newGrid);
    showModal(grid, 'Add')
  }
  $scope.updateGrid =  (gridInfo) => {
    var gridData = angular.copy(gridInfo)
    showModal(gridData, 'Update')
  }
};

export {GridsController};
