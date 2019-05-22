const listPopupController = ($scope,options,close, $http,_) => {
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
    $scope.loading = false;
    $scope.selectedBrand = '';
    $scope.selectedTemplate={};
    $scope.uploadClicked = false
    let allPossibleRules = {}
    var ruleObj = {name: '', type: '', fileMapping: ''}
    //$scope.rulesList = [angular.copy(ruleObj), angular.copy(ruleObj)]

    $(document).on('shown.bs.modal', '.modal',() => {
        $(this).find('[autofocus]').focus();
    });

    $scope.hideUpload = true
    let getLayoutRules = (setLayout) => {
        $http.get('/invoicemanager/api/layout/attribute').then((response) => {
            allPossibleRules = angular.copy(response.data);
            if ($scope.templateInfo){
                $scope.rulesList = $scope.templateInfo.layoutRuleMapList
                angular.forEach(allPossibleRules,(e) => {
                    angular.forEach($scope.rulesList,(item,index) => {
                        if (item.name === e.name){
                            $scope.rulesList[index].options = []
                            angular.forEach(e.options,(data) => {
                                $scope.rulesList[index].options.push(data)
                            })

                        }
                    })
                })
            }else{
                $scope.showRule = true
            }
             //$scope.templateInfo ? ($scope.rulesList = $scope.templateInfo.layoutRuleMapList) : $scope.showRule = true;
        },(error) => {
            $scope.rulesList = [];
        });
    }
    $scope.showRule = false;

    $scope.addRule = () => {
        if($scope.showRule === false){
            $scope.showRule = true
        }else{
            $scope.showRule =false
        }
    }

    $scope.onOptionsSelect = (item,rule) => {
        console.log($scope.rulesList)
        rule.value = item
        console.log($scope.rulesList)
    }

    $scope.uploadButtonClicked = () => {
        $scope.uploadClicked = true
    }


    $scope.getAvailableRules = () => {
        return _.filter(allPossibleRules,(eachRule) =>{
            return !_.find($scope.rulesList,(eachRuleOnScreen) => {
                return eachRuleOnScreen.name == eachRule.name;
            })
        })
    }
    $scope.addRuleToDisplay = (item,model) => {
        $scope.rulesList.push(_.find(allPossibleRules,(e) => {
            return e.name==item.name;

        }));
        $scope.showRule =false;
    }
    $scope.removeRule = (item, index) => {
        $scope.rulesList.splice(index,1)
        if ($scope.rulesList.length === 0){
            $scope.showRule = true
            $scope.getAvailableRules()
        }
    }
    let prepareModel = () => {
        let rulesToPost = [];
        _.forEach($scope.rulesList,(e) => {
                let obj= {name : e.name,operation : e.operation,value : e.value};
                rulesToPost.push(obj);
        })
        let layoutObject = {
            brand : $scope.selectedBrand,
            layoutId : $scope.selectedTemplate.selected.value,
            layoutRuleMapList : rulesToPost,
            id : $scope.templateInfo && $scope.templateInfo.id ? $scope.templateInfo.id : null
        }
        return layoutObject;
    }
    $scope.dismissModal = (result) => {
        console.log('in dismissModal ',$scope,result);
        //options.body.bodyContent = prepareModel();
        if(result =='Add' || result ==='Update'){
          console.log('Here in dismissModal reulet is ') ;
        }
        close({ operation : result, data :prepareModel() },200);
    }
    $scope.addBrands = false

    $scope.skipToRules =() => {
        if($scope.addBrands === false){
            $scope.addBrands = true
        }
        else{
            $scope.addBrands = false
        }
    }
    $scope.getAllOperationForAType = (type) => {
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


    let getBrands =  () => {
        $http.get('/invoicemanager/api/brand/config/brand').then((response) => {
            $scope.allBrands = response.data;
            $scope.allBrands.push('All');
        },(error) => {
            $scope.allBrands = [];
        });
    }
    let getLayouts =  (template) => {

        $http.get('/invoicemanager/api/layout/list').then((response) => {
            $scope.allLayouts = response.data;
            template ? $scope.selectedTemplate.selected = _.find($scope.allLayouts,function(l){return l.value==(template.layoutId? template.layoutId: template.id) }):'';
        },(error) => {
            $scope.allLayouts = {};
        })
        // $scope.allLayouts = $scope.layouts = [{brand: 'KHSD'},{brand: 'ASD'},{brand: 'QWE'}]
        console.log('$scope.allLayouts',$scope.allLayouts)
    }


    $scope.onBrandSelect = (item,model) => {
        $scope.selectedBrand = item;
    }

    $scope.onLayoutSelect = (item,model) => {
        $scope.selectedTemplate.selected = item;
    }

    $scope.uploadLayout = () => {
        var fd = new FormData()
        fd.append('name', $scope.template.name)
        fd.append('description', $scope.template.desc)
        $scope.template.file ? fd.append('file', $scope.template.file) : '';
        $scope.loading = true;
        $http.post('/invoicemanager/api/layout/template',fd,{
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined}
        }).then((response) => {
            $scope.loading = false;
            getLayoutRules();
            getLayouts(response.data);
            $scope.skipToRules()
        });//add error callback
    }

    $scope.postLayout = () => {
        if($scope.template.file === null){
            $scope.uploadClicked = false
            return
        }
        var fd = new FormData()
        fd.append('name', $scope.template.name)
        fd.append('description', $scope.template.desc)
        $scope.template.file ? fd.append('file', $scope.template.file) : '';
        $http.put('/invoicemanager/api/layout/template/'+$scope.templateInfo.layoutId , fd, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined}
        }).then((response) => {
            getLayoutRules();
            getLayouts(response.data);
            $scope.skipToRules()
        })
    }

    $scope.$watch('template.file',(newVal, oldVal) => {
        if (newVal !== oldVal && newVal !== '') {
            $scope.selectedTemplate ={};
            if(!$scope.template.name){
                $scope.template.name = $scope.template.file.name
            }
        }
    });

    $scope.moveToRules = () => {
        return false;
    }

    let init = () => {
        $scope.templateInfo  ? $scope.selectedBrand = $scope.templateInfo.brand : getBrands();
        $scope.templateInfo  ? $scope.template.name  = $scope.layoutUpdateObject.name : '';
        $scope.templateInfo  ? $scope.template.desc  = $scope.layoutUpdateObject.description : '';
        $scope.templateInfo && $scope.templateInfo.layoutRuleMapList.length < 1 ?  $scope.showRule =true :'';
        getLayouts($scope.templateInfo);
        getLayoutRules();
    }
    init();
};
export{listPopupController}
