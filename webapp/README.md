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

### On your machine
For convenience, you can create a simple script, say **run.sh**, with the following content:

```sh
#!/usr/bin/env bash

export OIDC_CLIENT_ID=...
export OIDC_CLIENT_SECRET=...
mvn generate-resources liberty:dev
```

Replace `...` above with your actual values.
The ones not listed above have sane defaults defined in the POM.

Every change you make to the application will immediately be reloaded by OpenLiberty.

## Deployment

### Inside a Docker container

For convenience, you can create a simple script, say **run-docker.sh**, with the following content:

```sh
#!/usr/bin/env bash

docker rm wa || true
docker run \
  -p 9080:9080 \
  -p 9443:9443 \
  --name wa \
  -e OIDC_CLIENT_ID=... \
  -e OIDC_CLIENT_SECRET=... \
  -e CLIENT_KEYSTORE_FILE=/tmp/keystore.p12 \
  -e CLIENT_KEYSTORE_PASSWORD=b4kBMqf6Y4pV \
  mthmulders-docker-futbolin.bintray.io/futbolin-webapp
```

Replace `...` above with your actual values.

Changes to the code base are **not** automatically reloaded.
You need to run `mvn package -Pdocker` and restart the above script.

### Inside a Kubernetes environment
Prepare a Kubernetes secret with the appropriate configuration values:
```sh
kubectl create secret generic webapp-oidc \
    --namespace=futbolin \
    --from-literal=oidc.client.id=... \
    --from-literal=oidc.client.secret=... \
    --from-literal=oidc.keystore.password=b4kBMqf6Y4pV
```

Again, replace `...` above with your actual values.