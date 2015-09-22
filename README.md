# dns-api

Proof-of-concept server (in node.js) and clients (in node.js and Android) demonstrating an API communicating over the DNS protocol. See [the writeup on Medium](http://medium.com/@dgmltn/foo-bar-baz).

This proof-of-concept server does a String-toUpperCase translation, but it could be expanded to any server that pairs a short (around 100 byte) request to a short (around 256 byte) response.

## Server

You'll need a server with a dedicated IP address, and a domain name whose DNS records you can configure. Configure one "A" DNS record to associate a name with your server's IP address, and one "NS" DNS record that points to the first:

| subdomain      | record type | data           |
|----------------|-------------|----------------|
| a.mydomain.com | A Record    | 1.2.3.4        |
| b.mydomain.com | NS Record   | a.mydomain.com |

Configure your host in config.json (note the preceeding dot):

```
cd location-of-this-cloned-repo
echo '{ "host_suffix": ".b.mydomain.com" }' > config.json
```

Install server-js/dns-server.js on your server, install and run it:

```
cd server-js
install
node dns-server.js
```

That's it!

## Client

You may need to wait a bit for your DNS configuration to propogate. After that, you can test it from the command line:

```
dig -t txt hello-world.b.mydomain.com +short
-or-
host -t TXT hello-world.b.mydomain.com
```

Once it works there, try the client-js/dns-client.js from your local box:

```
cd location-of-this-cloned-repo
echo '{ "host_suffix": ".b.mydomain.com" }' > config.json
cd client-js
install
node dns-client.js hello-world
```

If that works too, you're ready to import the Android app into Android Studio. It'll happily use the same config.json that you already configured earlier.

## Congrats!

You're done! Time to try something more interesting than `.toUpperCase()`. Modify the `process(query)` function in dns-server.js.
