angular.module("filters", [])
    .filter("searchFilter", function () {
        return function(docs, srchStr) {
            if(!srchStr){
                return docs;
            }
            var results = [];
            srchStr = srchStr.toLowerCase();
            angular.forEach(docs, function(doc){
                console.log(doc);
                if(doc.originalFileName[0].toString().toLowerCase().indexOf(srchStr) !== -1 ||
                    (doc.documentText_content && (doc.documentText_content[0].toLowerCase().indexOf(srchStr) !== -1) )) {
                    results.push(doc);
                }
            });
            return results;
        };
    });