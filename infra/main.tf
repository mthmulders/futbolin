terraform {
  required_providers {
    local = {
      source  = "hashicorp/local"
      version = "~> 1.4"
    }
    oci = {
      source  = "hashicorp/oci"
      version = "~> 4.27.0"
    }
  }
  required_version = ">= 0.14"
}

variable "user_ocid" {}            # OCID of your tenancy.
variable "tenancy_ocid" {}         # OCID of the user calling the API.
variable "compartment_ocid" {}     # OCID of your Comparment.
variable "fingerprint" {}          # Fingerprint for the key pair being used.
variable "private_key_path" {}     # The path (including filename) of the private key stored on your computer.
variable "private_key_password" {} # Passphrase used for the key, if it is encrypted.
variable "region" {}               # An Oracle Cloud Infrastructure region.
variable "ssh_public_key_path" {}  # Path to public SSH key for remote access to the instances in the Node Pool.

variable "kubernetes_version" { default = "v1.19.7" }
variable "oci_services_region" { default = "fra" }
variable "oci_vcn_name" { default = "Futbolín Virtual Network" }

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
    // Oracle-provided image "Oracle-Linux-7.9-2021.04.09-0"
    eu-frankfurt-1 = "ocid1.image.oc1.eu-frankfurt-1.aaaaaaaa4pnql6wprjir2xhf2is2r7u2g7e4s3lufxxjp6hzm3tyepannwra"
  }
}

variable "node_shape" {
  default = "VM.Standard.E2.1"
}

data "oci_core_services" "futbolin" {
}
