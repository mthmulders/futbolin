#!/usr/bin/env bash

if [[ "$#" -ne 1 ]]; then
    echo "$0 <num>"
    echo ""
    echo "Parameters:"
    echo "  <num> the version of the container being built"
    exit
fi

docker build -f Dockerfile -t futbolin.azurecr.io/demo-page:$1 .
docker push futbolin.azurecr.io/demo-page:$1