"use strict";

var dns = require('native-dns');

var host_suffix = require('../config.json').host_suffix;

var query = process.argv[2];

var constructedHost = query + host_suffix;

var request = dns.resolveTxt(constructedHost, function (err, results) {
    if (results == undefined) {
        console.log('no results');
    }
    else {
        results.forEach(function (result) {
            console.log(result[0]);
        });
    }
});
