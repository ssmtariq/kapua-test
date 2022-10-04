# JWT Security

To leverage JWT security features, an X509 Certificate with the related private key must be loaded in Kapua.

## Use random generated certificate and private key

By default Kapua will look for keys in the path specified by `certificate.jwt.private.key`
and `certificate.jwt.certificate` system properties at startup (see below). Such properties MUST be set, otherwise an
error will be thrown.

In thw Docker deployment a certificate and its private key are dynamically generated in the Docker environment startup.

## Use a custom certificate

If you want to use a custom certificate you can generate it, along with its private key,
with [OpenSSL](https://www.openssl.org/). In order to create those files you can use the following commands:

```bash
openssl req -x509 -newkey rsa:4096 -keyout <path_to_key> -out <path_to_certificate> -days 365 -nodes -subj '/O=Eclipse Kapua/C=XX'
openssl pkcs8 -topk8 -in <path_to_key> -out <path_to_pkcs8_key>
rm <path_to_key>
```

When converting the private key in PKCS8 format you can avoid password encryption by adding the `-nocrypt` switch to
the `openssl pkcs8` command above.

Both the certificate and the private key must be in PKCS8 format. If the private key is password encrypted, you can
specify it with the `certificate.jwt.private.key.password` system property. 
