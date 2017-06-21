var app = angular.module('invoiceManagerApp');

app.controller('ManageBrandsController',function($scope, $q, $http,ModalService){
  $scope.brands =[];
  var newBrand = {
    brand: '',
    agreementNumber: '',
    serviceLevel: '',
    prefixKID: '',
    kontonummer: '',
    useEABarcode: 0
  }
  $scope.selectedBrands = []
  $scope.allBrands = []
  function showSelectedBrands () {
    $scope.allBrands = []
    if ($scope.selectedBrands.length > 0) {
      angular.forEach($scope.brands, function (brandItem) {
        angular.forEach($scope.selectedBrands, function (selectedBrand) {
          if(brandItem.brand === selectedBrand.brand) {
            $scope.allBrands.push(brandItem)
          }
        })
      })
    } else {
      $scope.allBrands =  $scope.brands;
    }
  }

  $scope.onBrandSelect = function(item,model){
    console.log('onBrandSelect ',item,model,$scope.brands)
    $scope.selectedBrands.push(item);
    showSelectedBrands()
  }
  $scope.onBrandRemoval = function(item,model){
    console.log('onBrandRemoval ',item,model,$scope.brands)
    _.remove($scope.selectedBrands,function(eachSelectedBrand){
      return eachSelectedBrand === item;
    });
    showSelectedBrands()
  }
  $scope.getBrands = function () {
    $http.get('/brand/config').then(function (response) {
      $scope.allBrands =  $scope.brands = response.data;
    })
  }

  function addBrand (brand) {
    $http.post('/brand/config',brand).then(function () {
      $scope.getBrands()
    })
  }

  function updateBrand(brand) {
    $http.put('/brand/config',brand).then(function () {
    })
  }

  $scope.deleteBrands = function (brand, $event) {
    $event.stopPropagation();
    ModalService.showModal({
      templateUrl: 'js/modals/confirmDelete.html',
      controller:'popupController',
      inputs:{
          options:{
            body:{
              bodyContent: 'Please confirm to delete ',
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
          $http.delete('/brand/config/'+brand.id).then(function () {
            $scope.getBrands();
          })
        }
      })
    })
  }

  function showModal (brandInfo, type) {
    ModalService.showModal({
      templateUrl: 'js/brands/manage-brands.html',
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
    showModal(brandInfo, 'Update')
  }


});
