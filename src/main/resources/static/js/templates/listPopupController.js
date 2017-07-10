app.controller('listPopupController',function($scope,options,close, $http,_){
    $scope.options = options;
    $scope.items = options.body.bodyContent;
    $scope.templateInfo = options.body.bodyContent;
    $scope.allowRules = false;
    $scope.template = {
        name :'',
        desc : '',
        file : null
    };
    $scope.selectedBrand = '';
    $scope.selectedTemplate={};
    let allPossibleRules = {}
    var ruleObj = {name: '', type: '', fileMapping: ''}
    //$scope.rulesList = [angular.copy(ruleObj), angular.copy(ruleObj)]
    $scope.hideUpload = true
    let getLayoutRules = function(){
        $http.get('layout/attribute').then(function (response) {
            allPossibleRules = angular.copy(response.data);
            $scope.rulesList = $scope.templateInfo ? $scope.templateInfo.layoutRuleMapList :response.data;
        },function error(error){
            $scope.rulesList = [];
        });
    }
    $scope.showRule = false;
    $scope.showAddRule = function(index){
        return $scope.rulesList.length-1 == index ;
    }
    $scope.addRule = function () {
        if($scope.showRule === false){
            $scope.showRule = true
        }else{
            $scope.showRule =false
        }
    }
    $scope.getAvailableRules = function(){
        return _.filter(allPossibleRules, function(eachRule){
            return !_.find($scope.rulesList,function(eachRuleOnScreen){
                return eachRuleOnScreen.name == eachRule.name;
            })
        })
    }
    $scope.addRuleToDisplay = function(item,model){
        $scope.rulesList.push(_.find(allPossibleRules,function(e){
            return e.name==item.name;
        }));
        if($scope.rulesList.length === 0){
            $scope.showRule =false
        }
    }
    $scope.removeRule = function (item, index) {
        $scope.rulesList.splice(index,1)
    }
    let prepareModel = function(){
        let rulesToPost = [];
        _.forEach($scope.rulesList,function(e){ 
                let obj= {name : e.name,operation : e.operation,value : e.value};
                rulesToPost.push(obj);
        })
        let layoutObject = {
            brand : $scope.selectedBrand,
            layoutId : $scope.selectedTemplate.selected.value,
            layoutRuleMapList : rulesToPost
        }
        return layoutObject;
    }
    $scope.dismissModal = function(result) {
        console.log('in dismissModal ',$scope,result);
        //options.body.bodyContent = prepareModel();
        if(result =='Add' || result ==='Update'){
          console.log('Here in dismissModal reulet is ') ; 
        }
        close({ operation : result, data :prepareModel() },200);
    }
    $scope.addBrands = false

    $scope.skipToRules = function(){
        if($scope.addBrands === false){
            $scope.addBrands = true
        }
        else{
            $scope.addBrands = false
        }
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
    
    let getBrands = function (){

        $http.get('/brand/config/brand').then(function (response) {
            $scope.allBrands = response.data;
            $scope.allBrands.push('All');
        },function error(error){
            $scope.allBrands = [];
        });
    }
    let getLayouts = function (template) {

        $http.get('/layout/list').then(function (response) {
            $scope.allLayouts = response.data;
            template ? $scope.selectedTemplate.selected = _.find($scope.allLayouts,function(l){return l.value==template.layoutId }):'';
        },function error(error){
            $scope.allLayouts = {};
        })
        // $scope.allLayouts = $scope.layouts = [{brand: 'KHSD'},{brand: 'ASD'},{brand: 'QWE'}]
        console.log('$scope.allLayouts',$scope.allLayouts)
    }


    $scope.onBrandSelect = function(item,model){
        $scope.selectedBrand = item;
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
        // !$scope.template.name ? $scope.template.name = $scope.template.file.name : '';
        $scope.template.name = ($scope.template.file !== null) ? $scope.template.file.name : ''
    };

    $scope.uploadLayout = function(){
        $scope.selectedTemplate.selected = $scope.items;
        var fd = new FormData()
        fd.append('name', $scope.template.name)
        fd.append('description', $scope.template.desc)
        fd.append('file', $scope.template.file)
        $http.post('http://ec2-52-57-41-173.eu-central-1.compute.amazonaws.com:8888/layout/template',fd,{
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined}
        }).then(function(response){
            console.log(response)
            getLayoutRules()
            $scope.skipToRules()
        })//add error callback
        console.log('test')
    }
    $scope.downLoadLayout = function(layout){
        //TODO ADD FUNCTIONALITY FOR THIS
    }

    $scope.$watch('template.file',function(){
        $scope.onTemplateChange();
    });

    $scope.moveToRules = function(){
        return false;
    }
    
    let init = function(){
        $scope.templateInfo  ? $scope.selectedBrand = $scope.templateInfo.brand : getBrands();
        getLayouts($scope.templateInfo);
        getLayoutRules();
    }
    init();
});
