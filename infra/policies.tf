resource "oci_identity_policy" "oke-required-policies" {
  compartment_id = var.compartment_ocid
  description    = "Allow OKE service to manage required resources"
  name           = "oke-required-policies"
  statements = [
    "allow service OKE to manage all-resources in tenancy"
  ]
}

resource "oci_identity_policy" "buf-hackathon-users" {
  compartment_id = var.compartment_ocid
  description    = "Allow  members to access required resources"
  name           = "futbolin-users"
  statements = [
    "allow group ${oci_identity_group.futbolin.name} to manage all-resources in compartment ${oci_identity_compartment.futbolin.name}"
  ]
}