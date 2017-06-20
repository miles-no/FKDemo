var app = angular.module('invoiceManagerApp');
function getPageData(pageNumber){
    let queryParams = {
        page:pageIndex-1,
        size:this.pageSize
    }
    $http.get(this.getUrl,{params}).then(function(result){
        this.model= result.data[this.key];
    },function(error){
        
    })
}
function paginationController($http){
    var ctrl = this;
    ctrl.currentPage=1;
    //ctrl.totalPages = Math.ceil(ctrl.totalResults/ctrl.pageSize);
    console.log('In the component here ',ctrl);
    let getPageData = function(){
        ctrl.queryParams.page=ctrl.currentPage -1;
        ctrl.queryParams.size= ctrl.pageSize;
        
        $http.get(ctrl.getUrl,{params:ctrl.queryParams}).then(function(result){
            //ctrl.model= result.data[ctrl.key];
            ctrl.onPageChange({result: result});
        },function(error){
            
        })
    }   
    ctrl.getNextPageData = function(){
        ctrl.currentPage = ctrl.currentPage + 1 ;
        getPageData();
    }
    ctrl.getPrevPageData = function(){
        ctrl.currentPage = ctrl.currentPage - 1 ;
        getPageData();
    }
}
app.component('paginator',{
    templateUrl: 'components/Paginator/paginator.html',
    bindings: {
        pageSize: '<',
        totalPages :'=',
        onPageChange: '&',
        getUrl : '<',
        key:'<',
        queryParams :'<'
    },
    controller: paginationController,
});