resource "oci_containerengine_cluster" "futbolin" {
  compartment_id     = var.project_compartment_ocid
  kubernetes_version = var.kubernetes_version
  name               = "Futbolín Cluster"
  vcn_id             = oci_core_vcn.futbolin.id

  options {
    add_ons {
      is_kubernetes_dashboard_enabled = true
      is_tiller_enabled               = true
    }

    service_lb_subnet_ids = [
      oci_core_subnet.futbolin-loadbalancer-subnet.id
    ]
  }
}

resource "oci_containerengine_node_pool" "futbolin-pool-0" {
  cluster_id         = oci_containerengine_cluster.futbolin.id
  compartment_id     = var.project_compartment_ocid
  kubernetes_version = var.kubernetes_version
  name               = "Futbolín Node Pool 0"
  ssh_public_key     = var.ssh_public_key
  node_shape         = var.node_shape
  node_source_details {
    source_type = "image"
    image_id    = var.image_ids[var.region]
  }
  node_config_details {
    placement_configs {
      availability_domain = "MoMM:EU-FRANKFURT-1-AD-1"
      subnet_id           = oci_core_subnet.futbolin-worker-subnet.id
    }
    size = 1
  }
}

resource "oci_containerengine_node_pool" "futbolin-pool-1" {
  cluster_id         = oci_containerengine_cluster.futbolin.id
  compartment_id     = var.project_compartment_ocid
  kubernetes_version = var.kubernetes_version
  name               = "Futbolín Node Pool 1"
  ssh_public_key     = var.ssh_public_key
  node_shape         = var.node_shape
  node_source_details {
    source_type = "image"
    image_id    = var.image_ids[var.region]
  }
  node_config_details {
    placement_configs {
      availability_domain = "MoMM:EU-FRANKFURT-1-AD-2"
      subnet_id           = oci_core_subnet.futbolin-worker-subnet.id
    }
    size = 1
  }
}

/*
apiVersion: v1
kind: ServiceAccount
metadata:
  name: oke-admin
  namespace: kube-system
*/
resource "kubernetes_service_account" "oke-admin" {
  metadata {
    name = "oke-admin"
    namespace = "kube-system"
  }
}

/*
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: ClusterRoleBinding
metadata:
  name: oke-admin
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
  - kind: ServiceAccount
    name: oke-admin
    namespace: kube-system
*/
resource "kubernetes_cluster_role_binding" "oke-admin" {
  metadata {
    name = "oke-admin"
  }
  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind      = "ClusterRole"
    name      = "cluster-admin"
  }
  subject {
    kind      = "ServiceAccount"
    name      = "oke-admin"
    namespace = "kube-system"
  }
}
