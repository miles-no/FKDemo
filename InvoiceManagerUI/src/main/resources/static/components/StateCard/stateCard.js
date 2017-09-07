const stateCard = {
    templateUrl: 'components/StateCard/stateCard.html',
    bindings: {
        theme: '<',
        processingState: '='
    },
    controller: class stateCard {
        constructor($interval,$state) {
            'ngInject';
            this.$interval = $interval;
            this.$state = $state;
            this.odometerOptions = {theme: 'plaza', duration: 3000,animation : 'count'};
            console.log('In the component here ',this,this.theme,this.odometerOptions,this.processingState);
            
        }
        openDetailedView(){
                console.log('Entered openDetailedView ');
                this.$state.go('overview',{"processingState": this.processingState.name});
        };
    },
}

export {stateCard};