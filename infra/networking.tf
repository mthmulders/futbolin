#
# The network topology follows Example 3 described at
# https://docs.cloud.oracle.com/iaas/Content/ContEng/Concepts/contengnetworkconfigexample.htm
# It creates a Highly Available Public Cluster in a Region with Three Availability Domains, Using a Regional Subnet.
#

variable "workers-cidr" { default = "10.0.10.0/24" }
variable "loadbalancers-cidr" { default = "10.0.20.0/24" }

# Configure the VCN for the Kubernetes worker nodes
resource "oci_core_vcn" "futbolin" {
  cidr_block     = "10.0.0.0/16"
  compartment_id = var.project_compartment_ocid
  display_name   = "Futbolín Virtual Network"
  dns_label      = "futbolin"
}

# Configure an outbound Internet Gateway
resource "oci_core_internet_gateway" "futbolin" {
  compartment_id = var.project_compartment_ocid
  display_name   = "Futbolín Internet Gateway"
  vcn_id         = oci_core_vcn.futbolin.id
}

# Configure a Service Gateway for all services in our region
resource "oci_core_service_gateway" "futbolin" {
  compartment_id = var.project_compartment_ocid
  display_name   = "Futbolín Service Gateway"
  vcn_id         = oci_core_vcn.futbolin.id
  services {
    service_id = data.oci_core_services.futbolin.services.0.id
  }
}

# Configure a NAT Gateway 
resource "oci_core_nat_gateway" "futbolin" {
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.futbolin.id
  display_name   = "Futbolín NAT Gateway"
}

# Route traffic to the Internet over the Internet Gateway
resource "oci_core_route_table" "loadbalancer-routing" {
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.futbolin.id
  display_name   = "Load Balancers Routing Table"
  route_rules {
    network_entity_id = oci_core_internet_gateway.futbolin.id
    destination       = "0.0.0.0/0"
  }
}

# Route traffic to Oracle Cloud services over the Service Gateway
resource "oci_core_route_table" "worker-routing" {
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.futbolin.id
  display_name   = "Workers Routing Table"
  route_rules {
    network_entity_id = oci_core_nat_gateway.futbolin.id
    destination       = "0.0.0.0/0"
  }
}

# Configure a regional subnet for worker nodes
resource "oci_core_subnet" "futbolin-worker-subnet" {
  cidr_block                 = var.workers-cidr
  display_name               = "Futbolín Worker Subnet"
  compartment_id             = var.project_compartment_ocid
  vcn_id                     = oci_core_vcn.futbolin.id
  route_table_id             = oci_core_route_table.worker-routing.id
  prohibit_public_ip_on_vnic = true
  security_list_ids = [
    oci_core_security_list.futbolin-workers.id
  ]
}

# Configure a regional subnet for load balancers
resource "oci_core_subnet" "futbolin-loadbalancer-subnet" {
  cidr_block     = var.loadbalancers-cidr
  display_name   = "Futbolín Load Balancer Subnet"
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.futbolin.id
  route_table_id = oci_core_route_table.loadbalancer-routing.id
  security_list_ids = [
    oci_core_security_list.futbolin-loadbalancers.id
  ]
}

# Configure a security list for the Workers subnet
resource "oci_core_security_list" "futbolin-workers" {
  display_name   = "Security List for Worker nodes"
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.futbolin.id

  # ------------- #
  # INBOUND RULES #
  # ------------- #

  ingress_security_rules {
    protocol  = "all"
    source    = "10.0.10.0/24"
    stateless = true
  }
  ingress_security_rules {
    protocol  = "all"
    source    = "10.0.11.0/24"
    stateless = true
  }
  ingress_security_rules {
    protocol  = "all"
    source    = "10.0.12.0/24"
    stateless = true
  }

  ingress_security_rules {
    protocol  = "6" # TCP
    source    = "10.0.0.0/16"
    stateless = false
    tcp_options {
      min = 22
      max = 22
    }
  }

  # -------------- #
  # OUTBOUND RULES #
  # -------------- #

  egress_security_rules {
    protocol    = "all"
    destination = "10.0.10.0/24"
    stateless   = true
  }
  egress_security_rules {
    protocol    = "all"
    destination = "10.0.11.0/24"
    stateless   = true
  }
  egress_security_rules {
    protocol    = "all"
    destination = "10.0.12.0/24"
    stateless   = true
  }

  egress_security_rules {
    protocol    = "all"
    destination = "0.0.0.0/0"
  }
}

# Configure a security list for the Load Balancer subnet
resource "oci_core_security_list" "futbolin-loadbalancers" {
  display_name   = "Security List for Load Balancers"
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.futbolin.id

  # Stateless ingress and egress rules that allow all traffic between worker node subnets and load balancer subnets.
  egress_security_rules {
    destination = var.workers-cidr
    protocol    = "6" # TCP
    stateless   = true
  }
  ingress_security_rules {
    source    = var.workers-cidr
    protocol  = "6" # TCP
    stateless = true
  }

  # This rule enables incoming public traffic to service load balancers.
  ingress_security_rules {
    protocol  = "6" # TCP
    source    = "0.0.0.0/0"
    stateless = true
  }
  # This rule enables responses from a web application through the service load balancers.
  egress_security_rules {
    protocol    = "6" # TCP
    destination = "0.0.0.0/0"
    stateless   = true
  }
}
