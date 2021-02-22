resource "oci_mysql_mysql_db_system" "futbolin-database" {
  admin_password          = var.database_admin_password
  admin_username          = "admin"
  compartment_id          = oci_identity_compartment.futbolin.id
  shape_name              = "MySQL.VM.Standard.E3.1.8GB" # 1 CPU, 8GB memory
  subnet_id               = data.oci_core_subnets.workers_subnet.subnets[0].id
  data_storage_size_in_gb = "50"
  display_name            = "Futbolín Database"
  availability_domain     = "MoMM:EU-FRANKFURT-1-AD-1"
}

data "oci_core_subnets" "workers_subnet" {
  compartment_id = oci_identity_compartment.futbolin.id
  display_name   = "workers"
}

output "MySQL-IP" {
  value = oci_mysql_mysql_db_system.futbolin-database.endpoints[0].ip_address
}