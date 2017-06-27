app.controller('listPopupController',function($scope,options,close, $http){
    $scope.options = options;
    $scope.items = options.body.bodyContent
    $scope.dismissModal = function(result){
        close(result,200);
    }
    if ($scope.items.brand.length > 0) {
        $scope.disable = true
    }
    $scope.selectedBrands = options.body.bodyContent
    var getLayouts = function () {
        console.log('$scope.allLayouts',$scope.allLayouts)
        $http.get('/layout/config').then(function (response) {
            $scope.allLayouts =  $scope.layouts = response.data;;
        })
    }

    getLayouts()
    $scope.onLayoutSelect = function(item,model){
        $scope.selectedBrands = item;
    }
});
