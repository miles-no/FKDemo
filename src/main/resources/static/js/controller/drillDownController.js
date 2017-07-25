/**
 * Created by Kshitij Bahul on 17-05-2017.
 */

var app = angular.module('invoiceManagerApp');

app.controller('drillDownController',function($scope,$http,moment,$rootScope,$stateParams,ModalService){
    
    let dummyData = {"firstName": "Name ","lastName":"Last Name ","birthDate":moment().format('LLL'),"balance": 22.5};
    $scope.rowCollection = [];
    $scope.searchResults = [];
    $scope.states =[];
    $scope.brands =[];
    $scope.loading = false;
    $scope.datepickerConfig ={
        "dateFormat" :'YYYY-MM-DD',
        "minDate" : moment().subtract(20,'years'),
        "maxDate" : moment().add(1,'days')

    }

    $scope.onError = function(e) {
        console.error('Action:', e.action);
        console.error('Trigger:', e.trigger);
    }

    $scope.onStateSelect = function(item,model){
        console.log('onStateSelect ',item,model,$scope.states)
        $scope.states.push(item);
        $scope.getOverviewDetails();
    }
    $scope.onStateRemoval = function(item,model){
        console.log('onStateRemoval ',item,model,$scope.states)
         _.remove($scope.states,function(eachSelectedState){
            return eachSelectedState === item;
        });
        $scope.getOverviewDetails();
    }
    $scope.onBrandSelect = function(item,model){
        console.log('onStateSelect ',item,model,$scope.states)
        $scope.brands.push(item);
        $scope.getOverviewDetails();
    }
    $scope.onBrandRemoval = function(item,model){
        console.log('onStateRemoval ',item,model,$scope.states)
         _.remove($scope.brands,function(eachSelectedBrand){
            return eachSelectedBrand === item;
        });
        $scope.getOverviewDetails();
    }
    $scope.onPageChanged = function(pageChangedTo){
        $scope.getOverviewDetails(pageChangedTo)
    }
    $scope.getOverviewDetails = function(pageNumber){
        let queryParams = {
            "fromTime" :moment($scope.fromDate).format('YYYY-MM-DD HH:mm:ss'),
            "toTime" : moment($scope.toDate).hour(23).minute(59).second(59).format('YYYY-MM-DD HH:mm:ss'),
            "page": pageNumber? pageNumber-1 : 0 ,
            "size":$scope.pageSize
        }
        $scope.states && $scope.states.length >0 ? queryParams.states = _.join($scope.states,',') :'';
        $scope.brands && $scope.brands.length >0 ? queryParams.brand = _.join($scope.brands,',') :'';
        $scope.invoiceNumber ? queryParams.invoiceNumber = $scope.invoiceNumber :'';
        $http.get('/statement/details',{params:queryParams}).then(function success(result){
            $scope.searchResults = result.data.STATEMENTS
            $scope.totalPages = Math.ceil(result.data.TOTAL/$scope.pageSize);
        },function error(error){
            $scope.searchResults = [{"id":7326,"status":"INVOICE_PROCESSED","statementId":"707049570510175","statementType":null,"invoiceNumber":"707049570517","accountNumber":"7070495705","customerId":"50384706","createTime":1496820841404,"udateTime":1496820971610,"pdfAttachment":null,"city":"DRAMMEN","version":1,"distributionMethod":"EHF","amount":1186.8,"invoiceDate":null,"dueDate":1494354600000,"invoicePdfList":[{"id":16685,"statementId":7326,"type":"ATTACH_PDF"},{"id":16715,"statementId":7326,"type":"INVOICE_PDF"}]},{"id":7327,"status":"INVOICE_PROCESSED","statementId":"707050530510172","statementType":null,"invoiceNumber":"707050530517","accountNumber":"7070505305","customerId":"50384706","createTime":1496820841440,"udateTime":1496820971598,"pdfAttachment":null,"city":"DRAMMEN","version":1,"distributionMethod":"EHF","amount":676.03,"invoiceDate":null,"dueDate":1494354600000,"invoicePdfList":[{"id":16687,"statementId":7327,"type":"ATTACH_PDF"},{"id":16718,"statementId":7327,"type":"INVOICE_PDF"}]},{"id":7328,"status":"INVOICE_PROCESSED","statementId":"707050540410173","statementType":null,"invoiceNumber":"707050540417","accountNumber":"7070505404","customerId":"50384706","createTime":1496820841479,"udateTime":1496820971610,"pdfAttachment":null,"city":"DRAMMEN","version":1,"distributionMethod":"EHF","amount":585.94,"invoiceDate":null,"dueDate":1494354600000,"invoicePdfList":[{"id":16711,"statementId":7328,"type":"INVOICE_PDF"},{"id":16688,"statementId":7328,"type":"ATTACH_PDF"}]},{"id":7329,"status":"INVOICE_PROCESSED","statementId":"707051520510172","statementType":null,"invoiceNumber":"707051520517","accountNumber":"7070515205","customerId":"50384706","createTime":1496820841510,"udateTime":1496820971614,"pdfAttachment":null,"city":"DRAMMEN","version":1,"distributionMethod":"EHF","amount":1353.84,"invoiceDate":null,"dueDate":1494354600000,"invoicePdfList":[{"id":16717,"statementId":7329,"type":"INVOICE_PDF"},{"id":16690,"statementId":7329,"type":"ATTACH_PDF"}]},{"id":7330,"status":"INVOICE_PROCESSED","statementId":"707051610410176","statementType":null,"invoiceNumber":"707051610417","accountNumber":"7070516104","customerId":"50384706","createTime":1496820841539,"udateTime":1496820971605,"pdfAttachment":null,"city":"DRAMMEN","version":1,"distributionMethod":"EHF","amount":1215.65,"invoiceDate":null,"dueDate":1494354600000,"invoicePdfList":[{"id":16714,"statementId":7330,"type":"INVOICE_PDF"},{"id":16691,"statementId":7330,"type":"ATTACH_PDF"}]},{"id":7331,"status":"INVOICE_PROCESSED","statementId":"707052530310173","statementType":null,"invoiceNumber":"707052530317","accountNumber":"7070525303","customerId":"50384706","createTime":1496820841569,"udateTime":1496820971619,"pdfAttachment":null,"city":"DRAMMEN","version":1,"distributionMethod":"EHF","amount":766.1,"invoiceDate":null,"dueDate":1494354600000,"invoicePdfList":[{"id":16716,"statementId":7331,"type":"INVOICE_PDF"},{"id":16689,"statementId":7331,"type":"ATTACH_PDF"}]},{"id":7332,"status":"INVOICE_PROCESSED","statementId":"707052550110172","statementType":null,"invoiceNumber":"707052550117","accountNumber":"7070525501","customerId":"50384706","createTime":1496820841593,"udateTime":1496820971594,"pdfAttachment":null,"city":"DRAMMEN","version":1,"distributionMethod":"EHF","amount":1938.07,"invoiceDate":null,"dueDate":1494354600000,"invoicePdfList":[{"id":16712,"statementId":7332,"type":"INVOICE_PDF"},{"id":16692,"statementId":7332,"type":"ATTACH_PDF"}]},{"id":7333,"status":"INVOICE_PROCESSED","statementId":"707052560010172","statementType":null,"invoiceNumber":"707052560017","accountNumber":"7070525600","customerId":"50384706","createTime":1496820841616,"udateTime":1496820971627,"pdfAttachment":null,"city":"DRAMMEN","version":1,"distributionMethod":"EHF","amount":1109.45,"invoiceDate":null,"dueDate":1494354600000,"invoicePdfList":[{"id":16719,"statementId":7333,"type":"INVOICE_PDF"},{"id":16693,"statementId":7333,"type":"ATTACH_PDF"}]},{"id":7334,"status":"INVOICE_PROCESSED","statementId":"707052570910171","statementType":null,"invoiceNumber":"707052570917","accountNumber":"7070525709","customerId":"50384706","createTime":1496820841639,"udateTime":1496820971626,"pdfAttachment":null,"city":"DRAMMEN","version":1,"distributionMethod":"EHF","amount":2391.99,"invoiceDate":null,"dueDate":1494354600000,"invoicePdfList":[{"id":16720,"statementId":7334,"type":"INVOICE_PDF"},{"id":16694,"statementId":7334,"type":"ATTACH_PDF"}]},{"id":7335,"status":"INVOICE_PROCESSED","statementId":"707052580810171","statementType":null,"invoiceNumber":"707052580817","accountNumber":"7070525808","customerId":"50384706","createTime":1496820841661,"udateTime":1496820974672,"pdfAttachment":null,"city":"DRAMMEN","version":1,"distributionMethod":"EHF","amount":1136.31,"invoiceDate":null,"dueDate":1494354600000,"invoicePdfList":[{"id":16695,"statementId":7335,"type":"ATTACH_PDF"},{"id":16721,"statementId":7335,"type":"INVOICE_PDF"}]}]
        });
    }
    $scope.getForamttedDate = function(date){
        return (date ? moment(date) : moment()).format('YYYY-MM-DD');
    }

    $scope.getInvoiceFile = function(invoiceId){
        $scope.loading = true
        $http.get('/statement/pdf/'+invoiceId,{responseType: 'arraybuffer'}).success(function(response,status,headers){
          $scope.loading = false;
          var file = new Blob([response], {type: 'application/pdf'});
	        var fileURL = URL.createObjectURL(file);
            ModalService.showModal({
                templateUrl: 'templates/pdf-display-modal.html',
                controller: 'pdfDisplayController',
                inputs:{
                    pdfUrl :fileURL
                }
            }).then(function(modal){
                modal.element.modal();
                modal.close.then(function(result){
                    console.log('The modal got closed');
                });
            });
        }).error(
            function(error){
                console.log(error);
            }
        );
    }
    const getAllBrands = function(){
        $http.get('/brand/config/brand').then(function success(result){
            $scope.possibleBrands = result.data;
        },function error(error){
            console.log('Could not get the brands');
        });
    }
    $scope.isInvoicePDF = function (file){
        return file.type=='INVOICE_PDF';
    }
    $scope.init = function(){
        $scope.pageSize = 10;
        console.log($rootScope,$stateParams);
        $scope.fromDate = moment().subtract(1,'months').hour(0).minute(0).second(0).format('YYYY-MM-DD');
        $scope.toDate= moment().hour(23).minute(59).second(59).format('YYYY-MM-DD');
        getAllBrands();
        $scope.possibleStates = $rootScope.states;
        !$scope.possibleStates || ($scope.possibleStates && $scope.possibleStates.length)== 0 ? $scope.possibleStates = ["PENDING","PRE-PROCESSING","PROCESSING","MERGING","READY","FAILED"] :'';
        $stateParams && $stateParams.processingState ? $scope.states.push($stateParams.processingState) :'';
        $scope.getOverviewDetails();
    }
});