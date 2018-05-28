# Java EE 7 Samples: JAX-RPC 1.1 #

The [JSR 101](https://jcp.org/en/jsr/detail?id=101) specification is the old generation web services API predating JAX-WS, which in fact was
to become JAX-RPC 2.0.

JAX-RPC 1.x is **pruned** from Java EE, and **should not be used** anymore. This sample is only provided for historical purposes.

## Samples ##

 - **jaxrpc-endpoint** - *Defines a very basic hello endpoint (as all classical JAX-RPC examples did), generates the required .wsdl and mapping files, deploys the service, and calls it via two client side approaches: dynamic proxy and DII.*
 - **jaxrpc-security** - *Like `jaxrpc-endpoint`, but the service is protected and requires SOAP message level authentication via an encrypted username/password credential in the security header, and calls it via generated Stubs. (to keep the sample somewhat restricted in size does not sign the message)*

## How to run

More information on how to run can be found at: <https://github.com/javaee-samples/javaee7-samples#how-to-run->


