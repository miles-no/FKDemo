const ManageBrandsController = ($scope, $q, $http, ModalService) => {
  $scope.brands =[];
  $scope.alerts =[];
  $scope.allCounts = [10,25,50,100]
  var newBrand = {
    brand: '',
    agreementNumber: '',
    serviceLevel: '',
    prefixKID: '',
    kontonummer: '',
    useEABarcode: '0'
  }

  $scope.pageSize = 10
  $scope.selectedBrands = []
  $scope.tableBrands = []

  let showSelectedBrands = () => {
    $scope.tableBrands = []
    if($scope.selectedBrands.length > 0){
      angular.forEach($scope.brands,(item) => {
        angular.forEach($scope.selectedBrands,(selectedItem) => {
          if (selectedItem === item.brand){
            $scope.tableBrands.push(item)
          }
        })
        $scope.tableBrands = _.uniqBy($scope.tableBrands,(e) => {
          return e
        })
      })
    }
    else{
      $scope.tableBrands = $scope.brands
    }
  }

  $scope.isAuthorizedFor = (func) => {
    return AuthorizationService.hasAccess(func);
  }

  $scope.syncCall = () => {
    $http.get('/invoicemanager/api/brand/config/brand').then((response) => {
      $scope.dropDownData = response.data
    })
  }

  $scope.onCountSelect = (item, model) => {
    $scope.pageSize = item
  }
  $scope.closeAlert = (index) => {
      $scope.alerts.splice(index,1)
  }

  $scope.onBrandSelect = (item) => {
    $scope.selectedBrands.push(item);
      showSelectedBrands()
  }
  $scope.onBrandRemoval = (item) => {
    _.remove($scope.selectedBrands,(eachSelectedBrand) => {
      return eachSelectedBrand === item;
    });
    showSelectedBrands()
  }
  $scope.getBrands = () => {

    $http.get('/invoicemanager/api/brand/config').then((response) => {
      $scope.tableBrands= $scope.brands = response.data.Brand;
    })
  }

  let addBrand = (brand) => {
    $http.post('/invoicemanager/api/brand/config',brand).then((response) => {
      if(response.status === 200){
        $scope.getBrands()
        $scope.alerts.push({ type: 'success', msg: 'Record added successfully' })
      }
      else{
        $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
      }
    },(err) => {
      $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
    })
  }

  let updateBrand = (brand) => {
    $http.put('/invoicemanager/api/brand/config',brand).then((response) => {
      if(response.status === 200){
        $scope.getBrands()
        $scope.alerts.push({ type: 'success', msg: 'Record updated successfully' })
      }
      else{
        $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
      }
    },function(err){
      $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
    })
  }

  $scope.deleteBrands =  (brand) => {
    ModalService.showModal({
      templateUrl: 'js/modals/confirmDelete.html',
      controller:'popupController',
      inputs:{
          options:{
            body:{
              bodyContent: 'Please confirm to delete ' + brand.brand,
              brand: brand
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
      modal.close.then((result) => {
        if(result=='Delete'){
          $http.delete('/invoicemanager/api/brand/config/'+brand.id).then((response) => {
            if (response.status === 200){
              $scope.alerts.push({ type: 'success', msg: 'Record deleted successfully' })
              $scope.getBrands();
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

   let showModal = (brandInfo, type) => {
    ModalService.showModal({
      templateUrl: 'templates/brands/brandsPopup.html',
      controller: 'ManageBrandsPopupController',
      inputs: {
        options: {
          body:{
            bodyContent :brandInfo
          },
          header: type === 'Add' ? 'Add new brand' : 'Update '+ brandInfo.brand,
          conFirmBtnText : [
            {name: 'cancel'},
            {name: type }
          ],
          type : type,
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
          addBrand(brandInfo)
        } else if (response === 'Update') {
          updateBrand(brandInfo)
        }
      });
    });
  }
  $scope.addBrand =  () => {
    var brand = angular.copy(newBrand);
    showModal(brand, 'Add')
  }
  $scope.updateBrand = (brandInfo) => {
    var brandData = angular.copy(brandInfo)
    showModal(brandData, 'Update')
  }
};
export {ManageBrandsController};
