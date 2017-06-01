/**
 * Created by Kshitij Bahul on 17-05-2017.
 */
'use strict';
var app = angular.module('invoiceManagerApp');
//const colors = require('js/utils/color');
app.controller('landingPageController',function($scope,$http,$interval,_,moment){
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
            "name":"Last Hour"
        },
        {
            "name":"Yesterday"
        },
        {
            "name":"This Week"
        },
        {
            "name":"This Fortnight"
        },
        {
            "name":"This Month"
        },
    ]
    let prepareChartDataForObject = function(chartObject,chartData){
        _.forEach(chartData,(eachData)=>{
            chartObject.labels.push(eachData.name)
            chartObject.data.push(eachData.value)
        })
    }
    let getOverviewDetails = function(item){
        $http.get(`/getOverviewData/${item.valueFrom.format('YYYY-MM-DD HH:mm:ss')}/${item.valueTo.format('YYYY-MM-DD HH:mm:ss')}`).then(
        function success(result){
            console.log(item,model);
            $scope.overviewCount = result.data.TOTAL.value
            prepareChartDataForObject($scope.brandOverview,result.data.STATUS_BY_BRAND);
            prepareChartDataForObject($scope.locationOverview,result.data.STATUS_BY_CITY);
        },function error(error){
            console.log(`in Error ${error}`);
        });
    }
    $scope.getStates = function(){
        $http.get('/getStatementCountByStatus').then(
            function success(result){
                $scope.totalInvoices =0;
                $scope.processingStates = result.data;
                _.forEach($scope.processingStates,function(eachState){
                    if (eachState.name==='PRE_PROCESSING'){
                        eachState.theme ='dark-blue';
                    }
                    if (eachState.status==='MERGING'){
                        eachState.name ='dark-purple';
                    }
                    if (eachState.status==='FAILED'){
                        eachState.name ='dark-red';
                    }
                    if (eachState.status==='READY'){
                        eachState.name ='dark-green';
                    }
                    if (eachState.status==='PROCESSING'){
                        eachState.name ='dark-orange';
                    }
                    if (eachState.status==='PENDING'){
                        eachState.name ='dark-grey';
                    }
                    //$scope.totalInvoices = $scope.totalInvoices + eachState.count;
                    eachState.name = _.capitalize(eachState.name);
                    return eachState;
                })
            },function error(error){
                console.log('Error in response');
        });
    }
    
    $scope.init = function(){
        $scope.colors = ['#FF5500', '#A1D490','#00ADF9','#D792E8','#2B66B3'];
        $scope.name='Kshitij';
        $scope.getStates();
        $scope.overview.selected = $scope.possibleOverviews[0];
    }
    
    $scope.onOverviewSelect = function(item,model){
        console.log(item,model);
        getOverviewDetails(item);
    }
    $scope.testValueChange = 5400;
    $interval(function(){
        console.log('Came in here $interval',$scope.testValueChange);
        $scope.getStates();
    },10000)
});