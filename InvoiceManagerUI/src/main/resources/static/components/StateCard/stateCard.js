var app = angular.module('invoiceManagerApp');
function stateCardController($interval,$state){
    var ctrl = this;
    ctrl.odometerOptions ={theme: 'plaza', duration: 3000,animation : 'count'};
        console.log('In the component here ',ctrl,ctrl.theme,ctrl.odometerOptions,ctrl.processingState);
    ctrl.openDetailedView = function(){
        console.log('Entered openDetailedView ');
        $state.go('overview',{"processingState": ctrl.processingState.name});
    }    
}
app.component('stateCard',{
    templateUrl: 'components/StateCard/stateCard.html',
    bindings: {
        theme: '<',
        processingState: '='
    },
    controller: stateCardController,
});