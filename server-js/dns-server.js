var dns = require('native-dns');
var server = dns.createServer();

var host_suffix = require('../config.json').host_suffix;

server.on('request', function (request, response) {
    var name = request.question[0].name;
    if (name.substring(name.length - host_suffix.length) != host_suffix) {
        return;
    }

    var query = name.substring(0, name.length - host_suffix.length);
    var result = handle(query);

    var txt =  dns.TXT({
        name: name,
        data: [result],
        ttl: 1,
    });

    response.answer.push(txt);
    response.send();

    console.log(txt);
});

server.on('error', function (err, buff, req, res) {
    console.log(err.stack);
});

server.serve(53);

function handle(query) {
    return query.toUpperCase();
}
