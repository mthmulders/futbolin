resource "oci_identity_group" "futbolin" {
  compartment_id = var.compartment_ocid
  description    = "Futbolín"
  name           = "futbolin"
}

resource "oci_identity_compartment" "futbolin" {
  compartment_id = var.compartment_ocid
  description    = "Futbolín"
  name           = "futbolin"
}