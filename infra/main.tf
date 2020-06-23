terraform {
  required_providers {
    kubernetes = "~> 1.10.0"
    local = "~> 1.4"
    oci = "~> 3.56"
  }
}

variable "user_ocid" {}                # OCID of your tenancy.
variable "tenancy_ocid" {}             # OCID of the user calling the API.
variable "compartment_ocid" {}         # OCID of your Comparment.
variable "fingerprint" {}              # Fingerprint for the key pair being used.
variable "private_key_path" {}         # The path (including filename) of the private key stored on your computer.
variable "private_key_password" {}     # Passphrase used for the key, if it is encrypted.
variable "region" {}                   # An Oracle Cloud Infrastructure region.
variable "project_compartment_ocid" {} # Comparment inside Oracle Cloud where all resources will be created.
variable "ssh_public_key" {}           # Public SSH key for remote access to the instances in the Node Pool.

variable "kubernetes_version" { default = "v1.16.8" }
variable "oci_services_region" { default = "fra" }

# Configure the Oracle Cloud Infrastructure provider
provider "oci" {
  tenancy_ocid         = var.tenancy_ocid
  user_ocid            = var.user_ocid
  fingerprint          = var.fingerprint
  private_key_path     = var.private_key_path
  private_key_password = var.private_key_password
  region               = var.region
}

# Following https://www.terraform.io/docs/providers/oci/guides/best_practices.html
variable "image_ids" {
  type = map(string)
  default = {
    // See https://docs.cloud.oracle.com/iaas/images/
    // Oracle-provided image "Oracle-Linux-7.7-2019.12.18-0"
    eu-frankfurt-1 = "ocid1.image.oc1.eu-frankfurt-1.aaaaaaaalljvzqt3aw7cwpls3oqx7dyrcuntqfj6xn3a2ul3jiuby27lqdxa"
  }
}

variable "node_shape" {
  default = "VM.Standard.E2.1"
}

output "Kubernetes-API-Server-Endpoint" {
  value = oci_containerengine_cluster.futbolin.endpoints
}

data "oci_containerengine_cluster_kube_config" "futbolin" {
  cluster_id = oci_containerengine_cluster.futbolin.id
}

data "oci_core_services" "futbolin" {
}

resource "local_file" "kubeconfig" {
  content  = data.oci_containerengine_cluster_kube_config.futbolin.content
  filename = "k8s/config"
}
