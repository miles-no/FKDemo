var app  = angular.module('invoiceManagerApp')

app.controller('auditLogController',function($scope,$http,moment){

  $scope.datepickerConfig ={
    "dateFormat" :'YYYY-MM-DD',
    "minDate" : moment().subtract(20,'years'),
    "maxDate" : moment().add(1,'days')
  }

  $scope.getLogs = function(pageNumber){
    let queryParams = {
      "page": pageNumber ? pageNumber-1 : 0,
      "size": $scope.pageSize
      }
      $scope.invoiceNo ? queryParams.invoiceNo = $scope.invoiceNo :'';
      $scope.customerID ? queryParams.customerID = $scope.customerID :'';
      $scope.accountNumber ? queryParams.accountNumber = $scope.accountNumber :'';
      if($scope.fromTime){
        queryParams.fromTime = moment($scope.fromTime).startOf('day').format('YYYY-MM-DD HH:mm:ss')
      }
      if($scope.toTime){
        queryParams.toTime = moment($scope.toTime).startOf('day').format('YYYY-MM-DD HH:mm:ss')
      }
    $http.get('/auditRecord',{params:queryParams}).then(function(response){
      $scope.auditLog = response.data.AUDIT_LOG
      $scope.totalPages = Math.ceil(response.data.TOTAL/$scope.pageSize);
    })
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