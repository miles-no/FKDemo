var app  = angular.module('invoiceManagerApp')

app.controller('auditLogController',function($scope,$http,moment){

  $scope.datepickerConfig ={
    "dateFormat" :'YYYY-MM-DD',
    "minDate" : moment().subtract(20,'years'),
    "maxDate" : moment().add(1,'days')

  }

  $scope.getLogs = function(pageNumber){
    let queryParams = {
      "fromTime" :moment($scope.fromDate).format('YYYY-MM-DD HH:mm:ss'),
      "toTime" : moment($scope.toDate).hour(23).minute(59).second(59).format('YYYY-MM-DD HH:mm:ss'),
      "page": pageNumber ? pageNumber-1 : 0,
      "size": $scope.pageSize
      }
      $scope.invoiceNumber ? queryParams.invoiceNumber = $scope.invoiceNumber :'';
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
    return (date ? moment(date) : moment()).format('YYYY-MM-DD HH:mm:ss');
  }



  $scope.init = function(){
    $scope.pageSize = 10;
    $scope.getLogs()
  }
})