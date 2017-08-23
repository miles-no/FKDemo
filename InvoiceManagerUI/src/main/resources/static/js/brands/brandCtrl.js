var app = angular.module('invoiceManagerApp');

app.controller('ManageBrandsController',function($scope, $q, $http,ModalService){
  $scope.brands =[];
  $scope.alerts =[];

  var newBrand = {
    brand: '',
    agreementNumber: '',
    serviceLevel: '',
    prefixKID: '',
    kontonummer: '',
    useEABarcode: '0'
  }

  $scope.selectedBrands = []
  $scope.tableBrands = []

  function showSelectedBrands () {
    $scope.tableBrands = []
    if($scope.selectedBrands.length > 0){
      angular.forEach($scope.brands, function(item){
        angular.forEach($scope.selectedBrands, function(selectedItem){
          if (selectedItem === item.brand){
            $scope.tableBrands.push(item)
          }
        })
        $scope.tableBrands = _.uniqBy($scope.tableBrands,function(e){
          return e
        })
      })
    }
    else{
      $scope.tableBrands = $scope.brands
    }
  }
  $scope.syncCall = function(){
    $http.get('brand/config/brand').then(function(response){
      $scope.dropDownData = response.data
    })
  }

  $scope.closeAlert = function(index){
      $scope.alerts.splice(index,1)
  }

  $scope.onBrandSelect = function(item){
    $scope.selectedBrands.push(item);
      showSelectedBrands()
  }
  $scope.onBrandRemoval = function(item){
    _.remove($scope.selectedBrands,function(eachSelectedBrand){
      return eachSelectedBrand === item;
    });
    showSelectedBrands()
  }
  $scope.getBrands = function () {

    $http.get('/brand/config').then(function (response) {
      $scope.tableBrands= $scope.brands = response.data.Brand;
    })
  }

  function addBrand (brand) {
    $http.post('/brand/config',brand).then(function (response) {
      if(response.status === 200){
        $scope.getBrands()
        $scope.alerts.push({ type: 'success', msg: 'Record added successfully' })
      }
      else{
        $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
      }
    },function(err){
      $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
    })
  }

  function updateBrand(brand) {
    $http.put('/brand/config',brand).then(function (response) {
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

  $scope.deleteBrands = function (brand) {
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
      modal.close.then(function(result){
        if(result=='Delete'){
          $http.delete('/brand/config/'+brand.id).then(function (response) {
            if (response.status === 200){
              $scope.alerts.push({ type: 'success', msg: 'Record deleted successfully' })
              $scope.getBrands();
            }
            else{
              $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
            }
          },function(err){
            $scope.alerts.push({ type: 'danger', msg: 'Some unknown error occurred ! please try again' })
          })
        }
      })
    })
  }

  function showModal (brandInfo, type) {
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
    }).then(function(modal){
      modal.element.modal();
      modal.close.then(function(response){
        if (response === 'Add') {
          addBrand(brandInfo)
        } else if (response === 'Update') {
          updateBrand(brandInfo)
        }
      });
    });
  }
  $scope.addBrand = function () {
    var brand = angular.copy(newBrand);
    showModal(brand, 'Add')
  }
  $scope.updateBrand = function (brandInfo) {
    var brandData = angular.copy(brandInfo)
    showModal(brandData, 'Update')
  }
});
