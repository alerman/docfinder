angular.module("root", ["filters", "ngResource"])
    .controller("index", ["$scope", "$resource", "$location",
        function ($scope, $resource, $location) {
            var srchUrl = $resource($location.protocol() + "://" +
                $location.host() + ":" + 8983 +
                "/solr/gettingstarted/select?q=*:*&wt=json&sort=_docid_ desc");
            restRes = srchUrl.get(function(data) {
                $scope.allDocuments = data.response.docs;
            });
        }]);