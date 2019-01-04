#!/usr/bin/env bash

docker build -f Dockerfile -t futbolinregistry.azurecr.io/demo-page .
docker push futbolinregistry.azurecr.io/demo-page