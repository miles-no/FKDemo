var app = angular.module('invoiceManagerApp');

function navigationController($state, $rootScope, $http){
  this.showMenu = false;
  this.activeMenu = 'home'
  this.isDashbordLive = true;
  $rootScope.isDashbordLive = this.isDashbordLive;
  this.toggleDashbordLive = function(){
    this.isDashbordLive = !this.isDashbordLive;
  }

  this.navigateTo = function (url) {
    if (! url){
      $state.go('home');
    }else{
      this.activeMenu = url;
      this.showMenu = !this.showMenu;
      $state.go(url);
    }
  }

  this.disableReset = false
  this.resetDashboard = function () {
    if (!this.disableReset) {
      this.disableReset = true
      var self = this;
      $http.post('/transferfile/process').then(function (reponse) {
        self.disableReset = false
      })
    }
  }
}
app.component('navigation',{
  templateUrl: 'components/Navigation/navigation.html',
  bindings: {},
  controller: navigationController,
});
