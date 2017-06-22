var app = angular.module('invoiceManagerApp');

function paginationController($http){
    var ctrl = this;
    ctrl.currentPage=1;
    console.log('In the component here ',ctrl);
    let getPageData = function(){
        ctrl.onPageChange({result: ctrl.currentPage});
    }   
    ctrl.getNextPageData = function(){
        ctrl.currentPage == ctrl.totalPages ? '' : (ctrl.currentPage = ctrl.currentPage + 1  || getPageData());
    }
    ctrl.getPrevPageData = function(){
         ctrl.currentPage == 1 ? '' : (ctrl.currentPage = ctrl.currentPage - 1  || getPageData());
    }
}
app.component('paginator',{
    templateUrl: 'components/Paginator/paginator.html',
    bindings: {
        pageSize: '<',
        totalPages :'<',
        onPageChange: '&'
    },
    controller: paginationController,
});