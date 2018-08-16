'use strict';
const imageDisplayController = ($scope,ImageUrl,close) =>{
    $scope.url=ImageUrl;
    $scope.dismissModal = function(result){
        close(result,200);
    }
}
export {imageDisplayController};