#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder.sh: Creation of dummy data in an empty databases, 
#                   database users or schemas.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DBMS_DEFAULT=sqlite
export DB_SEEDER_SETUP_DBMS_DEFAULT=yes
export DB_SEEDER_NO_CREATE_RUNS_DEFAULT=2
export DB_SEEDER_RELEASE=2.1.0

if [ -z "$1" ]; then
    echo "==========================================="
    echo "complete     - All implemented DBMSs"
    echo "derby        - Apache Derby [client]"
    echo "derby_emb    - Apache Derby [embedded]"
    echo "cratedb      - CrateDB"
    echo "cubrid       - CUBRID"
    echo "firebird     - Firebird"
    echo "h2           - H2 Database Engine [client]"
    echo "h2_emb       - H2 Database Engine [embedded]"
    echo "hsqldb       - HyperSQL Database [client]"
    echo "hsqldb_emb   - HyperSQL Database [embedded]"
    echo "ibmdb2       - IBM Db2 Database"
    echo "informix     - IBM Informix"
    echo "mariadb      - MariaDB Server"
    echo "mimer        - Mimer SQL"
    echo "mssqlserver  - Microsoft SQL Server"
    echo "mysql        - MySQL"
    echo "mysql_presto - MySQL via Presto"
    echo "oracle       - Oracle Database"
    echo "postgresql   - PostgreSQL Database"
    echo "sqlite       - SQLite [embedded]"
    echo "-------------------------------------------"
    read -p -r "Enter the desired database management system [default: $DB_SEEDER_DBMS_DEFAULT] " DB_SEEDER_DBMS
    export DB_SEEDER_DBMS=$DB_SEEDER_DBMS

    if [ -z "$DB_SEEDER_DBMS" ]; then
        export DB_SEEDER_DBMS=$DB_SEEDER_DBMS_DEFAULT
    fi
else
    export DB_SEEDER_DBMS=$1
fi

if [ -z "$2" ]; then
    read -p -r "Setup the DBMS (yes/no) [default: $DB_SEEDER_SETUP_DBMS_DEFAULT] " DB_SEEDER_SETUP_DBMS
    export DB_SEEDER_SETUP_DBMS=$DB_SEEDER_SETUP_DBMS

    if [ -z "$DB_SEEDER_SETUP_DBMS" ]; then
        export DB_SEEDER_SETUP_DBMS=$DB_SEEDER_SETUP_DBMS_DEFAULT
    fi
else
    export DB_SEEDER_SETUP_DBMS=$2
fi

if [ -z "$3" ]; then
    read -p -r "Number of data creation runs (0-2) [default: $DB_SEEDER_NO_CREATE_RUNS_DEFAULT] " DB_SEEDER_NO_CREATE_RUNS
    export DB_SEEDER_NO_CREATE_RUNS=$DB_SEEDER_NO_CREATE_RUNS

    if [ -z "$DB_SEEDER_NO_CREATE_RUNS" ]; then
        export DB_SEEDER_NO_CREATE_RUNS=$DB_SEEDER_NO_CREATE_RUNS_DEFAULT
    fi
else
    export DB_SEEDER_NO_CREATE_RUNS=$3
fi

export DB_SEEDER_JAVA_CLASSPATH=".:lib/*:JAVA_HOME/lib"

# ------------------------------------------------------------------------------
# Start Properties.
# ------------------------------------------------------------------------------

export DB_SEEDER_DEFAULT_ROW_SIZE=1000

export DB_SEEDER_DBMS_EMBEDDED=no
export DB_SEEDER_DBMS_PRESTO=no

export DB_SEEDER_FILE_CONFIGURATION_NAME=src/main/resources/db_seeder.properties

export DB_SEEDER_FILE_STATISTICS_DELIMITER=\\t

if [ -z "$DB_SEEDER_FILE_STATISTICS_NAME" ]; then
    export DB_SEEDER_FILE_STATISTICS_NAME=statistics/db_seeder_local.tsv
fi 

unset -f DB_SEEDER_NULL_FACTOR=

if [ "$DB_SEEDER_DBMS" = "cratedb" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=5432
    export DB_SEEDER_CONNECTION_PREFIX=crate://
    export DB_SEEDER_CONTAINER_PORT=5432
    export DB_SEEDER_PASSWORD=cratedb
    export DB_SEEDER_USER=kxn_user
    export DB_SEEDER_USER_SYS=crate
    export DB_SEEDER_VERSION=4.1.6
    export DB_SEEDER_VERSION=4.1.8
    export DB_SEEDER_VERSION=4.2.2
fi

if [ "$DB_SEEDER_DBMS" = "cubrid" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=33000
    export DB_SEEDER_CONNECTION_PREFIX="jdbc:cubrid:"
    export DB_SEEDER_CONNECTION_SUFFIX="::"
    export DB_SEEDER_CONTAINER_PORT=33000
    export DB_SEEDER_DATABASE=kxn_db
    export DB_SEEDER_PASSWORD=cubrid
    export DB_SEEDER_USER=kxn_user
    export DB_SEEDER_USER_SYS=DBA
    export DB_SEEDER_VERSION=10.2
fi

if [ "$DB_SEEDER_DBMS" = "derby" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=1527
    export DB_SEEDER_CONNECTION_PREFIX=jdbc:derby:
    export DB_SEEDER_CONTAINER_PORT=1527
    export DB_SEEDER_DATABASE=./tmp/derby_kxn_db
    export DB_SEEDER_VERSION=10.15.2.0
fi

if [ "$DB_SEEDER_DBMS" = "derby_emb" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    # TODO Bug in Apache Derby
    if [ "$DB_SEEDER_NO_CREATE_RUNS" = "2" ]; then
        export DB_SEEDER_NO_CREATE_RUNS=1
    fi
    export DB_SEEDER_CONNECTION_PREFIX=jdbc:derby:
    export DB_SEEDER_DATABASE=./tmp/derby_kxn_db
    export DB_SEEDER_DBMS_EMBEDDED=yes
fi

if [ "$DB_SEEDER_DBMS" = "firebird" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=3050
    export DB_SEEDER_CONNECTION_PREFIX="jdbc:firebirdsql://"
    export DB_SEEDER_CONNECTION_SUFFIX="?encoding=UTF8&useFirebirdAutocommit=true&useStreamBlobs=true"
    export DB_SEEDER_CONTAINER_PORT=3050
    export DB_SEEDER_DATABASE=firebird_kxn_db
    export DB_SEEDER_PASSWORD=firebird
    export DB_SEEDER_PASSWORD_SYS=firebird
    export DB_SEEDER_USER=kxn_user
    export DB_SEEDER_USER_SYS=SYSDBA
    export DB_SEEDER_VERSION=3.0.5
    export DB_SEEDER_VERSION=3.0.6
fi

if [ "$DB_SEEDER_DBMS" = "h2" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=9092
    export DB_SEEDER_CONNECTION_PREFIX=jdbc:h2:
    export DB_SEEDER_CONTAINER_PORT=9092
    export DB_SEEDER_DATABASE=./tmp/h2_kxn_db
    export DB_SEEDER_PASSWORD=h2
    export DB_SEEDER_SCHEMA=kxn_schema
    export DB_SEEDER_USER=kxn_user
    export DB_SEEDER_VERSION=1.4.200
fi

if [ "$DB_SEEDER_DBMS" = "h2_emb" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_PREFIX=jdbc:h2:
    export DB_SEEDER_DATABASE=./tmp/h2_kxn_db
    export DB_SEEDER_DBMS_EMBEDDED=yes
    export DB_SEEDER_PASSWORD=h2
    export DB_SEEDER_SCHEMA=kxn_schema
    export DB_SEEDER_USER=kxn_user
fi

if [ "$DB_SEEDER_DBMS" = "hsqldb" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=9001
    export DB_SEEDER_CONNECTION_PREFIX="jdbc:hsqldb:"
    export DB_SEEDER_CONNECTION_SUFFIX=";ifexists=false;shutdown=true"
    export DB_SEEDER_CONTAINER_PORT=9001
    export DB_SEEDER_DATABASE=kxn_db
    export DB_SEEDER_PASSWORD=hsqldb
    export DB_SEEDER_SCHEMA=kxn_schema
    export DB_SEEDER_USER=kxn_user
    export DB_SEEDER_USER_SYS=SA
    export DB_SEEDER_VERSION=2.5.1
fi

if [ "$DB_SEEDER_DBMS" = "hsqldb_emb" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_PREFIX="jdbc:hsqldb:"
    export DB_SEEDER_CONNECTION_SUFFIX=";ifexists=false;shutdown=true"
    export DB_SEEDER_DATABASE=./tmp/hsqldb_kxn_db
    export DB_SEEDER_DBMS_EMBEDDED=yes
    export DB_SEEDER_PASSWORD=hsqldb
    export DB_SEEDER_SCHEMA=kxn_schema
    export DB_SEEDER_USER=kxn_user
    export DB_SEEDER_USER_SYS=SA
fi

if [ "$DB_SEEDER_DBMS" = "ibmdb2" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=50000
    export DB_SEEDER_CONNECTION_PREFIX=jdbc:db2://
    export DB_SEEDER_CONTAINER_PORT=50000
    export DB_SEEDER_DATABASE=kxn_db
    export DB_SEEDER_PASSWORD_SYS=ibmdb2
    export DB_SEEDER_SCHEMA=kxn_schema
    export DB_SEEDER_USER_SYS=db2inst1
    export DB_SEEDER_VERSION=11.5.0.0a
    export DB_SEEDER_VERSION=11.5.4.0
fi

if [ "$DB_SEEDER_DBMS" = "informix" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=9088
    export DB_SEEDER_CONNECTION_PREFIX="jdbc:informix-sqli://"
    export DB_SEEDER_CONNECTION_SUFFIX=":INFORMIXSERVER=informix"
    export DB_SEEDER_CONTAINER_PORT=9088
    export DB_SEEDER_DATABASE=kxn_db
    export DB_SEEDER_DATABASE_SYS=sysmaster
    export DB_SEEDER_PASSWORD_SYS=in4mix
    export DB_SEEDER_USER_SYS=informix
    export DB_SEEDER_VERSION=14.10.FC3DE
    export DB_SEEDER_VERSION=14.10.FC4DE
fi

if [ "$DB_SEEDER_DBMS" = "mariadb" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=3306
    export DB_SEEDER_CONNECTION_PREFIX=jdbc:mariadb://
    export DB_SEEDER_CONTAINER_PORT=3306
    export DB_SEEDER_DATABASE=kxn_db
    export DB_SEEDER_DATABASE_SYS=mysql
    export DB_SEEDER_PASSWORD=mariadb
    export DB_SEEDER_PASSWORD_SYS=mariadb
    export DB_SEEDER_USER=kxn_user
    export DB_SEEDER_USER_SYS=root
    export DB_SEEDER_VERSION=10.4.13
    export DB_SEEDER_VERSION=10.5.3
    export DB_SEEDER_VERSION=10.5.4
fi

if [ "$DB_SEEDER_DBMS" = "mimer" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=11360
    export DB_SEEDER_CONNECTION_PREFIX=jdbc:mimer://
    export DB_SEEDER_CONTAINER_PORT=1360
    export DB_SEEDER_DATABASE=kxn_db
    export DB_SEEDER_DATABASE_SYS=mimerdb
    export DB_SEEDER_PASSWORD=mimer
    export DB_SEEDER_PASSWORD_SYS=mimer
    export DB_SEEDER_USER=kxn_user
    export DB_SEEDER_USER_SYS=SYSADM
    export DB_SEEDER_VERSION=v11.0.3c
fi

if [ "$DB_SEEDER_DBMS" = "mssqlserver" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=1433
    export DB_SEEDER_CONNECTION_PREFIX=jdbc:sqlserver://
    export DB_SEEDER_CONTAINER_PORT=1433
    export DB_SEEDER_DATABASE=kxn_db
    export DB_SEEDER_DATABASE_SYS=master
    export DB_SEEDER_PASSWORD=mssqlserver_2019
    export DB_SEEDER_PASSWORD_SYS=mssqlserver_2019
    export DB_SEEDER_SCHEMA=kxn_schema
    export DB_SEEDER_USER=kxn_user
    export DB_SEEDER_USER_SYS=sa
    export DB_SEEDER_VERSION=2019-latest
fi

if [ "$DB_SEEDER_DBMS" = "mysql" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=3306
    export DB_SEEDER_CONNECTION_PREFIX="jdbc:mysql://"
    export DB_SEEDER_CONNECTION_SUFFIX="?serverTimezone=UTC"
    export DB_SEEDER_CONTAINER_PORT=3306
    export DB_SEEDER_DATABASE=kxn_db
    export DB_SEEDER_DATABASE_SYS=sys
    export DB_SEEDER_PASSWORD=mysql
    export DB_SEEDER_PASSWORD_SYS=mysql
    export DB_SEEDER_USER=kxn_user
    export DB_SEEDER_USER_SYS=root
    export DB_SEEDER_VERSION=8.0.20
    export DB_SEEDER_VERSION=8.0.21
fi

if [ "$DB_SEEDER_DBMS" = "mysql_presto" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CATALOG=db_seeder.mysql.user
    export DB_SEEDER_CATALOG_SYS=db_seeder.mysql.system
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=8080
    export DB_SEEDER_CONNECTION_PREFIX=jdbc:presto://
    export DB_SEEDER_CONTAINER_PORT=8080
    export DB_SEEDER_DBMS_PRESTO=yes
    export DB_SEEDER_PASSWORD=presto
    export DB_SEEDER_USER=presto
fi

if [ "$DB_SEEDER_DBMS" = "oracle" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=1521
    export DB_SEEDER_CONNECTION_PREFIX=jdbc:oracle:thin:@//
    export DB_SEEDER_CONNECTION_SERVICE=orclpdb1
    export DB_SEEDER_CONTAINER_PORT=1521
    export DB_SEEDER_PASSWORD=oracle
    export DB_SEEDER_PASSWORD_SYS=oracle
    export DB_SEEDER_USER=kxn_user
    export DB_SEEDER_USER_SYS="SYS AS SYSDBA"
    export DB_SEEDER_VERSION=db_12_2_ee
    export DB_SEEDER_VERSION=db_18_3_ee
    export DB_SEEDER_VERSION=db_19_3_ee
fi

if [ "$DB_SEEDER_DBMS" = "postgresql" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_HOST=localhost
    export DB_SEEDER_CONNECTION_PORT=5432
    export DB_SEEDER_CONNECTION_PREFIX=jdbc:postgresql://
    export DB_SEEDER_CONTAINER_PORT=5432
    export DB_SEEDER_DATABASE=kxn_db
    export DB_SEEDER_DATABASE_SYS=kxn_db_sys
    export DB_SEEDER_PASSWORD=postgresql
    export DB_SEEDER_PASSWORD_SYS=postgresql
    export DB_SEEDER_USER=kxn_user
    export DB_SEEDER_USER_SYS=kxn_user_sys
    export DB_SEEDER_VERSION=12.3-alpine
fi

if [ "$DB_SEEDER_DBMS" = "sqlite" ] || [ "$DB_SEEDER_DBMS" = "complete" ]; then
    export DB_SEEDER_CONNECTION_PREFIX=jdbc:sqlite:
    export DB_SEEDER_DATABASE=./tmp/sqlite_kxn_db
    export DB_SEEDER_DBMS_EMBEDDED=yes
fi

# ------------------------------------------------------------------------------
# End   Properties.
# ------------------------------------------------------------------------------

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Creation of dummy data in an empty database schema / user."
echo "--------------------------------------------------------------------------------"
echo "DBMS                              : $DB_SEEDER_DBMS"
echo "DBMS_EMBEDDED                     : $DB_SEEDER_DBMS_EMBEDDED"
echo "DBMS_PRESTO                       : $DB_SEEDER_DBMS_PRESTO"
echo "IS_TRAVIS                         : $DB_SEEDER_IS_TRAVIS"
echo "NO_CREATE_RUNS                    : $DB_SEEDER_NO_CREATE_RUNS"
echo "RELEASE                           : $DB_SEEDER_RELEASE"
echo "SETUP_DBMS                        : $DB_SEEDER_SETUP_DBMS"
echo "--------------------------------------------------------------------------------"
echo "FILE_CONFIGURATION_NAME           : $DB_SEEDER_FILE_CONFIGURATION_NAME"
echo "FILE_STATISTICS_DELIMITER         : $DB_SEEDER_FILE_STATISTICS_DELIMITER"
echo "FILE_STATISTICS_HEADER            : $DB_SEEDER_FILE_STATISTICS_HEADER"
echo "FILE_STATISTICS_NAME              : $DB_SEEDER_FILE_STATISTICS_NAME"
echo "JAVA_CLASSPATH                    : $DB_SEEDER_JAVA_CLASSPATH"
echo "--------------------------------------------------------------------------------"
echo "CATALOG                           : $DB_SEEDER_CATALOG"
echo "CATALOG_SYS                       : $DB_SEEDER_CATALOG_SYS"
echo "CONNECTION_HOST                   : $DB_SEEDER_CONNECTION_HOST"
echo "CONNECTION_PORT                   : $DB_SEEDER_CONNECTION_PORT"
echo "CONNECTION_PREFIX                 : $DB_SEEDER_CONNECTION_PREFIX"
echo "CONNECTION_SERVICE                : $DB_SEEDER_CONNECTION_SERVICE"
echo "CONNECTION_SUFFIX                 : $DB_SEEDER_CONNECTION_SUFFIX"
echo "CONTAINER_PORT                    : $DB_SEEDER_CONTAINER_PORT"
echo "DATABASE                          : $DB_SEEDER_DATABASE"
echo "DATABASE_SYS                      : $DB_SEEDER_DATABASE_SYS"
echo "PASSWORD                          : $DB_SEEDER_PASSWORD"
echo "PASSWORD_SYS                      : $DB_SEEDER_PASSWORD_SYS"
echo "SCHEMA                            : $DB_SEEDER_SCHEMA"
echo "USER                              : $DB_SEEDER_USER"
echo "USER_SYS                          : $DB_SEEDER_USER_SYS"
echo "VERSION                           : $DB_SEEDER_VERSION"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if [ "$DB_SEEDER_DBMS" = "complete" ]; then
    if ! ( ./scripts/run_db_seeder_complete.sh ); then
        exit 255
    fi    
else
    if [ "$DB_SEEDER_DBMS_PRESTO" = "yes" ]; then
        if ! ( ./scripts/run_db_seeder_setup_presto.sh ); then
            exit 255
        fi    
    fi

    if ! ( ./scripts/run_db_seeder_single.sh "${DB_SEEDER_DBMS}" ); then
        exit 255
    fi    
fi  

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
