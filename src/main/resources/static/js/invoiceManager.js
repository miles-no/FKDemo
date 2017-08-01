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
        'angularModalService',
        'ngclipboard'
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
            templateUrl: 'templates/landing-page/landing-page.html'
        })
        .state('overview',{
            url: '/overview',
            templateUrl: 'templates/drill-down/drill-down.html',
            params : {
                processingState : null,
                dates : null
            }
        }).state('listBrands',{
        url: '/brands',
        templateUrl: 'templates/brands/brands.html'
      }).state('listGrid',{
        url: '/grids',
        templateUrl: 'templates/grids/grids.html'
      }).state('templates',{
        url: '/templates',
        templateUrl: 'templates/template-management/templateManagement.html'
      }).state('state_config',{
        url: '/state_config',
        templateUrl: 'templates/system-configurations/systemConfigurations.html'
      }).state('rule',{
        url: '/rule',
        templateUrl: 'templates/template-attributes/templateAttributes.html'
      }).state('audit_log',{
        url: '/audit_log',
        templateUrl: 'templates/audit-log/auditLog.html'
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

invoiceManager.run(function($rootScope){
  $rootScope.abcd = 1234;
})
