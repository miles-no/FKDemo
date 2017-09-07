'use strict';
const paginator = {
    templateUrl: 'components/Paginator/paginator.html',
    bindings: {
        pageSize: '<',
        totalPages :'<',
        onPageChange: '&'
    },
    controller: class paginator{
        constructor(){
            this.currentPage=1;
        }
        getPageData(){
            this.onPageChange({result: this.currentPage});
        }
        getNextPageData(){
            this.currentPage == this.totalPages ? '' : ((this.currentPage = this.currentPage + 1)  && this.getPageData());
        }
        getPrevPageData(){
            this.currentPage == 1 ? '' : ((this.currentPage = this.currentPage - 1 ) && this.getPageData());
        }
        goToFirst(){
            this.currentPage =1
            this.getPageData()
        }
        goToLast(){
            this.currentPage = this.totalPages
            this.getPageData()
        }
    }
}
export {paginator}