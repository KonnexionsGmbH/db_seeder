#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_setup_dbms.sh: Setup a database Docker container.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DBMS_DEFAULT=sqlite
export DB_SEEDER_DBMS_EMBEDDED=no
export DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT=yes

export DB_SEEDER_CUBRID_DATABASE=kxn_db
export DB_SEEDER_DERBY_DATABASE=kxn_db
export DB_SEEDER_IBMDB2_DATABASE=kxn_db
export DB_SEEDER_SQLITE_DATABASE=kxn_db

export DB_SEEDER_VERSION_CRATEDB=4.1.6
export DB_SEEDER_VERSION_CUBRID=10.2
export DB_SEEDER_VERSION_DERBY=10.15.2.0
export DB_SEEDER_VERSION_IBMDB2=11.5.0.0a

export DB_SEEDER_VERSION_MARIADB=10.4.13
export DB_SEEDER_VERSION_MARIADB=10.5.3

export DB_SEEDER_VERSION_MSSQLSERVER=2019-latest
export DB_SEEDER_VERSION_MYSQL=8.0.20

export DB_SEEDER_VERSION_ORACLE=db_12_2_ee
export DB_SEEDER_VERSION_ORACLE=db_18_3_ee
export DB_SEEDER_VERSION_ORACLE=db_19_3_ee

export DB_SEEDER_VERSION_POSTGRESQL=12.3

if [ -z "$1" ]; then
    echo "===================================="
    echo "derby       - Apache Derby [client]"
    echo "derby_emb   - Apache Derby [embedded]"
    echo "cubrid      - CUBRID"
    echo "ibmdb2      - IBM Db2 Database"
    echo "mariadb     - MariaDB Server"
    echo "mssqlserver - Microsoft SQL Server"
    echo "mysql       - MySQL"
    echo "oracle      - Oracle Database"
    echo "postgresql  - PostgreSQL Database"
    echo "sqlite      - SQLite [embedded]"
    echo "------------------------------------"
    read -p "Enter the desired database management system [default: $DB_SEEDER_DBMS_DEFAULT] " DB_SEEDER_DBMS
    export DB_SEEDER_DBMS=$DB_SEEDER_DBMS

    if [ -z "$DB_SEEDER_DBMS" ]; then
        export DB_SEEDER_DBMS=$DB_SEEDER_DBMS_DEFAULT
    fi
else
    export DB_SEEDER_DBMS=$1
fi

if [ "$DB_SEEDER_DBMS" = "derby_emb" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
fi
if [ "$DB_SEEDER_DBMS" = "sqlite" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
fi

if [ "$DB_SEEDER_DBMS_EMBEDDED" = "yes" ]; then
    export DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT=no
fi

if [ "$DB_SEEDER_DBMS" = "sqlite" ]; then
    export DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT=no
else
    export DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT=yes
fi

if [ -z "$2" ]; then
    read -p "Delete the existing Docker container $DB_SEEDER_DBMS (yes/no) [default: $DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT] " DB_SEEDER_DELETE_EXISTING_CONTAINER
    export DB_SEEDER_DELETE_EXISTING_CONTAINER=$DB_SEEDER_DELETE_EXISTING_CONTAINER

    if [ -z "$DB_SEEDER_DELETE_EXISTING_CONTAINER" ]; then
        export DB_SEEDER_DELETE_EXISTING_CONTAINER=$DB_SEEDER_DELETE_EXISTING_CONTAINER_DEFAULT
    fi
else
    export DB_SEEDER_DELETE_EXISTING_CONTAINER=$2
fi

if [ "$DB_SEEDER_DELETE_EXISTING_CONTAINER" != "no" ]; then
    echo "Docker stop/rm db_seeder_db"
    docker stop db_seeder_db
    docker rm -f db_seeder_db
fi

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - setup a database Docker container."
echo "--------------------------------------------------------------------------------"
echo "DBMS                      : $DB_SEEDER_DBMS"
echo "DBMS_EMBEDDED             : $DB_SEEDER_DBMS_EMBEDDED"
if [ "$DB_SEEDER_DBMS_EMBEDDED" != "no" ]; then
    echo "DELETE_EXISTING_CONTAINER : $DB_SEEDER_DELETE_EXISTING_CONTAINER"
fi    
echo --------------------------------------------------------------------------------
if [ "$DB_SEEDER_DBMS" = "cratedb" ]; then
    echo "VERSION_CRATEDB           : $DB_SEEDER_VERSION_CRATEDB"
fi
if [ "$DB_SEEDER_DBMS" = "cubrid" ]; then
    echo "VERSION_CUBRID            : $DB_SEEDER_VERSION_CUBRID"
    echo "CUBRID_DATABASE           : $DB_SEEDER_CUBRID_DATABASE"
    rm -f $DB_SEEDER_CUBRID_DATABASE
fi
if [ "$DB_SEEDER_DBMS" = "derby_emb" ]; then
    echo "VERSION_DERBY             : $DB_SEEDER_VERSION_IBMDB2"
    echo "DERBY_DATABASE            : $DB_SEEDER_DERBY_DATABASE"
    rm -f $DB_SEEDER_DERBY_DATABASE
    mkdir $DB_SEEDER_DERBY_DATABASE
fi
if [ "$DB_SEEDER_DBMS" = "ibmdb2" ]; then
    echo "VERSION_IBMDB2            : $DB_SEEDER_VERSION_IBMDB2"
    echo "IBMDB2_DATABASE           : $DB_SEEDER_IBMDB2_DATABASE"
    rm -f $DB_SEEDER_IBMDB2_DATABASE
fi
if [ "$DB_SEEDER_DBMS" = "mariadb" ]; then
    echo "VERSION_MARIADB           : $DB_SEEDER_VERSION_MARIADB"
fi
if [ "$DB_SEEDER_DBMS" = "mssqlserver" ]; then
    echo "VERSION_MSSQLSERVER       : $DB_SEEDER_VERSION_MSSQLSERVER"
fi
if [ "$DB_SEEDER_DBMS" = "mysql" ]; then
    echo "VERSION_MYSQL             : $DB_SEEDER_VERSION_MYSQL"
fi
if [ "$DB_SEEDER_DBMS" = "oracle" ]; then
    echo "VERSION_ORACLE            : $DB_SEEDER_VERSION_ORACLE"
fi
if [ "$DB_SEEDER_DBMS" = "postgresql" ]; then
    echo "VERSION_POSTGRESQL        : $DB_SEEDER_VERSION_POSTGRESQL"
fi
if [ "$DB_SEEDER_DBMS" = "sqlite" ]; then
    echo "VERSION_SQLITE            : $DB_SEEDER_VERSION_SQLITE"
    echo "SQLITE_DATABASE           : $DB_SEEDER_SQLITE_DATABASE"
    rm -f $DB_SEEDER_SQLITE_DATABASE
fi
echo --------------------------------------------------------------------------------
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

# ------------------------------------------------------------------------------
# CrateDB                                         https://hub.docker.com/_/crate
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "cratedb" ]; then
    start=$(date +%s)
    echo "CrateDB."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (CrateDB $DB_SEEDER_VERSION_CRATEDB)"
    docker create --name db_seeder_db -p 5432:5432/tcp --env CRATE_HEAP_SIZE=2g crate:$DB_SEEDER_VERSION_CRATEDB crate -Cnetwork.host=_site_ -Cdiscovery.type=single-node

    echo "Docker start db_seeder_db (CrateDB $DB_SEEDER_VERSION_CRATEDB) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 10

    end=$(date +%s)
    echo "DOCKER CrateDB was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# CUBRID                                  https://hub.docker.com/r/cubrid/cubrid
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "cubrid" ]; then
    start=$(date +%s)
    echo "CUBRID."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (CUBRID $DB_SEEDER_VERSION_CRATEDB)"
    docker create --name db_seeder_db -p 33000:33000/tcp -e CUBRID_DB=$DB_SEEDER_CUBRID_DATABASE cubrid/cubrid:$DB_SEEDER_VERSION_CUBRID

    echo "Docker start db_seeder_db (CUBRID $DB_SEEDER_VERSION_CRATEDB) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

# wwe    sleep 10

    end=$(date +%s)
    echo "DOCKER CUBRID was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Apache Derby
#           https://hub.docker.com/repository/docker/konnexionsgmbh/apache_derby
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "derby" ]; then
    echo "Apache Derby"
    echo "--------------------------------------------------------------------------------"
    start=$(date +%s)
    echo "Docker create db_seeder_db (Apache Derby $DB_SEEDER_VERSION_DERBY)"
    docker create --name db_seeder_db -p 1527:1527/tcp konnexionsgmbh/apache_derby:$DB_SEEDER_VERSION_DERBY

    echo "Docker start db_seeder_db (Apache Derby $DB_SEEDER_VERSION_DERBY) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "`docker inspect -f {{.State.Health.Status}} db_seeder_db`" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 10; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER Apache Derby was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# IBM Db2 Database                           https://hub.docker.com/r/ibmcom/db2
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "ibmdb2" ]; then
    start=$(date +%s)
    echo "IBM Db2 Database."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (IBM Db2 $DB_SEEDER_VERSION_IBMDB2)"
    docker run -itd --name db_seeder_db --restart unless-stopped -e DBNAME=$DB_SEEDER_IBMDB2_DATABASE -e DB2INST1_PASSWORD=ibmdb2 -e LICENSE=accept -p 50000:50000 --privileged=true ibmcom/db2:$DB_SEEDER_VERSION_IBMDB2

    sleep 120

    end=$(date +%s)
    echo "DOCKER IBM Db2 Database was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# MariaDB Server                                https://hub.docker.com/_/mariadb
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "mariadb" ]; then
    start=$(date +%s)
    echo "MariaDB Server."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (MariaDB $DB_SEEDER_VERSION_MARIADB)"
    docker create -e MYSQL_ROOT_PASSWORD=mariadb --name db_seeder_db -p 3306:3306/tcp mariadb:$DB_SEEDER_VERSION_MARIADB

    echo "Docker start db_seeder_db (MariaDB $DB_SEEDER_VERSION_MARIADB) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 20

    end=$(date +%s)
    echo "DOCKER MariaDB Server was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Microsoft SQL Server Database  https://hub.docker.com/_/microsoft-mssql-server
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "mssqlserver" ]; then
    start=$(date +%s)
    echo "Microsoft SQL Server."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (Microsoft SQL Server $DB_SEEDER_VERSION_MSSQLSERVER)"
    docker create -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=mssqlserver_2019" --name db_seeder_db -p 1433:1433 mcr.microsoft.com/mssql/server:$DB_SEEDER_VERSION_MSSQLSERVER

    echo "Docker start db_seeder_db (Microsoft SQL Server $DB_SEEDER_VERSION_MSSQLSERVER) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 20

    end=$(date +%s)
    echo "DOCKER Microsoft SQL Server was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# MySQL Database                                  https://hub.docker.com/_/mysql
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "mysql" ]; then
    start=$(date +%s)
    echo "MySQL Database."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (MySQL $DB_SEEDER_VERSION_MYSQL)"
    docker create -e MYSQL_ROOT_PASSWORD=mysql --name db_seeder_db -p 3306:3306/tcp mysql:$DB_SEEDER_VERSION_MYSQL

    echo "Docker start db_seeder_db (MySQL $DB_SEEDER_VERSION_MYSQL) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    sleep 20

    end=$(date +%s)
    echo "DOCKER MySQL Database was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# Oracle Database.
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "oracle" ]; then
    echo "Oracle Database"
    echo "--------------------------------------------------------------------------------"
    start=$(date +%s)
    echo "Docker create db_seeder_db (Oracle $DB_SEEDER_VERSION_ORACLE)"
    docker create -e ORACLE_PWD=oracle --name db_seeder_db -p 1521:1521/tcp --shm-size 1G konnexionsgmbh/$DB_SEEDER_VERSION_ORACLE

    echo "Docker start db_seeder_db (Oracle $DB_SEEDER_VERSION_ORACLE) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    while [ "`docker inspect -f {{.State.Health.Status}} db_seeder_db`" != "healthy" ]; do docker ps --filter "name=db_seeder_db"; sleep 60; done
    if [ $? -ne 0 ]; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER Oracle Database was ready in $((end - start)) seconds"
fi

# ------------------------------------------------------------------------------
# PostgreSQL Database                          https://hub.docker.com/_/postgres
# ------------------------------------------------------------------------------

if [ "$DB_SEEDER_DBMS" = "postgresql" ]; then
    start=$(date +%s)
    echo "PostgreSQL Database."
    echo "--------------------------------------------------------------------------------"
    echo "Docker create db_seeder_db (PostgreSQL $DB_SEEDER_VERSION_POSTGRESQL)"
    docker create -e POSTGRES_DB=kxn_db_sys -e POSTGRES_PASSWORD=postgresql -e POSTGRES_USER=kxn_user_sys --name db_seeder_db -p 5432:5432 postgres:$DB_SEEDER_VERSION_POSTGRESQL

    echo "Docker start db_seeder_db (PostgreSQL $DB_SEEDER_VERSION_POSTGRESQL) ..."
    if ! docker start db_seeder_db; then
        exit 255
    fi

    end=$(date +%s)
    echo "DOCKER PostgreSQL Database was ready in $((end - start)) seconds"
fi

if [ "$DB_SEEDER_DBMS_EMBEDDED" == "no" ]; then
    docker ps
fi    

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
