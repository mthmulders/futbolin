# Build and deployment
The CircleCI workflow expects the following environment variables to be present:

| Name | Example value | Description |
| --- | --- | --- |
| AZ_SP_ID | 00000000-0000-0000-0000-000000000000| The _AppId_ for the service principal that you want to use. |
| AZ_SP_PASSWORD | super-secure-password | The password for the service principal that you want to use. |
| AZ_SP_URL | http://whatever-you-think-of | The _Name_ for the service principal that you want to use. |
| AZ_SP_TENANT | 00000000-0000-0000-0000-000000000000 | |
| AZ_RESOURCE_GROUP | MyResourceGroup | |
| AZ_AKS_NAME | MyKubernetesCluster | |
| AZ_ACR_NAME | mycontainerregistry.azurecr.io | |
 
Here, the variables starting with `AZ_SP_` refer to a [_Service Principal_](https://docs.microsoft.com/en-us/azure/container-registry/container-registry-authentication#service-principal) that you must create.

