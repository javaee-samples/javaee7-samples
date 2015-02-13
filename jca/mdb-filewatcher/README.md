## File Watcher MDB

### What is this?

This sample project demonstrates that writing (and testing) JCA resource adapter is fairly easy. We implemented Message Driven Bean which observes certain directory for files which are created, updated or deleted.

It's tested using:

* [Arquillian](http://arquillian.org) - powerful testing middleware
* [Awaitility](https://code.google.com/p/awaitility/) - simple, yet powerful DSL that allows you to express expectations of an asynchronous system in a concise and easy to read manner


This sample project is based on Robert Panzer [work](https://github.com/robertpanzer/filesystemwatch-connector) ([read the full blog post by Robert here](http://robertpanzer.github.io/blog/2014/inboundra-nointfmdbs.html)).