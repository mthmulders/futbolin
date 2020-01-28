#
# The network topology follows Example 3 described at
# https://docs.cloud.oracle.com/iaas/Content/ContEng/Concepts/contengnetworkconfigexample.htm
# It creates a Highly Available Public Cluster in a Region with Three Availability Domains, Using a Regional Subnet.
#

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

# Route traffic to the Internet over the Internet Gateway
resource "oci_core_route_table" "futbolin-internet-routing" {
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.futbolin.id
  display_name   = "Internet Route Table"
  route_rules {
    network_entity_id = oci_core_internet_gateway.futbolin.id
    destination       = "0.0.0.0/0"
  }
}

# Route traffic to Oracle Cloud services over the Service Gateway
resource "oci_core_route_table" "futbolin-services-routing" {
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.futbolin.id
  display_name   = "Services Route Table"
  route_rules {
    destination_type  = "SERVICE_CIDR_BLOCK"
    destination       = "all-${var.oci_services_region}-services-in-oracle-services-network"
    network_entity_id = oci_core_service_gateway.futbolin.id
  }
}

# Configure a regional subnet for worker nodes
resource "oci_core_subnet" "futbolin-worker-subnet" {
  cidr_block     = "10.0.10.0/24"
  display_name   = "Futbolín Worker Subnet"
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.futbolin.id
  route_table_id = oci_core_route_table.futbolin-services-routing.id
  security_list_ids = [
    oci_core_security_list.futbolin-workers.id
  ]
}

# Configure a regional subnet for load balancers
resource "oci_core_subnet" "futbolin-loadbalancer-subnet" {
  cidr_block     = "10.0.20.0/24"
  display_name   = "Futbolín Load Balancer Subnet"
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.futbolin.id
  route_table_id = oci_core_route_table.futbolin-internet-routing.id
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

  # Stateless ingress rules to enable intra-VCN traffic
  ingress_security_rules {
    protocol  = "6" # TCP
    source    = "10.0.10.0/24"
    stateless = true
  }

  # This rule enables worker nodes to receive Path MTU Discovery fragmentation messages.
  ingress_security_rules {
    protocol = "1" # ICMP
    source   = "0.0.0.0/0"
    icmp_options {
      type = 3
      code = 4
    }
  }

  # Stateful ingress rules to allow Container Engine for Kubernetes to access
  # worker nodes on port 22 from the following source CIDR blocks:
  ingress_security_rules {
    protocol = "6" # TCP
    tcp_options {
      min = 22
      max = 22
    }
    source = "130.35.0.0/16"
  }
  ingress_security_rules {
    protocol = "6" # TCP
    tcp_options {
      min = 22
      max = 22
    }
    source = "134.70.0.0/17"
  }
  ingress_security_rules {
    protocol = "6" # TCP
    tcp_options {
      min = 22
      max = 22
    }
    source = "138.1.0.0/16"
  }
  ingress_security_rules {
    protocol = "6" # TCP
    tcp_options {
      min = 22
      max = 22
    }
    source = "140.91.0.0/17"
  }
  ingress_security_rules {
    protocol = "6" # TCP
    tcp_options {
      min = 22
      max = 22
    }
    source = "147.154.0.0/16"
  }
  ingress_security_rules {
    protocol = "6" # TCP
    tcp_options {
      min = 22
      max = 22
    }
    source = "192.29.0.0/16"
  }

  # Optional rule to enable inbound SSH traffic from the internet on port 22 to access worker nodes.
  # ingress_security_rules {
  #   protocol = "6" # TCP
  #   source   = "0.0.0.0/0"
  #   tcp_options {
  #     min = 22
  #     max = 22
  #   }
  # }

  # Optional rule to enable inbound traffic to the worker nodes on the default NodePort range of 30000 to 32767
  ingress_security_rules {
    protocol = "6" # TCP
    source   = "0.0.0.0/0"
    tcp_options {
      min = 30000
      max = 32767
    }
  }

  # -------------- #
  # OUTBOUND RULES #
  # -------------- #

  # Optional rule to enable intra-VCN traffic
  egress_security_rules {
    protocol    = "6" # TCP
    destination = "10.0.10.0/24"
    stateless   = true
  }

  # This rule enables outbound ICMP traffic.
  egress_security_rules {
    protocol         = "1" # ICMP
    destination      = "all-${var.oci_services_region}-services-in-oracle-services-network"
    destination_type = "SERVICE_CIDR_BLOCK"
  }
  # Rules that allow traffic between worker node and Container Engine for Kubernetes.
  egress_security_rules {
    destination      = "all-${var.oci_services_region}-services-in-oracle-services-network"
    destination_type = "SERVICE_CIDR_BLOCK"
    protocol         = "6" # TCP
    tcp_options {
      min = 80
      max = 80
    }
  }
  egress_security_rules {
    destination      = "all-${var.oci_services_region}-services-in-oracle-services-network"
    destination_type = "SERVICE_CIDR_BLOCK"
    protocol         = "6" # TCP
    tcp_options {
      min = 443
      max = 443
    }
  }
  egress_security_rules {
    destination      = "all-${var.oci_services_region}-services-in-oracle-services-network"
    destination_type = "SERVICE_CIDR_BLOCK"
    protocol         = "6" # TCP
    tcp_options {
      min = 6443
      max = 6443
    }
  }
  egress_security_rules {
    destination      = "all-${var.oci_services_region}-services-in-oracle-services-network"
    destination_type = "SERVICE_CIDR_BLOCK"
    protocol         = "6" # TCP
    tcp_options {
      min = 12250
      max = 12250
    }
  }

  # Stateless ingress and egress rules that allow all traffic between
  # worker node subnets and load balancer subnets (if specified).
  //  egress_security_rules {
  //    protocol    = "all"
  //    destination = oci_core_subnet.futbolin-loadbalancer-subnet.cidr_block
  //  }
  //  ingress_security_rules {
  //    protocol = "all"
  //    source   = oci_core_subnet.futbolin-loadbalancer-subnet.cidr_block
  //  }

}

# Configure a security list for the Load Balancer subnet
resource "oci_core_security_list" "futbolin-loadbalancers" {
  display_name   = "Security List for Load Balancers"
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.futbolin.id

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
