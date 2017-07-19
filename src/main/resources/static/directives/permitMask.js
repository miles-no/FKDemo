var app = angular.module('invoiceManagerApp')

app.directive('permitMask', function () {
  return {
    require: 'ngModel',
    priority: 11,
    link: function (scope, element, attrs, ngModel) {
      if (ngModel) {
        var regExp = new RegExp('[' + attrs.permitMask + ']')
        ngModel.$parsers.unshift(function (inputValue) {
          if (!inputValue) {
            return undefined
          }

          var filtered = inputValue.split('').filter(function (s) {
            return s.match(regExp)
          }).join('')

          if (filtered !== ngModel.$viewValue) {
            ngModel.$setViewValue(filtered)
            ngModel.$render()
          }

          return filtered
        })
      }
    }
  }
})