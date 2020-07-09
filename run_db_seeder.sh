#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder.sh: Creation of dummy data in an empty database schema / user.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DBMS_DEFAULT=sqlite
export DB_SEEDER_DBMS_EMBEDDED=no

unset -f DB_SEEDER_FILE_STATISTICS_DELIMITER=
unset -f DB_SEEDER_FILE_STATISTICS_HEADER=
unset -f DB_SEEDER_FILE_STATISTICS_NAME=

if [ -z "$1" ]; then
    echo "==========================================="
    echo "derby       - Apache Derby [client]"
    echo "derby_emb   - Apache Derby [embedded]"
    echo "cratedb     - CrateDB"
    echo "cubrid      - CUBRID"
    echo "firebird    - Firebird"
    echo "h2          - H2 Database Engine [client]"
    echo "h2_emb      - H2 Database Engine [embedded]"
    echo "hsqldb      - HyperSQL Database [client]"
    echo "hsqldb_emb  - HyperSQL Database [embedded]"
    echo "ibmdb2      - IBM Db2 Database"
    echo "informix    - IBM Informix"
    echo "mariadb     - MariaDB Server"
    echo "mimer       - Mimer SQL"
    echo "mssqlserver - Microsoft SQL Server"
    echo "mysql       - MySQL"
    echo "oracle      - Oracle Database"
    echo "postgresql  - PostgreSQL Database"
    echo "sqlite      - SQLite [embedded]"
    echo "-------------------------------------------"
    read -p "Enter the desired database management system [default: $DB_SEEDER_DBMS_DEFAULT] " DB_SEEDER_DBMS
    export DB_SEEDER_DBMS=$DB_SEEDER_DBMS

    if [ -z "$DB_SEEDER_DBMS" ]; then
        export DB_SEEDER_DBMS=$DB_SEEDER_DBMS_DEFAULT
    fi
else
    export DB_SEEDER_DBMS=$1
fi

unset -f DB_SEEDER_ENCODING_ISO_8859_1=
unset -f DB_SEEDER_ENCODING_UTF_8=

export DB_SEEDER_FILE_CONFIGURATION_NAME=src/main/resources/db_seeder.properties

export DB_SEEDER_JAVA_CLASSPATH=".:lib/*:JAVA_HOME/lib"

unset -f DB_SEEDER_JDBC_CONNECTION_HOST=

unset -f DB_SEEDER_MAX_ROW_CITY=
unset -f DB_SEEDER_MAX_ROW_COMPANY=
unset -f DB_SEEDER_MAX_ROW_COUNTRY=
unset -f DB_SEEDER_MAX_ROW_COUNTRY_STATE=
unset -f DB_SEEDER_MAX_ROW_TIMEZONE=

if [ "$DB_SEEDER_DBMS" = "cratedb" ]; then
    unset -f DB_SEEDER_CRATEDB_CONNECTION_PORT=
    unset -f DB_SEEDER_CRATEDB_CONNETION_PREFIX=
    unset -f DB_SEEDER_CRATEDB_PASSWORD=
    unset -f DB_SEEDER_CRATEDB_USER=
    unset -f DB_SEEDER_CRATEDB_USER_SYS=
fi
if [ "$DB_SEEDER_DBMS" = "cubrid" ]; then
    export DB_SEEDER_ENCODING_UTF_8=false
    unset -f DB_SEEDER_CUBRID_CONNECTION_PORT=
    unset -f DB_SEEDER_CUBRID_CONNETION_PREFIX=
    unset -f DB_SEEDER_CUBRID_DATABASE=
    unset -f DB_SEEDER_CUBRID_PASSWORD=
    unset -f DB_SEEDER_CUBRID_USER=
fi
if [ "$DB_SEEDER_DBMS" = "derby" ]; then
    unset -f DB_SEEDER_DERBY_CONNECTION_PORT=
    unset -f DB_SEEDER_DERBY_CONNETION_PREFIX=
    unset -f DB_SEEDER_DERBY_DATABASE=
fi
if [ "$DB_SEEDER_DBMS" = "derby_emb" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
    unset -f DB_SEEDER_DERBY_CONNETION_PREFIX=
    unset -f DB_SEEDER_DERBY_DATABASE=
fi
if [ "$DB_SEEDER_DBMS" = "firebird" ]; then
    unset -f DB_SEEDER_FIREBIRD_CONNECTION_PORT=
    unset -f DB_SEEDER_FIREBIRD_CONNETION_PREFIX=
    unset -f DB_SEEDER_FIREBIRD_CONNETION_SUFFIX=
    unset -f DB_SEEDER_FIREBIRD_DATABASE=
    unset -f DB_SEEDER_FIREBIRD_PASSWORD=
    unset -f DB_SEEDER_FIREBIRD_PASSWORD_SYS=
    unset -f DB_SEEDER_FIREBIRD_USER=
    unset -f DB_SEEDER_FIREBIRD_USER_SYS=
fi
if [ "$DB_SEEDER_DBMS" = "h2" ]; then
    unset -f DB_SEEDER_H2_CONNECTION_PORT=
    unset -f DB_SEEDER_H2_CONNETION_PREFIX=
    unset -f DB_SEEDER_H2_DATABASE=
    unset -f DB_SEEDER_H2_PASSWORD=
    unset -f DB_SEEDER_H2_SCHEMA=
    unset -f DB_SEEDER_H2_USER=
fi
if [ "$DB_SEEDER_DBMS" = "h2_emb" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
    unset -f DB_SEEDER_H2_CONNETION_PREFIX=
    unset -f DB_SEEDER_H2_DATABASE=
    unset -f DB_SEEDER_H2_PASSWORD=
    unset -f DB_SEEDER_H2_SCHEMA=
    unset -f DB_SEEDER_H2_USER=
fi
if [ "$DB_SEEDER_DBMS" = "hsqldb_emb" ]; then
    export DB_SEEDER_DBMS_EMBEDDED=yes
    unset -f DB_SEEDER_HSQLDB_CONNETION_PREFIX=
    unset -f DB_SEEDER_HSQLDB_CONNETION_SUFFIX=
    unset -f DB_SEEDER_HSQLDB_DATABASE=
    unset -f DB_SEEDER_HSQLDB_PASSWORD=
    unset -f DB_SEEDER_HSQLDB_SCHEMA=
    unset -f DB_SEEDER_HSQLDB_USER=
fi
if [ "$DB_SEEDER_DBMS" = "ibmdb2" ]; then
    unset -f DB_SEEDER_IBMDB2_CONNECTION_PORT=
    unset -f DB_SEEDER_IBMDB2_CONNETION_PREFIX=
    unset -f DB_SEEDER_IBMDB2_DATABASE=
    unset -f DB_SEEDER_IBMDB2_PASSWORD_SYS=
    unset -f DB_SEEDER_IBMDB2_SCHEMA=
    unset -f DB_SEEDER_IBMDB2_USER_SYS=
fi
if [ "$DB_SEEDER_DBMS" = "informix" ]; then
    unset -f DB_SEEDER_INFORMIX_CONNECTION_PORT=
    unset -f DB_SEEDER_INFORMIX_CONNETION_PREFIX=
    unset -f DB_SEEDER_INFORMIX_CONNETION_SUFFIX=
    unset -f DB_SEEDER_INFORMIX_DATABASE=
    unset -f DB_SEEDER_INFORMIX_DATABASE_SYS=
    unset -f DB_SEEDER_INFORMIX_PASSWORD_SYS=
    unset -f DB_SEEDER_INFORMIX_USER_SYS=
fi
if [ "$DB_SEEDER_DBMS" = "mariadb" ]; then
    unset -f DB_SEEDER_MARIADB_CONNECTION_PORT=
    unset -f DB_SEEDER_MARIADB_CONNETION_PREFIX=
    unset -f DB_SEEDER_MARIADB_DATABASE=
    unset -f DB_SEEDER_MARIADB_DATABASE_SYS=
    unset -f DB_SEEDER_MARIADB_PASSWORD=
    unset -f DB_SEEDER_MARIADB_PASSWORD_SYS=
    unset -f DB_SEEDER_MARIADB_USER=
    unset -f DB_SEEDER_MARIADB_USER_SYS=
fi
if [ "$DB_SEEDER_DBMS" = "mimer" ]; then
    unset -f DB_SEEDER_MIMER_CONNECTION_PORT=
    unset -f DB_SEEDER_MIMER_CONNETION_PREFIX=
    unset -f DB_SEEDER_MIMER_DATABASE=
    unset -f DB_SEEDER_MIMER_DATABASE_SYS=
    unset -f DB_SEEDER_MIMER_PASSWORD=
    unset -f DB_SEEDER_MIMER_PASSWORD_SYS=
    unset -f DB_SEEDER_MIMER_USER=
    unset -f DB_SEEDER_MIMER_USER_SYS=
fi
if [ "$DB_SEEDER_DBMS" = "mssqlserver" ]; then
    export DB_SEEDER_ENCODING_UTF_8=false
    unset -f DB_SEEDER_MSSQLSERVER_CONNECTION_PORT=
    unset -f DB_SEEDER_MSSQLSERVER_CONNETION_PREFIX=
    unset -f DB_SEEDER_MSSQLSERVER_DATABASE=
    unset -f DB_SEEDER_MSSQLSERVER_DATABASE_SYS=
    unset -f DB_SEEDER_MSSQLSERVER_PASSWORD=
    unset -f DB_SEEDER_MSSQLSERVER_PASSWORD_SYS=
    unset -f DB_SEEDER_MSSQLSERVER_SCHEMA=
    unset -f DB_SEEDER_MSSQLSERVER_USER=
    unset -f DB_SEEDER_MSSQLSERVER_USER_SYS=
fi
if [ "$DB_SEEDER_DBMS" = "mysql" ]; then
    unset -f DB_SEEDER_MYSQL_CONNECTION_PORT=
    unset -f DB_SEEDER_MYSQL_CONNETION_PREFIX=
    unset -f DB_SEEDER_MYSQL_CONNETION_SUFFIX=
    unset -f DB_SEEDER_MYSQL_DATABASE=
    unset -f DB_SEEDER_MYSQL_DATABASE_SYS=
    unset -f DB_SEEDER_MYSQL_PASSWORD=
    unset -f DB_SEEDER_MYSQL_PASSWORD_SYS=
    unset -f DB_SEEDER_MYSQL_USER=
    unset -f DB_SEEDER_MYSQL_USER_SYS=
fi
if [ "$DB_SEEDER_DBMS" = "oracle" ]; then
    unset -f DB_SEEDER_ORACLE_CONNECTION_PORT=
    unset -f DB_SEEDER_ORACLE_CONNETION_PREFIX=
    unset -f DB_SEEDER_ORACLE_CONNETION_SERVICE=
    unset -f DB_SEEDER_ORACLE_PASSWORD=
    unset -f DB_SEEDER_ORACLE_PASSWORD_SYS=
    unset -f DB_SEEDER_ORACLE_USER=
    unset -f DB_SEEDER_ORACLE_USER_SYS=
fi
if [ "$DB_SEEDER_DBMS" = "postgresql" ]; then
    unset -f DB_SEEDER_POSTGRESQL_CONNECTION_PORT=
    unset -f DB_SEEDER_POSTGRESQL_CONNETION_PREFIX=
    unset -f DB_SEEDER_POSTGRESQL_DATABASE=
    unset -f DB_SEEDER_POSTGRESQL_DATABASE_SYS=
    unset -f DB_SEEDER_POSTGRESQL_PASSWORD=
    unset -f DB_SEEDER_POSTGRESQL_PASSWORD_SYS=
    unset -f DB_SEEDER_POSTGRESQL_USER=
    unset -f DB_SEEDER_POSTGRESQL_USER_SYS=
fi
if [ "$DB_SEEDER_DBMS" = "sqlite" ]; then
    unset -f DB_SEEDER_SQLITE_CONNETION_PREFIX=
    unset -f DB_SEEDER_SQLITE_DATABASE=
fi

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Creation of dummy data in an empty database schema / user."
echo "--------------------------------------------------------------------------------"
echo "DBMS                              : $DB_SEEDER_DBMS"
echo "DBMS_EMBEDDED                     : $DB_SEEDER_DBMS_EMBEDDED"
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
if [ "$DB_SEEDER_DBMS" = "cratedb" ]; then
    echo "CRATEDB_CONNECTION_PORT           : $DB_SEEDER_CRATEDB_CONNECTION_PORT"
    echo "CRATEDB_CONNECTION_PREFIX         : $DB_SEEDER_CRATEDB_CONNECTION_PREFIX"
    echo "CRATEDB_PASSWORD                  : $DB_SEEDER_CRATEDB_PASSWORD"
    echo "CRATEDB_USER                      : $DB_SEEDER_CRATEDB_USER"
    echo "CRATEDB_USER_SYS                  : $DB_SEEDER_CRATEDB_USER_SYS"
fi
if [ "$DB_SEEDER_DBMS" = "cubrid" ]; then
    echo "CUBRID_CONNECTION_PORT            : $DB_SEEDER_CUBRID_CONNECTION_PORT"
    echo "CUBRID_CONNECTION_PREFIX          : $DB_SEEDER_CUBRID_CONNECTION_PREFIX"
    echo "CUBRID_CONNECTION_SUFFIX          : $DB_SEEDER_CUBRID_CONNECTION_SUFFIX"
    echo "CUBRID_DATABASE                   : $DB_SEEDER_CUBRID_DATABASE"
    echo "CUBRID_PASSWORD                   : $DB_SEEDER_CUBRID_PASSWORD"
    echo "CUBRID_PASSWORD_SYS               : $DB_SEEDER_CUBRID_PASSWORD_SYS"
    echo "CUBRID_USER                       : $DB_SEEDER_CUBRID_USER"
    echo "CUBRID_USER_SYS                   : $DB_SEEDER_CUBRID_USER_SYS"
fi
if [ "$DB_SEEDER_DBMS" = "derby" ]; then
    echo "DERBY_CONNECTION_PORT             : $DB_SEEDER_DERBY_CONNECTION_PORT"
    echo "DERBY_CONNECTION_PREFIX           : $DB_SEEDER_DERBY_CONNECTION_PREFIX"
    echo "DERBY_DATABASE                    : $DB_SEEDER_DERBY_DATABASE"
fi
if [ "$DB_SEEDER_DBMS" = "derby_emb" ]; then
    echo "DERBY_CONNECTION_PREFIX           : $DB_SEEDER_DERBY_CONNECTION_PREFIX"
    echo "DERBY_DATABASE                    : $DB_SEEDER_DERBY_DATABASE"
fi
if [ "$DB_SEEDER_DBMS" = "firebird" ]; then
    echo "FIREBIRD_CONNECTION_PORT          : $DB_SEEDER_FIREBIRD_CONNECTION_PORT"
    echo "FIREBIRD_CONNECTION_PREFIX        : $DB_SEEDER_FIREBIRD_CONNECTION_PREFIX"
    echo "FIREBIRD_CONNECTION_SUFFIX        : $DB_SEEDER_FIREBIRD_CONNECTION_SUFFIX"
    echo "FIREBIRD_DATABASE                 : $DB_SEEDER_FIREBIRD_DATABASE"
    echo "FIREBIRD_PASSWORD                 : $DB_SEEDER_FIREBIRD_PASSWORD"
    echo "FIREBIRD_PASSWORD_SYS             : $DB_SEEDER_FIREBIRD_PASSWORD_SYS"
    echo "FIREBIRD_USER                     : $DB_SEEDER_FIREBIRD_USER"
    echo "FIREBIRD_USER_SYS                 : $DB_SEEDER_FIREBIRD_USER_SYS"
fi
if [ "$DB_SEEDER_DBMS" = "h2" ]; then
    echo "H2_CONNECTION_PORT                : $DB_SEEDER_H2_CONNECTION_PORT"
    echo "H2_CONNECTION_PREFIX              : $DB_SEEDER_H2_CONNECTION_PREFIX"
    echo "H2_DATABASE                       : $DB_SEEDER_H2_DATABASE"
    echo "H2_PASSWORD                       : $DB_SEEDER_H2_PASSWORD"
    echo "H2_SCHEMA                         : $DB_SEEDER_H2_SCHEMA"
    echo "H2_USER                           : $DB_SEEDER_H2_USER"
fi
if [ "$DB_SEEDER_DBMS" = "h2_emb" ]; then
    echo "H2_CONNECTION_PREFIX              : $DB_SEEDER_H2_CONNECTION_PREFIX"
    echo "H2_CONNECTION_SUFFIX              : $DB_SEEDER_H2_CONNECTION_SUFFIX"
    echo "H2_DATABASE                       : $DB_SEEDER_H2_DATABASE"
    echo "H2_PASSWORD                       : $DB_SEEDER_H2_PASSWORD"
    echo "H2_SCHEMA                         : $DB_SEEDER_H2_SCHEMA"
    echo "H2_USER                           : $DB_SEEDER_H2_USER"
fi
if [ "$DB_SEEDER_DBMS" = "hsqldb" ]; then
    echo "HSQLDB_CONNECTION_PORT            : $DB_SEEDER_HSQLDB_CONNECTION_PORT"
    echo "HSQLDB_CONNECTION_PREFIX          : $DB_SEEDER_HSQLDB_CONNECTION_PREFIX"
    echo "HSQLDB_CONNECTION_SUFFIX          : $DB_SEEDER_HSQLDB_CONNECTION_SUFFIX"
    echo "HSQLDB_DATABASE                   : $DB_SEEDER_HSQLDB_DATABASE"
    echo "HSQLDB_PASSWORD                   : $DB_SEEDER_HSQLDB_PASSWORD"
    echo "HSQLDB_SCHEMA                     : $DB_SEEDER_HSQLDB_SCHEMA"
    echo "HSQLDB_USER                       : $DB_SEEDER_HSQLDB_USER"
    echo "HSQLDB_USER_SYS                   : $DB_SEEDER_HSQLDB_USER_SYS"
fi
if [ "$DB_SEEDER_DBMS" = "hsqldb_emb" ]; then
    echo "HSQLDB_CONNECTION_PREFIX          : $DB_SEEDER_HSQLDB_CONNECTION_PREFIX"
    echo "HSQLDB_CONNECTION_SUFFIX          : $DB_SEEDER_HSQLDB_CONNECTION_SUFFIX"
    echo "HSQLDB_DATABASE                   : $DB_SEEDER_HSQLDB_DATABASE"
    echo "HSQLDB_PASSWORD                   : $DB_SEEDER_HSQLDB_PASSWORD"
    echo "HSQLDB_SCHEMA                     : $DB_SEEDER_HSQLDB_SCHEMA"
    echo "HSQLDB_USER_SYS                   : $DB_SEEDER_HSQLDB_USER_SYS"
    echo "HSQLDB_USER                       : $DB_SEEDER_HSQLDB_USER"
fi
if [ "$DB_SEEDER_DBMS" = "ibmdb2" ]; then
    echo "IBMDB2_CONNECTION_PORT            : $DB_SEEDER_IBMDB2_CONNECTION_PORT"
    echo "IBMDB2_CONNECTION_PREFIX          : $DB_SEEDER_IBMDB2_CONNECTION_PREFIX"
    echo "IBMDB2_DATABASE                   : $DB_SEEDER_IBMDB2_DATABASE"
    echo "IBMDB2_PASSWORD_SYS               : $DB_SEEDER_IBMDB2_PASSWORD_SYS"
    echo "IBMDB2_SCHEMA                     : $DB_SEEDER_IBMDB2_SCHEMA"
    echo "IBMDB2_USER_SYS                   : $DB_SEEDER_IBMDB2_USER_SYS"
fi
if [ "$DB_SEEDER_DBMS" = "informix" ]; then
    echo "INFORMIX_CONNECTION_PORT          : $DB_SEEDER_INFORMIX_CONNECTION_PORT"
    echo "INFORMIX_CONNECTION_PREFIX        : $DB_SEEDER_INFORMIX_CONNECTION_PREFIX"
    echo "INFORMIX_CONNECTION_SUFFIX        : $DB_SEEDER_INFORMIX_CONNECTION_SUFFIX"
    echo "INFORMIX_DATABASE                 : $DB_SEEDER_INFORMIX_DATABASE"
    echo "INFORMIX_DATABASE_SYS             : $DB_SEEDER_INFORMIX_DATABASE.SYS"
    echo "INFORMIX_PASSWORD_SYS             : $DB_SEEDER_INFORMIX_PASSWORD.SYS"
    echo "INFORMIX_USER_SYS                 : $DB_SEEDER_INFORMIX_USER.SYS"
fi
if [ "$DB_SEEDER_DBMS" = "mariadb" ]; then
    echo "MARIADB_CONNECTION_PORT           : $DB_SEEDER_MARIADB_CONNECTION_PORT"
    echo "MARIADB_CONNECTION_PREFIX         : $DB_SEEDER_MARIADB_CONNECTION_PREFIX"
    echo "MARIADB_DATABASE                  : $DB_SEEDER_MARIADB_DATABASE"
    echo "MARIADB_DATABASE_SYS              : $DB_SEEDER_MARIADB_DATABASE_SYS"
    echo "MARIADB_PASSWORD                  : $DB_SEEDER_MARIADB_PASSWORD"
    echo "MARIADB_PASSWORD_SYS              : $DB_SEEDER_MARIADB_PASSWORD_SYS"
    echo "MARIADB_USER                      : $DB_SEEDER_MARIADB_USER"
    echo "MARIADB_USER_SYS                  : $DB_SEEDER_MARIADB_USER_SYS"
fi
if [ "$DB_SEEDER_DBMS" = "mimer" ]; then
    echo "MIMER_CONNECTION_PORT             : $DB_SEEDER_MIMER_CONNECTION_PORT"
    echo "MIMER_CONNECTION_PREFIX           : $DB_SEEDER_MIMER_CONNECTION_PREFIX"
    echo "MIMER_DATABASE                    : $DB_SEEDER_MIMER_DATABASE"
    echo "MIMER_DATABASE_SYS                : $DB_SEEDER_MIMER_DATABASE_SYS"
    echo "MIMER_PASSWORD                    : $DB_SEEDER_MIMER_PASSWORD"
    echo "MIMER_PASSWORD_SYS                : $DB_SEEDER_MIMER_PASSWORD_SYS"
    echo "MIMER_USER                        : $DB_SEEDER_MIMER_USER"
    echo "MIMER_USER_SYS                    : $DB_SEEDER_MIMER_USER_SYS"
fi
if [ "$DB_SEEDER_DBMS" = "mssqlserver" ]; then
    echo "MSSQLSERVER_CONNECTION_PORT       : $DB_SEEDER_MSSQLSERVER_CONNECTION_PORT"
    echo "MSSQLSERVER_CONNECTION_PREFIX     : $DB_SEEDER_MSSQLSERVER_CONNECTION_PREFIX"
    echo "MSSQLSERVER_DATABASE              : $DB_SEEDER_MSSQLSERVER_DATABASE"
    echo "MSSQLSERVER_DATABASE_SYS          : $DB_SEEDER_MSSQLSERVER_DATABASE_SYS"
    echo "MSSQLSERVER_PASSWORD              : $DB_SEEDER_MSSQLSERVER_PASSWORD"
    echo "MSSQLSERVER_PASSWORD_SYS          : $DB_SEEDER_MSSQLSERVER_PASSWORD_SYS"
    echo "MSSQLSERVER_SCHEMA                : $DB_SEEDER_MSSQLSERVER_SCHEMA"
    echo "MSSQLSERVER_USER                  : $DB_SEEDER_MSSQLSERVER_USER"
    echo "MSSQLSERVER_USER_SYS              : $DB_SEEDER_MSSQLSERVER_USER_SYS"
fi
if [ "$DB_SEEDER_DBMS" = "mysql" ]; then
    echo "MYSQL_CONNECTION_PORT             : $DB_SEEDER_MYSQL_CONNECTION_PORT"
    echo "MYSQL_CONNECTION_PREFIX           : $DB_SEEDER_MYSQL_CONNECTION_PREFIX"
    echo "MYSQL_CONNECTION_SUFFIX           : $DB_SEEDER_MYSQL_CONNECTION_SUFFIX"
    echo "MYSQL_DATABASE                    : $DB_SEEDER_MYSQL_DATABASE"
    echo "MYSQL_DATABASE_SYS                : $DB_SEEDER_MYSQL_DATABASE_SYS"
    echo "MYSQL_PASSWORD                    : $DB_SEEDER_MYSQL_PASSWORD"
    echo "MYSQL_PASSWORD_SYS                : $DB_SEEDER_MYSQL_PASSWORD_SYS"
    echo "MYSQL_USER                        : $DB_SEEDER_MYSQL_USER"
    echo "MYSQL_USER_SYS                    : $DB_SEEDER_MYSQL_USER_SYS"
fi
if [ "$DB_SEEDER_DBMS" = "oracle" ]; then
    echo "ORACLE_CONNECTION_PORT            : $DB_SEEDER_ORACLE_CONNECTION_PORT"
    echo "ORACLE_CONNECTION_PREFIX          : $DB_SEEDER_ORACLE_CONNECTION_PREFIX"
    echo "ORACLE_CONNECTION_SERVICE         : $DB_SEEDER_ORACLE_CONNECTION_SSERVICE"
    echo "ORACLE_PASSWORD                   : $DB_SEEDER_ORACLE_PASSWORD"
    echo "ORACLE_PASSWORD_SYS               : $DB_SEEDER_ORACLE_PASSWORD_SYS"
    echo "ORACLE_USER                       : $DB_SEEDER_ORACLE_USER"
    echo "ORACLE_USER_SYS                   : $DB_SEEDER_ORACLE_USER_SYS"
fi
if [ "$DB_SEEDER_DBMS" = "postgresql" ]; then
    echo "POSTGRESQL_CONNECTION_PORT        : $DB_SEEDER_POSTGRESQL_CONNECTION_PORT"
    echo "POSTGRESQL_CONNECTION_PREFIX      : $DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX"
    echo "POSTGRESQL_DATABASE               : $DB_SEEDER_POSTGRESQL_DATABASE"
    echo "POSTGRESQL_DATABASE_SYS           : $DB_SEEDER_POSTGRESQL_DATABASE_SYS"
    echo "POSTGRESQL_PASSWORD               : $DB_SEEDER_POSTGRESQL_PASSWORD"
    echo "POSTGRESQL_PASSWORD_SYS           : $DB_SEEDER_POSTGRESQL_PASSWORD_SYS"
    echo "POSTGRESQL_USER                   : $DB_SEEDER_POSTGRESQL_USER"
    echo "POSTGRESQL_USER_SYS               : $DB_SEEDER_POSTGRESQL_USER_SYS"
fi
if [ "$DB_SEEDER_DBMS" = "sqlite" ]; then
    echo "SQLITE_CONNECTION_PREFIX          : $DB_SEEDER_SQLITE_CONNECTION_PREFIX"
    echo "SQLITE_DATABASE                   : $DB_SEEDER_SQLITE_DATABASE"
fi

echo "--------------------------------------------------------------------------------"
echo "FILE_STATISTICS_DELIMITER         : $DB_SEEDER_FILE_STATISTICS_DELIMITER"
echo "FILE_STATISTICS_HEADER            : $DB_SEEDER_FILE_STATISTICS_HEADER"
echo "FILE_STATISTICS_NAME              : $DB_SEEDER_FILE_STATISTICS_NAME"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

rm -f db_seeder.log

java --enable-preview -cp $DB_SEEDER_JAVA_CLASSPATH ch.konnexions.db_seeder.DatabaseSeeder $DB_SEEDER_DBMS

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
