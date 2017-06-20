var app = angular.module('invoiceManagerApp');
function getReults(){

}
function paginationController($interval,$state){
    var ctrl = this;
    ctrl.odometerOptions ={theme: 'plaza', duration: 3000,animation : 'count'};
        console.log('In the component here ',ctrl,ctrl.theme,ctrl.odometerOptions,ctrl.processingState);
    ctrl.openDetailedView = function(){
        console.log('Entered openDetailedView ');
        $state.go('overview',{"processingState": ctrl.processingState.name});
    }    
}
app.component('paginator',{
    templateUrl: 'components/Paginator/paginator.html',
    bindings: {
        pageSize: '<',
        totalPages :'<',
        model: '='
    },
    controller: paginationController,
});