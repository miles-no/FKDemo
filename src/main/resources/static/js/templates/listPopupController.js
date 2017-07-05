app.controller('listPopupController',function($scope,options,close, $http){
    $scope.options = options;
    $scope.items = options.body.bodyContent
    $scope.allowRules = false;
    $scope.items = {
        template : {
            name: ''
        }
    }
    var ruleObj = {name: '', type: '', fileMapping: ''}
    //$scope.rulesList = [angular.copy(ruleObj), angular.copy(ruleObj)]
    $scope.hideUpload = true
    let getLayoutRules = function(){
        $http.get('layout/attribute').then(function (response) {
             $scope.rulesList = response.data;
        },function error(error){
             $scope.rulesList = [];
        });
    }
    $scope.addRule = function () {
        $scope.rulesList.push(angular.copy(ruleObj))
    }

    $scope.removeRule = function (item, index) {
        $scope.rulesList.splice(index,1)
    }
    let prepareModel = function(){

    }
    $scope.dismissModal = function(result) {
        console.log('in dismissModal ',$scope);
        prepareModel()
        close(result,200);
    }
    $scope.getAllOperationForAType = function(type){
        switch(type){
            case 'STRING':
                return ['EQUALS','NOT EQUALS'] ;
            case 'INTEGER':
                return ['LESS THAN','LESS THAN EQUAL TO','GREATER THAN','GREATER THAN EQUAL TO','EQUAL TO','NO EQUAL TO'] ;
            case 'FLOAT':
                return ['LESS THAN','LESS THAN EQUAL TO','GREATER THAN','GREATER THAN EQUAL TO','EQUAL TO','NO EQUAL TO'] ;
            default :
                return ['EQUALS','NOT EQUALS'] ;
        }


    }
    /*if ($scope.items.brand.length > 0) {
     $scope.disable = true
     }*/
    $scope.selectedBrands = '';
    $scope.selectedTemplate={};
    let getBrands = function (){
        
        $http.get('/brand/config/brand').then(function (response) {
             $scope.allBrands = response.data;
             $scope.allBrands.push('All');
        },function error(error){
             $scope.allBrands = [];
        });
    }
    let getLayouts = function () {

        $http.get('/layout/list').then(function (response) {
             $scope.allLayouts = response.data;;
        },function error(error){
             $scope.allLayouts = {};
        })
       // $scope.allLayouts = $scope.layouts = [{brand: 'KHSD'},{brand: 'ASD'},{brand: 'QWE'}]
        console.log('$scope.allLayouts',$scope.allLayouts)
    }

    
    $scope.onBrandSelect = function(item,model){
        $scope.selectedBrands = item;
    }

    $scope.onLayoutSelect = function(item,model){
        $scope.selectedTemplate.selected = item;
    }

    $scope.clearTemplate = function () {
        //$scope.selectedTemplate = [];
        $scope.items.template ={}
    }
    $scope.onTemplateChange = function(){
        console.log('Came here on Template change',$scope.selectedTemplate)
        $scope.selectedTemplate ={};
    };
    $scope.$watch('items.template',function(){
        $scope.onTemplateChange();
    });
    $scope.moveToRules = function(){
        return false;
    }
    let init = function(){
        getBrands();
        getLayouts();
        getLayoutRules();
    }
    init();
});
