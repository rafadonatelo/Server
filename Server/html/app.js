var app = angular.module("acessoRemoto", []);
app.controller("controllerAR", function($scope){
	$scope.r = "FULLMODE";
	$scope.width = screen.width;
	$scope.height = screen.heigth;
	$scope.adjust = function() {
		if($scope.r == "ADJUSTMODE"){
			$scope.r = "FULLMODE";
			$scope.width = screen.width;
			$scope.height = screen.heigth;
		}else{
			$scope.r = "ADJUSTMODE";
			$scope.width = 1000;
			$scope.height = 600;
		}
    };
});

