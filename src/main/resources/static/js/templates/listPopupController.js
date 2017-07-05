app.controller('listPopupController',function($scope,options,close, $http){
    $scope.options = options;
    $scope.items = options.body.bodyContent
    $scope.items = {
        template : {
            name: ''
        }
    }
    var ruleObj = {name: '', type: '', fileMapping: ''}
    $scope.rulesList = [angular.copy(ruleObj), angular.copy(ruleObj)]
    $scope.hideUpload = true
    $scope.addRule = function () {
        $scope.rulesList.push(angular.copy(ruleObj))
    }

    $scope.removeRule = function (item, index) {
        $scope.rulesList.splice(index,1)
    }
    $scope.dismissModal = function(result) {
        close(result,200);
    }
    /*if ($scope.items.brand.length > 0) {
     $scope.disable = true
     }*/
    $scope.selectedBrands = []
    $scope.selectedTemplate=[]
    var getLayouts = function () {

        /*$http.get('/layout/config').then(function (response) {
         $scope.allLayouts =  $scope.layouts = response.data;;
         })*/
        $scope.allLayouts = $scope.layouts = [{brand: 'KHSD'},{brand: 'ASD'},{brand: 'QWE'}]
        console.log('$scope.allLayouts',$scope.allLayouts)
    }

    getLayouts()
    $scope.onBrandSelect = function(item,model){
        $scope.selectedBrands = item;
    }

    $scope.onLayoutSelect = function(item,model){
        $scope.selectedTemplate = angular.copy(item);
    }

    $scope.clearTemplate = function () {
        $scope.selectedTemplate = [];
        $scope.layouts = $scope.allLayouts
        $scope.items.template ={ name : ''}
    }
});
