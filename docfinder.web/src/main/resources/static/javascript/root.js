

angular.module("root", ["filters", "ngResource"])
    .controller("index", ["$scope", "$resource", "$location",
        function ($scope, $resource, $location) {
            var srchUrl = $resource($location.protocol() + "://" +
                $location.host() + ":" + 8983 +
                "/solr/gettingstarted/select?q=documentText_content:*"+$location.search().searchTerm+"*&wt=json&sort=_docid_ desc&rows=500");
            restRes = srchUrl.get(function(data) {
                $scope.allDocuments = data.response.docs;
            });
        }]);

angular.module("root")
    .controller("contentDisplay", ["$scope","$resource", "$location",
        function ($scope, $resource,$location) {
            console.log($location.search().docId);

            console.log($location.search().searchTerm);
            var srchUrl = $resource($location.protocol() + "://" +
                $location.host() + ":" + 8983 +
                "/solr/gettingstarted/select?q=id%3A" + $location.search().docId + "&wt=json");

            restRes = srchUrl.get(function(data) {
                console.log($location.protocol() + "://" +
                    $location.host() + ":" + 8983 +
                    "/solr/gettingstarted/select?q=id%3A" + $location.search().docId + "&wt=json")
                $scope.resultDoc = data.response.docs[0];
                console.log($scope.resultDoc.documentText_content[0])
            });

        }])