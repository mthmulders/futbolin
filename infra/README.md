# Provisioning infrastructure in Azure
We use [Terraform](https://www.terraform.io/) to provision infrastructure in Azure.
Make sure to have Terraform installed, as well as the [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest).


## Preparation
Terraform needs a _Service Principal_ to interact with Azure on your behalf.
To create one using the Azure CLI, issue the following commands:

```bash
# First, log in.
az login
az account list \
    --output table
# The output contains a SubscriptionID, you need this in the following steps.

# Create the Service Principal for Terraform.
az ad sp create-for-rbac \
    --role "Contributor" \
    --scopes "/subscriptions/00000000-0000-0000-0000-000000000000" \
    --output table
```

Next, create a file, say `env.sh`.

* The _SubscriptionId_ comes from the first table.
* The _AppId_, _Password_ and _Tenant_ come from the second table.

```bash
#!/usr/bin/env bash

export TF_VAR_client_id=00000000-0000-0000-0000-000000000000
export TF_VAR_client_secret=Generated password
export TF_VAR_subscription_id=00000000-0000-0000-0000-000000000000
export TF_VAR_tenant_id=00000000-0000-0000-0000-000000000000

#
# Also, think of the following parameters
#

# Name of the Azure Resource Group where all components are provisioned.
export TF_VAR_resource_group_name=...
# Name of the Azure Container Registry where all containers will be stored.
export TF_VAR_registry_name=...
# Name of the Azure Kubernetes Cluster where all containers will run.
export TF_VAR_cluster_name=...
# DNS prefix of the AKS
export TF_VAR_dns_prefix=...
```

## Provision the Azure Container Registry (ACR)
Source the environment file (using `. /path/to/env.sh`) to load the Azure SP credentials.

Now you can initialize Terraform using `terraform init`.
Preview the changes using `terraform plan --target azurerm_container_registry.acr`.
Finally, execute them using `terraform apply --target azurerm_container_registry.acr`.

In the output, look for

    Apply complete! Resources: 2 added, 0 changed, 0 destroyed.
    
    Outputs:
    
    Azure Container Registry = ...
    Resource Group = ...
    
This indicates the ACR was provisioned successfully.

## Provision the Azure Kubernetes Service (AKS)
Create a new Service Principal for the Azure Kubernetes Service (AKS).
```bash
# Create the Service Principal for AKS
az ad sp create-for-rbac \
    --skip-assignment \
    --output table
# Assign this service principal the ro
az role assignment create \
    --assignee 00000000-0000-0000-0000-000000000000 \
    --role acrpull \
    --scope ...
# Here, the scope is something like
# /subscriptions/.../resourceGroups/.../providers/Microsoft.ContainerRegistry/registries/...

```

Append `env.sh` that you have created before by adding a few more lines:

```bash
# Read these from the output of the previous step
export TF_VAR_aks_sp_id=00000000-0000-0000-0000-000000000000
export TF_VAR_aks_sp_secret=Generated password
```

Now it is time to provision the AKS cluster.
Re-load `env.sh` (using `. /path/to/env.sh`).
Preview the changes using `terraform plan`.
Finally, execute them using `terraform apply`.
This may take a few minutes.
Again, the output should end with 

    Apply complete! Resources: 1 added, 0 changed, 0 destroyed.
    
    Outputs:
    
    Azure Container Registry = ...
    Resource Group = ...
    
This indicates the AKS was also provisioned successfully.

## Accessing the cluster

### Using `kubectl`
If you want to use `kubectl` to interact with the cluster, issue

```bash
az aks get-credentials \
    --resource-group ... \
    --name ...
```

If you also work with Futbolin on your own machine, you need a local Kubernetes cluster (e.g. using [Minikube](https://kubernetes.io/docs/setup/minikube/)).
To interact with that, you also use `kubectl`.
To switch between a remotely running Kubernetes cluster and locally running one, you can define _contexts_.
Using `kubectl config use-context <name-of-context>` to determine which cluster you are interacting with.

### Using the Kubernetes Web UI Dashboard

```bash
az aks browse \
    --resource-group ...\
    --name ...
```

