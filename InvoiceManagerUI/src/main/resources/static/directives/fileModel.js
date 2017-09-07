const fileModel = ($parse) => {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
      var model = $parse(attrs.fileModel);
      var modelSetter = model.assign;

      element.bind('change', function(){
        scope.$apply(function(){
          modelSetter(scope.$parent, element[0].files[0]);
        });
      });
    }
  };
};

export {fileModel}