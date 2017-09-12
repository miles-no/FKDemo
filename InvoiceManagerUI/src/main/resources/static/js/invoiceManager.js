/**
 * Created by Kshitij Bahul on 16-05-2017.
 */
'use strict';

import {mainController} from './controller/mainController'
import {landingPageController} from './controller/landingPageController';
import {drillDownController} from './controller/drillDownController';
import {pdfDisplayController} from './controller/pdfDisplayController';
import {auditLogController} from './auditLog/auditController';
import {ManageBrandsController} from './brands/brandCtrl';
import {ManageBrandsPopupController} from './brands/brandPopUpController';
import {popupController} from './confirmDelete/confirmDelete';
import {GridsController} from './grids/gridCtrl';
import {gridPopupController} from './grids/gridPopUpController';
import {StateConfigController} from './systemConfiguration/systemConfigurationController';
import {systemConfigurationPopupController} from './systemConfiguration/systemConfigurationPopup';
import {templateAttributeController} from './template-attributes/templateAttributeController';
import {templateAttributePopupController} from './template-attributes/templateAttributePopupController';
import {listCtrl} from './template-management/templateManagementController';
import {listPopupController} from './template-management/templateManagementPopupController';
// import {landingPageController} from './controller/landingPageController';
// import {drillDownController} from './controller/drillDownController';
// import {mainController} from './controller/mainController'
// import {landingPageController} from './controller/landingPageController';
// import {drillDownController} from './controller/drillDownController';
//Component Imports
import {stateCard} from '../components/StateCard/stateCard';
import {paginator} from '../components/Paginator/paginator';
import {navigation} from '../components/Navigation/navigation';
import {spinner} from '../components/Spinner/spinner';

//directive import
import {fileModel} from '../directives/fileModel'
import {limitTo} from '../directives/limitTo'
import {permitMask} from '../directives/permitMask'


//external Authentication modules
import {authentication} from 'app-common/modules/authentication';
//external Authorization module
import {authorization} from 'app-common/modules/authorization';

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
        //'authentication',
        //'authorization'
        // 'angular.circular.timepicker'
    ];

var invoiceManager = angular.module('invoiceManagerApp',dependencies);


//configure Controllers

invoiceManager.controller('mainController',mainController);
invoiceManager.controller('landingPageController',landingPageController);
invoiceManager.controller('drillDownController',drillDownController);
invoiceManager.controller('pdfDisplayController',pdfDisplayController);
invoiceManager.controller('auditLogController',auditLogController);
invoiceManager.controller('ManageBrandsController',ManageBrandsController);
invoiceManager.controller('ManageBrandsPopupController',ManageBrandsPopupController);
invoiceManager.controller('popupController',popupController);
invoiceManager.controller('GridsController',GridsController);
invoiceManager.controller('gridPopupController',gridPopupController);
invoiceManager.controller('StateConfigController',StateConfigController);
invoiceManager.controller('systemConfigurationPopupController',systemConfigurationPopupController);
invoiceManager.controller('templateAttributeController',templateAttributeController);
invoiceManager.controller('templateAttributePopupController',templateAttributePopupController);
invoiceManager.controller('listCtrl',listCtrl);
invoiceManager.controller('listPopupController',listPopupController);

// invoiceManager.controller('mainController',mainController);
// invoiceManager.controller('landingPageController',landingPageController);
// invoiceManager.controller('mainController',mainController);
// invoiceManager.controller('landingPageController',landingPageController);


//configure components
invoiceManager.component('stateCard',stateCard);
invoiceManager.component('paginator',paginator);
invoiceManager.component('navigation',navigation);
invoiceManager.component('spinner',spinner);

//configure directives
invoiceManager.directive('fileModel',fileModel);
invoiceManager.directive('limitTo',limitTo);
invoiceManager.directive('permitMask',permitMask);

//Configure Global Constants
invoiceManager.constant('_',window._);
invoiceManager.constant('loginUrl','https://afitest.fjordkraft.no/app/logon/logon.html?redirect=http://localhost:8080/#/home')

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
      }).state('system_config',{
        url: '/system_config',
        templateUrl: 'templates/system-configurations/systemConfigurations.html'
      }).state('attributes',{
        url: '/attributes',
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
