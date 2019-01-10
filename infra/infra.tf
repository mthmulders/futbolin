# AppId for the Service Principal that Terraform uses
variable "client_id" {}

# Password for the Service Principal that Terraform uses
variable "client_secret" {}

# Subscription for the Service Principal that Terraform uses
variable "subscription_id" {}

# TenantID for the Service Principal that Terraform uses
variable "tenant_id" {}

# AppId for the AKS Service Principa
variable "aks_sp_id" {}

# Password for the AKS Service Principal
variable "aks_sp_secret" {}

# Name of the Resource Group where all resources must be created
variable "resource_group_name" {}

# Name of the container registry
variable "registry_name" {}

# Name of the Kubernetes cluster
variable "cluster_name" {}

# DNS prefix for the Kubernetes cluster
variable "dns_prefix" {}

provider "azurerm" {
  # Whilst version is optional, we /strongly recommend/ using it to pin the version of the Provider being used
  version = "=1.20.0"

  subscription_id = "${var.subscription_id}"
  client_id       = "${var.client_id}"
  client_secret   = "${var.client_secret}"
  tenant_id       = "${var.tenant_id}"
}

resource "azurerm_resource_group" "rg" {
  name     = "${var.resource_group_name}"
  location = "West Europe"
}

resource "azurerm_container_registry" "acr" {
  name                = "${var.registry_name}"
  resource_group_name = "${azurerm_resource_group.rg.name}"
  location            = "${azurerm_resource_group.rg.location}"
  sku                 = "Basic"
  admin_enabled       = false
}

output "Resource Group" {
  value = "${azurerm_resource_group.rg.id}"
}

output "Azure Container Registry" {
  value = "${azurerm_container_registry.acr.id}"
}

resource "azurerm_kubernetes_cluster" "aks" {
  name                = "${var.cluster_name}"
  location            = "${azurerm_resource_group.rg.location}"
  resource_group_name = "${azurerm_resource_group.rg.name}"
  dns_prefix          = "${var.dns_prefix}"

  agent_pool_profile {
    name            = "default"
    count           = 1
    vm_size         = "Standard_D1_v2"
    os_type         = "Linux"
    os_disk_size_gb = 30
  }

  service_principal {
    client_id     = "${var.aks_sp_id}"
    client_secret = "${var.aks_sp_secret}"
  }

  addon_profile {
    http_application_routing {
      enabled                            = true
    }
  }
}
