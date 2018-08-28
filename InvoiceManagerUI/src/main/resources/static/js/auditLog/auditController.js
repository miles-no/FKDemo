'use strict';

const auditLogController = ($scope,$http,moment,$state,$stateParams) => {

  $scope.logType = ''
  $scope.allLogTypes = ['INFO','ERROR','WARNING']
  $scope.legalPartClass = ''
  $scope.datepickerConfig ={
    "dateFormat" :'YYYY-MM-DD',
    "minDate" : moment().subtract(20,'years'),
    "maxDate" : moment().add(1,'days')
  }

  $scope.getLogs = (pageNumber) => {
    let queryParams = {
      "page": pageNumber ? pageNumber-1 : 0,
      "size": $scope.pageSize
      }
      $scope.legalPartClass ? queryParams.legalPartClass = $scope.legalPartClass :'';
      $scope.invoiceNo ? queryParams.invoiceNo = $scope.invoiceNo :'';
      $scope.customerID ? queryParams.customerID = $scope.customerID :'';
      $scope.accountNumber ? queryParams.accountNumber = $scope.accountNumber :'';
      $scope.logType ? queryParams.logType = $scope.logType : '';
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

  $scope.setLogType = (item) => {
    $scope.logType = item
    $scope.getLogs()
  }

  $scope.onLegalSelect = function(item,model){
    $scope.legalPartClass = item;
    $scope.getLogs();
  }

  $scope.init = () => {
    $scope.pageSize = 10;
    $stateParams.invoiceNo ? $scope.invoiceNo = $stateParams.invoiceNo : $scope.invoiceNo = null
    $scope.legalOptions = ["Individual","Organization"]
    $scope.getLogs()
  }
}

export {auditLogController};
