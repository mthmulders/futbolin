# Uses https://registry.terraform.io/modules/oracle-terraform-modules/oke/oci/latest

module "oke" {
  source  = "oracle-terraform-modules/oke/oci"
  version = "3.2.0"

  compartment_id       = oci_identity_compartment.futbolin.id
  tenancy_id           = var.tenancy_ocid
  user_id              = var.user_ocid
  api_fingerprint      = var.fingerprint
  api_private_key_path = var.private_key_path

  use_signed_images  = false
  image_signing_keys = []

  region             = var.region
  cluster_name       = "Futbolín"
  use_encryption     = false
  existing_key_id    = ""
  bastion_enabled    = false
  operator_enabled   = false
  dashboard_enabled  = true
  kubernetes_version = var.kubernetes_version

  vcn_dns_label = "buf"
  vcn_name      = var.oci_vcn_name

  check_node_active       = "one"
  allow_worker_ssh_access = false
  ssh_public_key_path     = var.ssh_public_key_path
  node_pool_image_id      = var.image_ids[var.region]
  node_pools = {
    np1 = {
      shape          = "VM.Standard.E2.1",
      node_pool_size = 3
    }
  }


  tags = {
    vcn = {
    }
    bastion = {
    }
    operator = {
    }
  }
}
