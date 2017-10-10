'use strict';
const navigation = {
    templateUrl: 'components/Navigation/navigation.html',
    controller: class navigation {
      constructor($state, $scope,$rootScope, $http, $window, logoffConfig, loginUrl){
        this.$http = $http;
        this.$state = $state;
        this.$scope = $scope;
        this.$rootScope = $rootScope;
        this.showMenu = false;
        this.activeMenu = 'home'
        this.isDashbordLive = true;
        this.window = $window
        //this.disableReset = false;
        this.time =  this.$rootScope.time
        this.$rootScope.isDashbordLive = this.isDashbordLive;
        this.logoffConfig = logoffConfig
        this.loginUrl = loginUrl
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

      redirectAfterLogOff(){
        this.window.location = this.loginUrl
      }

      logOff(){
        this.$http.get(this.logoffConfig).then((response) => {
          this.redirectAfterLogOff()
        })
      }
      //resetDashboard() {
      //  if (!this.disableReset) {
      //    this.disableReset = true
      //    this.$http.post('/transferfile/process').then(function (reponse) {
      //      this.disableReset = false
      //    })
      //  }
      //}
      resetDashboard() {
        this.$http.post('/transferfile/process').then(function (reponse) {
        })
      }
    }
}
export {navigation};