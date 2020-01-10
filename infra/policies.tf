resource "oci_identity_policy" "oke-required-policies" {
  compartment_id = var.compartment_ocid
  description    = "Allow OKE service to manage required resources"
  name           = "oke-required-policies"
  statements = [
    "allow service OKE to manage all-resources in tenancy"
  ]
}