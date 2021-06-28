#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_setup_dbms.sh: Setup database files or a Docker container.
#
# ------------------------------------------------------------------------------

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DBSeeder - setting up the DBMS."
echo "--------------------------------------------------------------------------------"
echo "DBMS_DB                   : ${DB_SEEDER_DBMS_DB}"
echo "DBMS_EMBEDDED             : ${DB_SEEDER_DBMS_EMBEDDED}"
echo "DBMS_TRINO                : ${DB_SEEDER_DBMS_TRINO}"
echo "VERSION                   : ${DB_SEEDER_VERSION}"
echo --------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_EMBEDDED}" = "no" ]; then
    echo "Docker stop/rm db_seeder_db ................................ before:"
    docker ps -a
    docker ps | grep "db_seeder_db" && docker stop db_seeder_db
    docker ps -a | grep "db_seeder_db" && docker rm --force db_seeder_db
    echo "............................................................. after:"
    docker ps -a
fi

if [ "${DB_SEEDER_DBMS_EMBEDDED}" = "yes" ] ||
   [ "${DB_SEEDER_DBMS_DB}" = "derby" ] ||
   [ "${DB_SEEDER_DBMS_DB}" = "h2" ] ||
   [ "${DB_SEEDER_DBMS_DB}" = "ibmdb2" ]; then
    ( ./scripts/run_db_seeder_setup_files.sh "${DB_SEEDER_DBMS_DB}" )
fi

start=$(date +%s)

# ------------------------------------------------------------------------------
# AgensGraph                         https://hub.docker.com/r/bitnine/agensgraph
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "agens" ]; then
    echo "AgensGraph."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (AgensGraph ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create --name    db_seeder_db \
                  --network db_seeder_net \
                  -e        POSTGRES_PASSWORD=agens \
                  -e        POSTGRES_USER=agens \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}" \
                  -t \
                  bitnine/agensgraph:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (AgensGraph ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 30

    end=$(date +%s)
    echo "DOCKER AgensGraph was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# CockroachDB                     https://hub.docker.com/r/cockroachdb/cockroach
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "cockroach" ]; then
    echo "CockroachDB."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (CockroachDB ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create --name     db_seeder_db \
                  --net      db_seeder_net \
                  -p         "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}" \
                  cockroachdb/cockroach:"${DB_SEEDER_VERSION}" \
                  start --insecure --join=db_seeder_db

    echo "Docker start db_seeder_db (CockroachDB ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 30

    if ! docker exec -it db_seeder_db ./cockroach init --insecure; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER CockroachDB was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# CrateDB                                         https://hub.docker.com/_/crate
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "cratedb" ]; then
    echo "CrateDB."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (CrateDB ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create --env     CRATE_HEAP_SIZE=2g \
                  --name    db_seeder_db \
                  --network db_seeder_net \
                   -p       "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp crate:"${DB_SEEDER_VERSION}" \
                   crate -Cnetwork.host=_site_ -Cdiscovery.type=single-node

    echo "Docker start db_seeder_db (CrateDB ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 30

    end=$(date +%s)
    echo "DOCKER CrateDB was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# CUBRID                                  https://hub.docker.com/r/cubrid/cubrid
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "cubrid" ]; then
    echo "CUBRID."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (CUBRID ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create -e        CUBRID_DB="${DB_SEEDER_DATABASE}" \
                  --name    db_seeder_db \
                  --network db_seeder_net \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
                  cubrid/cubrid:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (CUBRID ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 30

    end=$(date +%s)
    echo "DOCKER CUBRID was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Apache Derby
#           https://hub.docker.com/repository/docker/konnexionsgmbh/apache_derby
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "derby" ]; then
    rm -rf tmp/derby_kxn_db ¦¦ sudo rm -rf tmp/derby_kxn_db
    echo "Apache Derby"
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (Apache Derby ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create --name    db_seeder_db \
                  --network db_seeder_net \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
                  konnexionsgmbh/apache_derby:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (Apache Derby ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "$(docker inspect -f {{.State.Health.Status}} db_seeder_db)" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 30; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER Apache Derby was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Exasol                               https://hub.docker.com/r/exasol/docker-db
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "exasol" ]; then
    echo "Exasol."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (Exasol ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker run --detach \
               --name       db_seeder_db \
               --network    db_seeder_net \
               -p           127.0.0.1:"${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
               --privileged \
               exasol/docker-db:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (Exasol ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 120

    end=$(date +%s)
    echo "DOCKER Exasol was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Firebird                        https://hub.docker.com/r/jacobalberty/firebird
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "firebird" ]; then
    echo "Firebird."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (Firebird ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create -e        FIREBIRD_DATABASE="${DB_SEEDER_DATABASE}" \
                  -e        ISC_PASSWORD=firebird \
                  --name    db_seeder_db \
                  --network db_seeder_net \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
                  jacobalberty/firebird:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (Firebird ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "$(docker inspect -f {{.State.Health.Status}} db_seeder_db)" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 30; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER Firebird was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# H2 Database Engine
#     https://hub.docker.com/repository/docker/konnexionsgmbh/h2_database_engine
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "h2" ]; then
    rm -rf tmp/h2_kxn_db.mv.db ¦¦ sudo rm -rf tmp/h2_kxn_db.mv.db
    echo "H2 Database Engine"
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (H2 Database Engine ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create --name    db_seeder_db \
                  --network db_seeder_net \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
                  konnexionsgmbh/h2_database_engine:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (H2 Database Engine ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "$(docker inspect -f {{.State.Health.Status}} db_seeder_db)" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 30; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER H2 Database Engine was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# HSQLDB
#      https://hub.docker.com/repository/docker/konnexionsgmbh/hypersql_database
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "hsqldb" ]; then
    echo "HSQLDB"
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (HSQLDB ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create --name    db_seeder_db \
                  --network db_seeder_net \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
                  konnexionsgmbh/hypersql_database:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (HSQLDB ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "$(docker inspect -f {{.State.Health.Status}} db_seeder_db)" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 30; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER HSQLDB was ready in $((end - start)) seconds"
fi

## -----------------------------------------------------------------------------
# IBM Db2 Database                           https://hub.docker.com/r/ibmcom/db2
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "ibmdb2" ]; then
    echo "IBM Db2 Database."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (IBM Db2 ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create -e           DBNAME="${DB_SEEDER_DATABASE}" \
                  -e           DB2INST1_PASSWORD=ibmdb2 \
                  -e           LICENSE=accept \
                  --name       db_seeder_db \
                  --network    db_seeder_net \
                  -p           "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}" \
                  --privileged=true \
                  ibmcom/db2:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (IBM Db2 ${DB_SEEDER_VERSION})"
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 300

    end=$(date +%s)
    echo "DOCKER IBM Db2 Database was ready in $((end - start)) seconds"
fi

## -----------------------------------------------------------------------------
# IBM Informix       https://hub.docker.com/r/ibmcom/informix-developer-database
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "informix" ]; then
    echo "IBM Informix."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (IBM Informix ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create -e           DB_INIT=1 \
                  -e           LICENSE=accept \
                  --name       db_seeder_db \
                  --network    db_seeder_net \
                  -p           "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}" \
                  --privileged \
                  ibmcom/informix-developer-database:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (IBM Informix ${DB_SEEDER_VERSION})"
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "$(docker inspect -f {{.State.Health.Status}} db_seeder_db)" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 30; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    docker exec -i db_seeder_db bash < scripts/run_db_seeder_setup_informix.input

    end=$(date +%s)
    echo "DOCKER IBM Informix was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# MariaDB Server                                https://hub.docker.com/_/mariadb
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "mariadb" ]; then
    echo "MariaDB Server."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (MariaDB Server ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create -e        MYSQL_ROOT_PASSWORD=mariadb \
                  --name    db_seeder_db \
                  --network db_seeder_net \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
                  mariadb:"${DB_SEEDER_VERSION}" \
                  --character-set-server=${DB_SEEDER_CHARACTER_SET_SERVER} \
                  --collation-server=${DB_SEEDER_COLLATION_SERVER}
                  
    echo "Docker start db_seeder_db (MariaDB Server ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 30

    end=$(date +%s)
    echo "DOCKER MariaDB Server was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Mimer SQL                     https://hub.docker.com/r/mimersql/mimersql_v11.0
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "mimer" ]; then
    echo "Mimer SQL."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (Mimer SQL ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create -e        MIMER_SYSADM_PASSWORD=mimer \
                  --name    db_seeder_db \
                  --network db_seeder_net \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
                  mimersql/mimersql_v11.0:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (Mimer SQL ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 30

    docker exec -i db_seeder_db bash < scripts/run_db_seeder_setup_mimer.input

    end=$(date +%s)
    echo "DOCKER Mimer SQL was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# MonetDB                               https://hub.docker.com/r/monetdb/monetdb
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "monetdb" ]; then
    echo "MonetDB."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (MonetDB ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create --name    db_seeder_db \
                  --network db_seeder_net \
                   -p       "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
                   monetdb/monetdb:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (MonetDB ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER MonetDB was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# MySQL Database                                  https://hub.docker.com/_/mysql
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "mysql" ]; then
    echo "MySQL Database."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (MySQL ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create -e        MYSQL_ROOT_PASSWORD=mysql \
                  --name    db_seeder_db \
                  --network db_seeder_net \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
                  mysql:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (MySQL ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 60

    docker network ls
    docker network inspect db_seeder_net

    end=$(date +%s)
fi

# ------------------------------------------------------------------------------
# OmniSciDB                 https://hub.docker.com/_/omnisci-open-source-edition
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "omnisci" ]; then
    rm -rf tmp/omnisci-docker-storage ¦¦ sudo rm -rf tmp/omnisci-docker-storage
    echo "OmniSciDB."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (OmniSciDB ${DB_SEEDER_VERSION})"

    rm    -rf tmp/omnisci-docker-storage
    mkdir     tmp/omnisci-docker-storage

    docker network create db_seeder_net  2>/dev/null || true
    docker create --name    db_seeder_db \
                  --network db_seeder_net \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
                  -v        "$PWD/tmp/omnisci-docker-storage":/omnisci-storage \
                  omnisci/core-os-cpu

    echo "Docker start db_seeder_db (OmniSciDB ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 60

    docker network ls
    docker network inspect db_seeder_net

    end=$(date +%s)
fi

# ------------------------------------------------------------------------------
# Oracle Database.
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "oracle" ]; then
    echo "Oracle Database"
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (Oracle ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create -e         ORACLE_PWD=oracle \
                  --name     db_seeder_db \
                  --network  db_seeder_net \
                  -p         "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
                  konnexionsgmbh/"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (Oracle ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "$(docker inspect -f {{.State.Health.Status}} db_seeder_db)" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 60; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    docker network ls
    docker network inspect db_seeder_net

    end=$(date +%s)
    echo "DOCKER Oracle Database was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Percona Server                         https://hub.docker.com/_/percona-server
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "percona" ]; then
    echo "Percona Server."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (Percona Server ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create -e        MYSQL_ROOT_PASSWORD=percona \
                  --name    db_seeder_db \
                  --network db_seeder_net \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}"/tcp \
                  percona/percona-server:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (Percona Server ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 60

    end=$(date +%s)
fi

# ------------------------------------------------------------------------------
# PostgreSQL                                   https://hub.docker.com/_/postgres
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "postgresql" ]; then
    echo "PostgreSQL."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (PostgreSQL ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create -e        POSTGRES_DB=kxn_db_sys \
                  -e        POSTGRES_PASSWORD=postgresql \
                  -e        POSTGRES_USER=kxn_user_sys \
                  --name    db_seeder_db \
                  --network db_seeder_net \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}" \
                  postgres:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (PostgreSQL ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 30

    docker network ls
    docker network inspect db_seeder_net

    end=$(date +%s)
    echo "DOCKER PostgreSQL was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# SQL Server Database  https://hub.docker.com/_/microsoft-mssql-server
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "sqlserver" ]; then
    echo "SQL Server."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (SQL Server ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker create -e        "ACCEPT_EULA=Y" \
                  -e        "SA_PASSWORD=sqlserver_2019" \
                  --name    db_seeder_db \
                  --network db_seeder_net \
                  -p        "${DB_SEEDER_CONNECTION_PORT}":"${DB_SEEDER_CONTAINER_PORT}" \
                  mcr.microsoft.com/mssql/server:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (SQL Server ${DB_SEEDER_VERSION}) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 30

    docker network ls
    docker network inspect db_seeder_net

    end=$(date +%s)
    echo "DOCKER SQL Server was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# VoltDB                        https://hub.docker.com/r/voltdb/voltdb-community
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "voltdb" ]; then
    echo "VoltDB."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (VoltDB ${DB_SEEDER_VERSION})"

    docker network create db_seeder_net  2>/dev/null || true
    docker run -d \
               -e        HOST_COUNT=1 \
               --name    db_seeder_db \
               --network db_seeder_net \
               -p        21212:21212 \
               -v        "$PWD/resources/voltdb/deployment.xml":/tmp/deployment.xml \
               voltdb/voltdb-community:"${DB_SEEDER_VERSION}"

    echo "Docker start db_seeder_db (VoltDB ${DB_SEEDER_VERSION}) ..."

    sleep 20

    end=$(date +%s)
    echo "DOCKER VoltDB was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# YugabyteDB                       https://hub.docker.com/r/yugabytedb/yugabyte/
# ------------------------------------------------------------------------------

if [ "${DB_SEEDER_DBMS_DB}" = "yugabyte" ]; then
    echo "YugabyteDB."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (YugabyteDB ${DB_SEEDER_VERSION})"

    rm -rf "$PWD/tmp/yb_data" ¦¦ sudo rm -rf "$PWD/tmp/yb_data"
    mkdir -p "$PWD/tmp/yb_data"

    docker network create db_seeder_net  2>/dev/null || true
    docker run -d \
               --name    db_seeder_db \
               --network db_seeder_net \
               -p        5433:5433 \
               -p        7000:7000 \
               -p        9001:9000 \
               -p        9042:9042 \
               -v        "$PWD/tmp/yb_data":/home/yugabyte/var \
               yugabytedb/yugabyte:"${DB_SEEDER_VERSION}" bin/yugabyted start --daemon=false

    echo "Docker start db_seeder_db (YugabyteDB ${DB_SEEDER_VERSION}) ..."

    sleep 30

    end=$(date +%s)
    echo "DOCKER YugabyteDB was ready in $((end - start)) seconds"
fi

if [ "${DB_SEEDER_DBMS_EMBEDDED}" == "no" ]; then
    docker ps
fi    

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
