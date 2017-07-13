app.controller('listPopupController',function($scope,options,close, $http,_){
    $scope.options = options;
    $scope.layoutUpdateObject = options.body.bodyContent;
    $scope.templateInfo = options.body.bodyContent ? options.body.bodyContent.layoutRule : null;
    $scope.allowRules = false;
    $scope.template = {
        name :'',
        desc : '',
        file : null
    };
    $scope.rulesList =[];
    $scope.selectedBrand = '';
    $scope.selectedTemplate={};
    let allPossibleRules = {}
    var ruleObj = {name: '', type: '', fileMapping: ''}
    //$scope.rulesList = [angular.copy(ruleObj), angular.copy(ruleObj)]
    $scope.hideUpload = true
    let getLayoutRules = function(setLayout){
        $http.get('layout/attribute').then(function (response) {
            allPossibleRules = angular.copy(response.data);
             $scope.templateInfo ? $scope.rulesList = $scope.templateInfo.layoutRuleMapList : $scope.showRule = true;
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
        $scope.showRule =false;
    }
    $scope.removeRule = function (item, index) {
        $scope.rulesList.splice(index,1)
        if ($scope.rulesList.length === 0){
            $scope.showRule = true
            $scope.getAvailableRules()
        }
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
                return ['LESS THAN','LESS THAN EQUAL TO','GREATER THAN','GREATER THAN EQUAL TO','EQUALS','NOT EQUALS'] ;
            case 'FLOAT':
                return ['LESS THAN','LESS THAN EQUAL TO','GREATER THAN','GREATER THAN EQUAL TO','EQUALS','NOT EQUALS'] ;
            default :
                return ['EQUALS','NOT EQUALS'] ;
        }


    }
    
    
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
            template ? $scope.selectedTemplate.selected = _.find($scope.allLayouts,function(l){return l.value==(template.layoutId? template.layoutId: template.id) }):'';
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

    
    $scope.onTemplateChange = function(){
        console.log('Came here on Template change',$scope.selectedTemplate)
        $scope.selectedTemplate ={};
        !$scope.template.name ? $scope.template.name = ($scope.template.file ? $scope.template.file.name:'') : '';
        //$scope.template.name = ($scope.template.file !== null) ? $scope.template.file.name : ''
    };

    $scope.uploadLayout = function(){
        var fd = new FormData()
        fd.append('name', $scope.template.name)
        fd.append('description', $scope.template.desc)
        $scope.template.file ? fd.append('file', $scope.template.file) : '';
        $http.post('/layout/template',fd,{
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined}
        }).then(function(response){
            console.log(response)
            getLayoutRules();
            getLayouts(response.data);
            $scope.skipToRules()
        })//add error callback
        console.log('test')
    }
    $scope.downLoadLayout = function(layout){
        var id = layout.value
        $http({
            method : 'GET',
            url : '/layout/rptdesign?id='+id,
        }).then(function(response,status,headers){
            var file = new Blob([response.data], {type: 'application/xml'});
            var downloadLink = angular.element('<a></a>');
            downloadLink.attr('href',window.URL.createObjectURL(file));
            downloadLink.attr('download', layout.name+'.xml');
            downloadLink[0].click();

        })
    }
    $scope.$watch('template.file',function(){
        $scope.onTemplateChange();
    });

    $scope.moveToRules = function(){
        return false;
    }
    
    let init = function(){
        $scope.templateInfo  ? $scope.selectedBrand = $scope.templateInfo.brand : getBrands();
        $scope.templateInfo  ? $scope.template.name  = $scope.layoutUpdateObject.name : '';
        $scope.templateInfo  ? $scope.template.desc  = $scope.layoutUpdateObject.description : '';
        $scope.templateInfo && $scope.templateInfo.layoutRuleMapList.length < 1 ?  $scope.showRule =true :'';
        getLayouts($scope.templateInfo);
        getLayoutRules();
    }
    init();
});
