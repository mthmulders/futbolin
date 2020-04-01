# RabbitMQ

## Deployment

### Inside a Docker container

For convenience, you can create a simple script, say **run-docker.sh**, with the following content:

```sh
#!/usr/bin/env bash

docker rm rmq || true
docker run \
  -p 5672:5672 \
  -p 15672:15672 \
  --name rmq \
  -e RABBITMQ_DEFAULT_USER=rabbit-user \
  -e RABBITMQ_DEFAULT_PASS=... \
  -e RABBITMQ_ERLANG_COOKIE=... \
  mthmulders-docker-futbolin.bintray.io/futbolin-rabbitmq
```

Replace `...` above with your actual values.

### Inside a Kubernetes environment
Prepare a Kubernetes secret with the appropriate configuration values:
```sh
kubectl create secret generic rabbitmq \
    --namespace=futbolin \
    --from-literal=rabbitmq.defaultuser.password=... \
    --from-literal=rabbitmq.erlang.cookie=...
```

Again, replace `...` above with your actual values.