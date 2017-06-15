/**
 * Created by Kshitij Bahul on 16-05-2017.
 */

var app = angular.module('invoiceManagerApp');

app.controller('pdfDisplayController',function($scope,pdfUrl,close){
    $scope.url=pdfUrl;
    $scope.dismissModal = function(result){
        close(result,200);
    }
});
