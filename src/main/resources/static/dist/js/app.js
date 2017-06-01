/**
 * Created by Kshitij Bahul on 16-05-2017.
 */
'use strict';

let dependencies = [
        'ui.router',
        'ui.bootstrap',
        'chart.js',
        'ui.odometer',
        'ngMaterial',
        'ui.select',
        'ngSanitize',
        'angularMoment',
        'smart-table',
        '720kb.datepicker'
    ];

var invoiceManager = angular.module('invoiceManagerApp',dependencies);

//Configure Global Constants 
invoiceManager.constant('_',window._);

//Configure routes

invoiceManager.config(function($stateProvider,$urlRouterProvider){
    $stateProvider
        .state('home',{
            url: '/home',
            templateUrl: 'templates/landing-page.html'
        })
        .state('overview',{
            url: '/overview',
            templateUrl: 'templates/overview.html'
        });
    $urlRouterProvider.otherwise('/home');
});

//Configure Material Themes
invoiceManager.config(function($mdThemingProvider) {
  $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
  $mdThemingProvider.theme('dark-orange').backgroundPalette('orange').dark();
  $mdThemingProvider.theme('dark-purple').backgroundPalette('deep-purple').dark();
  $mdThemingProvider.theme('dark-blue').backgroundPalette('blue').dark();
  $mdThemingProvider.theme('dark-red').backgroundPalette('red').dark();
  $mdThemingProvider.theme('dark-green').backgroundPalette('green').dark();
  $mdThemingProvider.theme('dark-blue').backgroundPalette('blue').dark();
});


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
/**
 * Created by Kshitij Bahul on 16-05-2017.
 */

var app = angular.module('invoiceManagerApp');

app.controller('mainController',function($scope,$http){
    $scope.init = function(){
      console.log('Here in mainController')  ;
    };
});

/**
 * Created by Kshitij Bahul on 17-05-2017.
 */

var app = angular.module('invoiceManagerApp');

app.controller('overviewController',function($scope,$http,moment){
    $scope.date1;
    let dummyData = {"firstName": "Name ","lastName":"Last Name ","birthDate":moment().format('LLL'),"balance": 22.5};
    $scope.rowCollection = [
        
    ]
    $scope.init = function(){
        $scope.name='Kshitij';
        for(var i =0; i<100; i++){

            $scope.rowCollection.push({"firstName":`${dummyData.firstName} ${i}`,"lastName":`${dummyData.lastName}  ${i}`,"birthDate":moment().format('LLL'),"balance": 22.5})
        }
    }
});
/*
module.exports ={
  brandColor : '#FF5500'
};*/
console.log('This is a helper class');
var app = angular.module('invoiceManagerApp');
function stateCardController($interval){
    var ctrl = this;
    ctrl.odometerOptions ={theme: 'plaza', duration: 3000,animation : 'count'};
        console.log('In the component here ',ctrl,ctrl.theme,ctrl.odometerOptions,ctrl.processingState);
}
app.component('stateCard',{
    templateUrl: 'components/StateCard/stateCard.html',
    bindings: {
        theme: '<',
        processingState: '='
    },
    controller: stateCardController,
});