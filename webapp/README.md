# Web Application

This module contains the user-facing web application for Futbolín.

## Configuration

You can configure the web application using environment variables.

This are the supported configuration settings.

| Environment variable name | Description |
| --- | --- |
| `CLIENT_KEYSTORE_FILE` | Password to the aforementioned file. |
| `CLIENT_KEYSTORE_PASSWORD` | Path to the keystore (generated during build) with trusted certificates for interacting with other systems. |
| `OIDC_CLIENT_ID` | Client identifier for the OpenID Connect integration |
| `OIDC_CLIENT_SECRET` | Client secret for the OpenID Connect integration |
| `OIDC_RPHOSTANDPORT` | Host and port (`localhost:9443`) for constructing the OpenID Connect redirect URL.

## Development

For convenience, you can create a simple script, say **run.sh**, with the following content:

```sh
#!/usr/bin/env bash

export OIDC_CLIENT_ID=...
export OIDC_CLIENT_SECRET=...
mvn generate-resources liberty:dev
```

Replace `...` above with your actual values.
The ones not listed above have sane defaults defined in the POM.