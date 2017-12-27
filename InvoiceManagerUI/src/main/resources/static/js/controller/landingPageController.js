/**
 * Created by Kshitij Bahul on 17-05-2017.
 */
'use strict';
const landingPageController = ($scope,$http,$interval,_,moment,$rootScope) => {
    $scope.overview ={};
    var dayStart = moment().hour(0).minute(0).second(0);
    var dayEnd = moment().hour(23).minute(59).second(59);
    $scope.totalInvoices =0;
    $scope.overviewCount = 0;
    $scope.locationOverview ={
            data: [],
            labels : [],
            options: {legend: {display: true}}
        };
    $scope.brandOverview = {
        data: [],
        labels : []
    };
    
    $scope.possibleOverviews = [
        {
            "name":"Today",
            "valueFrom" :dayStart,
            "valueTo" :dayEnd
        },
        {
            "name":"Last Hour",
            "valueFrom" :moment().subtract(1,'hours'),
            "valueTo" :moment()
        },
        {
            "name":"Yesterday",
            "valueFrom" :moment().hour(0).minute(0).second(0).subtract(1,'days'),
            "valueTo" : moment().hour(23).minute(59).second(59).subtract(1,'days')
        },
        {
            "name":"This Week",
            "valueFrom" :moment().hour(0).minute(0).second(0).startOf('week'),
            "valueTo" :dayEnd
        },
        {
            "name":"This Fortnight",
            "valueFrom" :moment().hour(0).minute(0).second(0).subtract(14,'days'),
            "valueTo" :dayEnd
        },
        {
            "name":"This Month",
            "valueFrom" :moment().hour(0).minute(0).second(0).startOf('month'),
            "valueTo" :dayEnd
        },
        {
            "name":"Last Month",
            "valueFrom" :moment().hour(0).minute(0).second(0).startOf('month').subtract(1,'month'),
            "valueTo" :moment().hour(0).minute(0).second(0).endOf('month').subtract(1,'month')
        }
    ]
    let prepareChartDataForObject = function(chartObject,chartData){

        _.forEach(chartData,function(eachData){
            chartObject.labels.push(eachData.name)
            chartObject.data.push(eachData.value)
        })
    }
    let getOverviewDetails = function(item){
        $http.get(`/invoicemanager/api/dashboard/all`,{params : {fromTime: item.valueFrom.format('YYYY-MM-DD HH:mm:ss'),toTime: item.valueTo.format('YYYY-MM-DD HH:mm:ss')}}).then(

        function success(result){
            $scope.overviewCount = result.data.TOTAL[0].value;
            $scope.locationOverview ={
                data: [],
                labels : [],
                options: {legend: {display: true}}
            };
            $scope.brandOverview = {
                data: [],
                labels : []
            };
            prepareChartDataForObject($scope.brandOverview,result.data.STATUS_BY_BRAND);
            prepareChartDataForObject($scope.locationOverview,_.takeRight(_.sortBy(result.data.STATUS_BY_CITY,[function (eachCity){ return eachCity.value}]),10));
            $rootScope.brands && $rootScope.brands.length && $rootScope.brands.length>0 ? '' : $rootScope.brands = _.cloneDeep($scope.brandOverview.labels);
        },function error(error){
            console.log(`in Error ${error}`);
        });
    }
    $scope.getStates = function(){
        let getStateNames = $rootScope.states && $rootScope.states.length ==0 ? true: false
        $http.get('/invoicemanager/api/dashboard/status').then(
            function success(result){
                var getDateAndTime = new Date();
                var dateTime = moment(getDateAndTime).format('YYYY-MM-DD HH:mm:ss')
                $rootScope.time = 'Last updated'+ ' ' + dateTime
                $scope.processingStates = result.data;
                _.forEach($scope.processingStates,function(eachState){
                    getStateNames ?(eachState.name==='Total' ? '' :$rootScope.states.push(eachState.name)): '';
                    if (eachState.name==='PRE-PROCESSING'){
                        eachState.theme ='dark-blue';
                    }
                    if (eachState.name==='MERGING'){
                        eachState.theme ='dark-purple';
                    }
                    if (eachState.name==='FAILED'){
                        eachState.theme ='dark-red';
                    }
                    if (eachState.name==='READY'){
                        eachState.theme ='dark-green';
                    }
                    if (eachState.name==='PROCESSING'){
                        eachState.theme ='dark-orange';
                    }
                    if (eachState.name==='PENDING'){
                        eachState.theme ='dark-grey';
                    }
                    if (eachState.name==='Total'){
                        $scope.totalInvoices =eachState.value;
                    }
                    //$scope.totalInvoices = $scope.totalInvoices + eachState.count;
                    eachState.label = _.capitalize(_.camelCase(eachState.name));
                    return eachState;
                })
            },function error(error){
                console.log('Error in response');
        });
    }
    
    $scope.init = function(){
        $scope.colors = ['#FF5500', '#A1D490','#00ADF9','#D792E8','#2B66B3'];
        $rootScope.brands =[];
        $rootScope.states =[];
        $scope.getStates();
        
        $scope.overview.selected = $scope.possibleOverviews[5];
        getOverviewDetails($scope.overview.selected);
    }
    
    $scope.onOverviewSelect = function(item,model){
        console.log(item,model);
        getOverviewDetails(item);
    }
    $scope.testValueChange = 5400;
    let refreshDashbord=$interval(function(){
        console.log('Came in here $interval',$scope.testValueChange);
        $scope.getStates();
    },5000)
    $scope.$on('$destroy', function () { 
        console.log('Came in here $destroy');
        $interval.cancel(refreshDashbord)
    });
}
export {landingPageController};