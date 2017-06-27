var app = angular.module('invoiceManagerApp');

function navigationController($state, $rootScope){
  this.showMenu = false;
  this.activeMenu = 'home'
  this.isDashbordLive = true;
  $rootScope.isDashbordLive = this.isDashbordLive;
  this.toggleDashbordLive = function(){
    this.isDashbordLive = !this.isDashbordLive;
  }

  this.navigateTo = function (url) {
    this.activeMenu = url
    this.showMenu = !this.showMenu
    $state.go(url)
  }
}
app.component('navigation',{
  templateUrl: 'components/Navigation/navigation.html',
  bindings: {},
  controller: navigationController,
});
