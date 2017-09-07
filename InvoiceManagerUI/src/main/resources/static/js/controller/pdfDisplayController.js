/**
 * Created by Kshitij Bahul on 16-05-2017.
 */
'use strict';
const pdfDisplayController = ($scope,pdfUrl,close) =>{
    $scope.url=pdfUrl;
    $scope.dismissModal = function(result){
        close(result,200);
    }
}
export {pdfDisplayController};