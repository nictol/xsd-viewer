app.controller('ctrl', ['$scope', '$http', 'mainService',
    function ($scope, $http, mainService) {
        $scope.postFile = mainService.postFile;
        $scope.getFile = mainService.getFile;
}]);