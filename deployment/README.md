# Deployment

## Prerequisites
1. You have an [Oracle Cloud Infrastructure Container Engine for Kubernetes](https://docs.cloud.oracle.com/en-us/iaas/Content/ContEng/Concepts/contengoverview.htm) ready for use.
Make sure that Tiller is enabled in the cluster (see the [../infra folder](../infra) for details).
1. You have [Helm](https://docs.helm.sh/using_helm/#installing-helm) installed.

## Instructions
Following [this guide](https://medium.com/oracledevs/secure-your-kubernetes-services-using-cert-manager-nginx-ingress-and-lets-encrypt-888c8b996260).

* Make sure to use the right Kubernetes context by running `. ./env.sh`.
* Verify that Tiller is running with `kubectl get pods --namespace kube-system | grep tiller`.
You should see one pod.
* Create the required namespaces with `kubectl apply -f namespaces.yml`.
* Add the Helm repository that contains cert-manager with `helm repo add jetstack https://charts.jetstack.io`.
* Add the Helm repository that contains nginx with `helm repo add stable https://kubernetes-charts.storage.googleapis.com/`.
* Update the Helm repositories with `helm repo update`.
* Install [cert-manager](https://cert-manager.readthedocs.io/en/latest/index.html) with
```sh
helm install cert-manager \
  --namespace cert-manager \
  --set ingressShim.defaultIssuerName=letsencrypt-staging \
  --set ingressShim.defaultIssuerKind=ClusterIssuer \
  --set webhook.enabled=false \
  --version v0.13 \
  jetstack/cert-manager
```
* Install the Custom Resource Definitions for cert-manager with `kubectl apply --validate=false -f https://raw.githubusercontent.com/jetstack/cert-manager/v0.14.0/deploy/manifests/00-crds.yaml`
* Create the Let's Encrypt _Staging_ issuer with `kubectl apply -f letsencrypt-staging.yml`.
This issuer is used to verify everything works.
If it does, we can create a new issuer for obtaining _real_ certificates with `kubectl apply -f letsencrypt-prod.yml`.
* Install the nginx Ingress with
```sh
helm install nginx \
  --set rbac.create=true \
  --namespace kube-system \
  stable/nginx-ingress
```

### Updating cert-manager
* Ensure the local Helm chart repository cache is up to date with `helm repo update`
* Upgrade cert-manager with the following command (replace with appropriate version number):
```helm upgrade cert-manager \
  --namespace cert-manager \
  --version v0.14 \
  jetstack/cert-manager```
