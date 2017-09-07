'use strict';
const navigation = {
    templateUrl: 'components/Navigation/navigation.html',
    controller: class navigation {
      constructor($state, $scope,$rootScope, $http){
        this.$http = $http;
        this.$state = $state;
        this.$scope = $scope;
        this.$rootScope = $rootScope;
        this.showMenu = false;
        this.activeMenu = 'home'
        this.isDashbordLive = true;
        this.disableReset = false;
        this.time =  this.$rootScope.time
        this.$rootScope.isDashbordLive = this.isDashbordLive;
        this.$rootScope.$watch('time', () => {
          this.time = this.$rootScope.time
        })
      }
      toggleDashbordLive(){
        this.isDashbordLive = !this.isDashbordLive;
      }
      navigateTo(url) {
        if (! url){
          this.$state.go('home');
          this.activeMenu = 'home';
        }else{
          this.activeMenu = url;
          this.showMenu = !this.showMenu;
          this.$state.go(url);
        }
      }

      logOff(){
        this.$http.get('https://afitest.fjordkraft.no/api/openrest/security/logoff').then((response) => {
          console.log(response);
        })
      }
      resetDashboard() {
        if (!this.disableReset) {
          this.disableReset = true
          this.$http.post('/invoicemanager/transferfile/process').then(function (reponse) {
            this.disableReset = false
          })
        }
      }
    }
}
export {navigation};