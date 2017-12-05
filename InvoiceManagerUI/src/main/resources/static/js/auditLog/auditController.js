'use strict';

const auditLogController = ($scope,$http,moment,$state,$stateParams) => {

  $scope.datepickerConfig ={
    "dateFormat" :'YYYY-MM-DD',
    "minDate" : moment().subtract(20,'years'),
    "maxDate" : moment().add(1,'days')
  }

  $scope.$watch(()=>{
    return $state.params.invoiceId
  },(newParams, oldParams)=>{
    $scope.invoiceNo = $state.params.invoiceId
    $scope.getLogs()
  })
  $scope.getLogs = (pageNumber) => {
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
        queryParams.toTime = moment($scope.toTime).hour(23).minute(59).second(59).format('YYYY-MM-DD HH:mm:ss')
      }
    $http.get('/invoicemanager/api/auditRecord',{params:queryParams}).then((response) => {
      $scope.auditLog = response.data.AUDIT_LOG
      $scope.totalPages = Math.ceil(response.data.TOTAL/$scope.pageSize);
    })
  }

  $scope.onPageChanged = (pageChangedTo) => {
    $scope.getLogs(pageChangedTo)
  }

  $scope.getForamttedDate = (date) => {
    return (date ? moment(date) : moment()).format('YYYY-MM-DD HH:mm:ss');
  }

  $scope.init = () => {
    $scope.pageSize = 10;
    $scope.getLogs()
  }
}

export {auditLogController};
