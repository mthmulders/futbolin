#!/usr/bin/env bash
set -euo pipefail

MYSQL_PASSWORD=$(cat `pwd`/terraform.tfvars | grep database_admin_password | cut -d '=' -f 2 | tr -d '"' | tr -d '[:blank:]')
KUBECONFIG=`pwd`/generated/kubeconfig
MYSQL_IP=$(terraform output | grep "MySQL-IP" | cut -d '=' -f 2 | tr -d '"' | tr -d '[:blank:]')

kubectl run -it --rm=true --restart=Never \
    --image=mysql:8.0.23 mysql-client \
    -- mysql -u admin -h ${MYSQL_IP} -p${MYSQL_PASSWORD}

