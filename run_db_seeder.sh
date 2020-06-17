#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder.sh: Creation of dummy data in an empty database schema / user.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DATABASE_DBMS_DEFAULT=sqlite

if [ -z "$1" ]; then
    echo "===================================="
    echo "cratedb     - CrateDB"
    echo "cubrid      - CUBRID"
    echo "ibmdb2      - IBM Db2 Database"
    echo "mariadb     - MariaDB Server"
    echo "mssqlserver - Microsoft SQL Server"
    echo "mysql       - MySQL"
    echo "oracle      - Oracle Database"
    echo "postgresql  - PostgreSQL Database"
    echo "sqlite      - SQLite"
    echo "------------------------------------"
    read -p "Enter the desired database management system [default: $DB_SEEDER_DATABASE_DBMS_DEFAULT] " DB_SEEDER_DATABASE_DBMS
    export DB_SEEDER_DATABASE_DBMS=$DB_SEEDER_DATABASE_DBMS

    if [ -z "$DB_SEEDER_DATABASE_DBMS" ]; then
        export DB_SEEDER_DATABASE_DBMS=$DB_SEEDER_DATABASE_DBMS_DEFAULT
    fi
else
    export DB_SEEDER_DATABASE_DBMS=$1
fi

export DB_SEEDER_FILE_CONFIGURATION_NAME=src/main/resources/db_seeder.properties

export DB_SEEDER_JAVA_CLASSPATH=".:lib/*:JAVA_HOME/lib"

# export DB_SEEDER_JDBC_CONNECTION_HOST=

# export DB_SEEDER_MAX_ROW_CITY=
# export DB_SEEDER_MAX_ROW_COMPANY=
# export DB_SEEDER_MAX_ROW_COUNTRY=
# export DB_SEEDER_MAX_ROW_COUNTRY_STATE=
# export DB_SEEDER_MAX_ROW_TIMEZONE=

#if [ "$DB_SEEDER_DATABASE_DBMS" = "cratedb" ]; then
#    export DB_SEEDER_CRATEDB_CONNECTION_PORT=
#    export DB_SEEDER_CRATEDB_CONNETION_PREFIX=
#    export DB_SEEDER_CRATEDB_PASSWORD=
#    export DB_SEEDER_CRATEDB_USER=
#fi
#if [ "$DB_SEEDER_DATABASE_DBMS" = "cubrid" ]; then
#    export DB_SEEDER_CUBRID_CONNECTION_PORT=
#    export DB_SEEDER_CUBRID_CONNETION_PREFIX=
#    export DB_SEEDER_CUBRID_DATABASE=
#    export DB_SEEDER_CUBRID_PASSWORD=
#    export DB_SEEDER_CUBRID_USER=
#fi
#if [ "$DB_SEEDER_DATABASE_DBMS" = "ibmdb2" ]; then
#    export DB_SEEDER_IBMDB2_CONNECTION_PORT=
#    export DB_SEEDER_IBMDB2_CONNETION_PREFIX=
#    export DB_SEEDER_IBMDB2_DATABASE=
#    export DB_SEEDER_IBMDB2_PASSWORD=
#    export DB_SEEDER_IBMDB2_SCHEMA=
#fi
#if [ "$DB_SEEDER_DATABASE_DBMS" = "mariadb" ]; then
#    export DB_SEEDER_MARIADB_CONNECTION_PORT=
#    export DB_SEEDER_MARIADB_CONNETION_PREFIX=
#    export DB_SEEDER_MARIADB_DATABASE=
#    export DB_SEEDER_MARIADB_PASSWORD=
#    export DB_SEEDER_MARIADB_PASSWORD_SYS=
#    export DB_SEEDER_MARIADB_USER=
#fi
#if [ "$DB_SEEDER_DATABASE_DBMS" = "mssqlserver" ]; then
#    export DB_SEEDER_MSSQLSERVER_CONNECTION_PORT=
#    export DB_SEEDER_MSSQLSERVER_CONNETION_PREFIX=
#    export DB_SEEDER_MSSQLSERVER_DATABASE=
#    export DB_SEEDER_MSSQLSERVER_PASSWORD=
#    export DB_SEEDER_MSSQLSERVER_PASSWORD_SYS=
#    export DB_SEEDER_MSSQLSERVER_SCHEMA=
#    export DB_SEEDER_MSSQLSERVER_USER=
#fi
#if [ "$DB_SEEDER_DATABASE_DBMS" = "mysql" ]; then
#    export DB_SEEDER_MYSQL_CONNECTION_PORT=
#    export DB_SEEDER_MYSQL_CONNETION_PREFIX=
#    export DB_SEEDER_MYSQL_CONNETION_SUFFIX=
#    export DB_SEEDER_MYSQL_DATABASE=
#    export DB_SEEDER_MYSQL_PASSWORD=
#    export DB_SEEDER_MYSQL_PASSWORD_SYS=
#    export DB_SEEDER_MYSQL_USER=
#fi
#if [ "$DB_SEEDER_DATABASE_DBMS" = "oracle" ]; then
#    export DB_SEEDER_ORACLE_CONNECTION_PORT=
#    export DB_SEEDER_ORACLE_CONNETION_PREFIX=
#    export DB_SEEDER_ORACLE_CONNETION_SERVICE=
#    export DB_SEEDER_ORACLE_PASSWORD=
#    export DB_SEEDER_ORACLE_PASSWORD_SYS=
#    export DB_SEEDER_ORACLE_USER=
#fi
#if [ "$DB_SEEDER_DATABASE_DBMS" = "postgresql" ]; then
#    export DB_SEEDER_POSTGRESQL_CONNECTION_PORT=
#    export DB_SEEDER_POSTGRESQL_CONNETION_PREFIX=
#    export DB_SEEDER_POSTGRESQL_DATABASE=
#    export DB_SEEDER_POSTGRESQL_PASSWORD=
#    export DB_SEEDER_POSTGRESQL_PASSWORD_SYS=
#    export DB_SEEDER_POSTGRESQL_USER=
#fi
#if [ "$DB_SEEDER_DATABASE_DBMS" = "sqlite" ]; then
#    export DB_SEEDER_SQLITE_CONNETION_PREFIX=
#    export DB_SEEDER_SQLITE_DATABASE=
#fi

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Creation of dummy data in an empty database schema / user."
echo "--------------------------------------------------------------------------------"
echo "DATABASE_DBMS                     : $DB_SEEDER_DATABASE_DBMS"
echo --------------------------------------------------------------------------------
echo "FILE_CONFIGURATION_NAME           : $DB_SEEDER_FILE_CONFIGURATION_NAME"
echo "JAVA_CLASSPATH                    : $DB_SEEDER_JAVA_CLASSPATH"
echo "--------------------------------------------------------------------------------"
echo "CONNECTION_HOST                   : $DB_SEEDER_JDBC_CONNECTION_HOST"
echo "--------------------------------------------------------------------------------"
echo "MAX_ROW_CITY                      : $DB_SEEDER_MAX_ROW_CITY"
echo "MAX_ROW_COMPANY                   : $DB_SEEDER_MAX_ROW_COMPANY"
echo "MAX_ROW_COUNTRY                   : $DB_SEEDER_MAX_ROW_COUNTRY"
echo "MAX_ROW_COUNTRY_STATE             : $DB_SEEDER_MAX_ROW_COUNTRY_STATE"
echo "MAX_ROW_TIMEZONE                  : $DB_SEEDER_MAX_ROW_TIMEZONE"
echo "--------------------------------------------------------------------------------"
if [ "$DB_SEEDER_DATABASE_DBMS" = "cratedb" ]; then
    echo "CRATEDB_CONNECTION_PORT           : $DB_SEEDER_CRATEDB_CONNECTION_PORT"
    echo "CRATEDB_CONNECTION_PREFIX         : $DB_SEEDER_CRATEDB_CONNECTION_PREFIX"
    echo "CRATEDB_PASSWORD                  : $DB_SEEDER_CRATEDB_PASSWORD"
    echo "CRATEDB_USER                      : $DB_SEEDER_CRATEDB_USER"
fi
if [ "$DB_SEEDER_DATABASE_DBMS" = "cubrid" ]; then
    echo "CUBRID_CONNECTION_PORT            : $DB_SEEDER_CUBRID_CONNECTION_PORT"
    echo "CUBRID_CONNECTION_PREFIX          : $DB_SEEDER_CUBRID_CONNECTION_PREFIX"
    echo "CUBRID_DATABASE                   : $DB_SEEDER_CUBRID_DATABASE"
    echo "CUBRID_PASSWORD                   : $DB_SEEDER_CUBRID_PASSWORD"
    echo "CUBRID_PASSWORD_SYS               : $DB_SEEDER_CUBRID_PASSWORD_SYS"
    echo "CUBRID_USER                       : $DB_SEEDER_CUBRID_USER"
fi
if [ "$DB_SEEDER_DATABASE_DBMS" = "ibmdb2" ]; then
    echo "IBMDB2_CONNECTION_PORT            : $DB_SEEDER_IBMDB2_CONNECTION_PORT"
    echo "IBMDB2_CONNECTION_PREFIX          : $DB_SEEDER_IBMDB2_CONNECTION_PREFIX"
    echo "IBMDB2_DATABASE                   : $DB_SEEDER_IBMDB2_DATABASE"
    echo "IBMDB2_PASSWORD                   : $DB_SEEDER_IBMDB2_PASSWORD"
    echo "IBMDB2_SCHEMA                     : $DB_SEEDER_IBMDB2_SCHEMA"
fi
if [ "$DB_SEEDER_DATABASE_DBMS" = "mariadb" ]; then
    echo "MARIADB_CONNECTION_PORT           : $DB_SEEDER_MARIADB_CONNECTION_PORT"
    echo "MARIADB_CONNECTION_PREFIX         : $DB_SEEDER_MARIADB_CONNECTION_PREFIX"
    echo "MARIADB_DATABASE                  : $DB_SEEDER_MARIADB_DATABASE"
    echo "MARIADB_PASSWORD                  : $DB_SEEDER_MARIADB_PASSWORD"
    echo "MARIADB_PASSWORD_SYS              : $DB_SEEDER_MARIADB_PASSWORD_SYS"
    echo "MARIADB_USER                      : $DB_SEEDER_MARIADB_USER"
fi
if [ "$DB_SEEDER_DATABASE_DBMS" = "mssqlserver" ]; then
    echo "MSSQLSERVER_CONNECTION_PORT       : $DB_SEEDER_MSSQLSERVER_CONNECTION_PORT"
    echo "MSSQLSERVER_CONNECTION_PREFIX     : $DB_SEEDER_MSSQLSERVER_CONNECTION_PREFIX"
    echo "MSSQLSERVER_DATABASE              : $DB_SEEDER_MSSQLSERVER_DATABASE"
    echo "MSSQLSERVER_PASSWORD              : $DB_SEEDER_MSSQLSERVER_PASSWORD"
    echo "MSSQLSERVER_PASSWORD_SYS          : $DB_SEEDER_MSSQLSERVER_PASSWORD_SYS"
    echo "MSSQLSERVER_SCHEMA                : $DB_SEEDER_MSSQLSERVER_SCHEMA"
    echo "MSSQLSERVER_USER                  : $DB_SEEDER_MSSQLSERVER_USER"
fi
if [ "$DB_SEEDER_DATABASE_DBMS" = "mysql" ]; then
    echo "MYSQL_CONNECTION_PORT             : $DB_SEEDER_MYSQL_CONNECTION_PORT"
    echo "MYSQL_CONNECTION_PREFIX           : $DB_SEEDER_MYSQL_CONNECTION_PREFIX"
    echo "MYSQL_CONNECTION_SUFFIX           : $DB_SEEDER_MYSQL_CONNECTION_SUFFIX"
    echo "MYSQL_DATABASE                    : $DB_SEEDER_MYSQL_DATABASE"
    echo "MYSQL_PASSWORD                    : $DB_SEEDER_MYSQL_PASSWORD"
    echo "MYSQL_PASSWORD_SYS                : $DB_SEEDER_MYSQL_PASSWORD_SYS"
    echo "MYSQL_USER                        : $DB_SEEDER_MYSQL_USER"
fi
if [ "$DB_SEEDER_DATABASE_DBMS" = "oracle" ]; then
    echo "ORACLE_CONNECTION_PORT            : $DB_SEEDER_ORACLE_CONNECTION_PORT"
    echo "ORACLE_CONNECTION_PREFIX          : $DB_SEEDER_ORACLE_CONNECTION_PREFIX"
    echo "ORACLE_CONNECTION_SERVICE         : $DB_SEEDER_ORACLE_CONNECTION_SSERVICE"
    echo "ORACLE_PASSWORD                   : $DB_SEEDER_ORACLE_PASSWORD"
    echo "ORACLE_PASSWORD_SYS               : $DB_SEEDER_ORACLE_PASSWORD_SYS"
    echo "ORACLE_USER                       : $DB_SEEDER_ORACLE_USER"
fi
if [ "$DB_SEEDER_DATABASE_DBMS" = "postgresql" ]; then
    echo "POSTGRESQL_CONNECTION_PORT        : $DB_SEEDER_POSTGRESQL_CONNECTION_PORT"
    echo "POSTGRESQL_CONNECTION_PREFIX      : $DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX"
    echo "POSTGRESQL_DATABASE               : $DB_SEEDER_POSTGRESQL_DATABASE"
    echo "POSTGRESQL_PASSWORD               : $DB_SEEDER_POSTGRESQL_PASSWORD"
    echo "POSTGRESQL_PASSWORD_SYS           : $DB_SEEDER_POSTGRESQL_PASSWORD_SYS"
    echo "POSTGRESQL_USER                   : $DB_SEEDER_POSTGRESQL_USER"
fi
if [ "$DB_SEEDER_DATABASE_DBMS" = "sqlite" ]; then
    echo "SQLITE_CONNECTION_PREFIX          : $DB_SEEDER_SQLITE_CONNECTION_PREFIX"
    echo "SQLITE_DATABASE                   : $DB_SEEDER_SQLITE_DATABASE"
fi

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

java --enable-preview -cp $DB_SEEDER_JAVA_CLASSPATH ch.konnexions.db_seeder.DatabaseSeeder $DB_SEEDER_DATABASE_DBMS

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
