#!/bin/sh

export KUBECONFIG=../infra/k8s/config
echo Fetching oke-admin secret...
secret=$(kubectl -n kube-system get secret | grep oke-admin | awk '{print $1}')
echo Fetching $secret
token=$(kubectl -n kube-system describe secret $secret | grep "token:" | cut -d ':' -f 2 | tr -d ' ')

echo Kubernetes Dashboard token: $token

echo You might want to start kubectl proxy
echo and navigate to http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/#!/login