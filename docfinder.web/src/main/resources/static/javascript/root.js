

angular.module("root", ["filters", "ngResource", "ngMaterial", "ngMessages"])
    .controller("index", ["$scope", "$resource", "$location", "$window",
        function ($scope, $resource, $location, $window) {
            function getUrlVars() {
                var vars = {};
                var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
                    vars[key] = value;
                });
                return vars;
            }
            var vars = getUrlVars()
            var srchUrl = $resource($location.protocol() + "://" +
                "172.17.0.2:" + 8983 +
                "/solr/gettingstarted/select?q="+"documentText_content:*"+vars["searchTerm"]+"*"+"&wt=json&sort=_docid_ desc&rows=500");
            restRes = srchUrl.get(function(data) {
                $scope.allDocuments = data.response.docs;
            });
            $scope.searchTerm = "";
            $scope.search = function()
            {
                $window.location.href =
                    "/solrSearch.html?searchTerm=" + $scope.searchTerm;
            }
        }]);

angular.module("root")
    .controller("contentDisplay", ["$scope","$resource", "$location",
        function ($scope, $resource, $location) {

            function getUrlVars() {
                var vars = {};
                var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
                    vars[key] = value;
                });
                return vars;
            }
            var vars = getUrlVars()
            console.log(vars)
            var srchUrl = $resource($location.protocol() + "://" +
                "172.17.0.2" + ":" + 8983 +
                "/solr/gettingstarted/select?q=id%3A" + vars["docId"] + "&wt=json");

            restRes = srchUrl.get(function(data) {
                console.log($location.protocol() + "://" +
                    "172.17.0.2" + ":" + 8983 +
                    "/solr/gettingstarted/select?q=id%3A" + vars["docId"]+ "&wt=json")
                $scope.resultDoc = data.response.docs[0];
                console.log($scope.resultDoc.documentText_content[0])
            });

        }])