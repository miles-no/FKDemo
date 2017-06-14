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
        'ngFlatDatepicker',
        'angularModalService'
        // 'angular.circular.timepicker'
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
            templateUrl: 'templates/drill-down.html',
            params : {
                processingState : null,
                dates : null
            }
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


