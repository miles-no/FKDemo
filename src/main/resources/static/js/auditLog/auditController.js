var app  = angular.module('invoiceManagerApp')

app.controller('auditLogController',function($scope,$http,moment){

  $scope.getLogs = function(pageNumber){
    let queryParams = {
      "page": pageNumber ? pageNumber-1 : 0,
      "size": $scope.pageSize
    }
    $http.get('/auditRecord',{params:queryParams}).then(function(response){
      $scope.auditLog = response.data.AUDIT_LOG
      $scope.totalPages = Math.ceil(response.data.TOTAL/$scope.pageSize);
    })
  }

  //$.('audit-ellipsis').on('mouseover',function(){
  //  if ()
  //})

  $scope.showHiddenText = function(elem){
    if (elem.style.overflow == 'hidden'){
      elem.style = 'initial';
    }
  }

  $scope.onPageChanged = function(pageChangedTo){
    $scope.getLogs(pageChangedTo)
  }

  $scope.getForamttedDate = function(date){
    return (date ? moment(date) : moment()).format('YYYY-MM-DD');
  }



  $scope.init = function(){
    $scope.pageSize = 10;
    $scope.getLogs()
  }
})