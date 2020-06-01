#!/bin/bash

export -e

# ------------------------------------------------------------------------------
#
# run_db_seeder.sh: Creation of dummy data in an empty database schema / user.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_DATABASE_BRAND_DEFAULT=oracle

export DB_SEEDER_FILE_CONFIGURATION_NAME=src/main/resources/db_seeder.properties

export DB_SEEDER_JAVA_CLASSPATH=".:lib/*:JAVA_HOME/lib"

export DB_SEEDER_JDBC_CONNECTION_HOST=192.168.1.109

# export DB_SEEDER_MAX_ROW_CITY=
# export DB_SEEDER_MAX_ROW_COMPANY=
# export DB_SEEDER_MAX_ROW_COUNTRY=
# export DB_SEEDER_MAX_ROW_COUNTRY_STATE=
# export DB_SEEDER_MAX_ROW_TIMEZONE=

# export DB_SEEDER_MYSQL_CONNETION_DATABASE=
# export DB_SEEDER_MYSQL_CONNECTION_PORT=
# export DB_SEEDER_MYSQL_CONNETION_PREFIX=
# export DB_SEEDER_MYSQL_CONNETION_SUFFIX=
# export DB_SEEDER_MYSQL_PASSWORD=
# export DB_SEEDER_MYSQL_PASSWORD_SYS=
# export DB_SEEDER_MYSQL_USER=
# export DB_SEEDER_MYSQL_USER_SYS=

# export DB_SEEDER_ORACLE_CONNECTION_PORT=
# export DB_SEEDER_ORACLE_CONNETION_PREFIX=
# export DB_SEEDER_ORACLE_CONNETION_SERVICE=
# export DB_SEEDER_ORACLE_CONNETION_SUFFIX=
# export DB_SEEDER_ORACLE_PASSWORD=
# export DB_SEEDER_ORACLE_PASSWORD_SYS=
# export DB_SEEDER_ORACLE_USER=
# export DB_SEEDER_ORACLE_USER_SYS=

if [ -z "$1" ]; then
    read -p "Enter the desired database brand (mysql/oracle) [default: $DB_SEEDER_DATABASE_BRAND_DEFAULT] " DB_SEEDER_DATABASE_BRAND
    export DB_SEEDER_DATABASE_BRAND=$DB_SEEDER_DATABASE_BRAND

    if [ -z "$DB_SEEDER_DATABASE_BRAND" ]; then
        export DB_SEEDER_DATABASE_BRAND=$DB_SEEDER_DATABASE_BRAND_DEFAULT
    fi
else
    export DB_SEEDER_DATABASE_BRAND=$1
fi

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Creation of dummy data in an empty database schema / user."
echo "--------------------------------------------------------------------------------"
echo "DATABASE_BRAND           : $DB_SEEDER_DATABASE_BRAND"
echo --------------------------------------------------------------------------------
echo "FILE_CONFIGURATION_NAME  : $DB_SEEDER_FILE_CONFIGURATION_NAME"
echo "JAVA_CLASSPATH           : $DB_SEEDER_JAVA_CLASSPATH"
echo "--------------------------------------------------------------------------------"
echo "CONNECTION_HOST          : $DB_SEEDER_JDBC_CONNECTION_HOST"
echo "--------------------------------------------------------------------------------"
echo "MAX_ROW_CITY             : $DB_SEEDER_MAX_ROW_CITY"
echo "MAX_ROW_COMPANY          : $DB_SEEDER_MAX_ROW_COMPANY"
echo "MAX_ROW_COUNTRY          : $DB_SEEDER_MAX_ROW_COUNTRY"
echo "MAX_ROW_COUNTRY_STATE    : $DB_SEEDER_MAX_ROW_COUNTRY_STATE"
echo "MAX_ROW_TIMEZONE         : $DB_SEEDER_MAX_ROW_TIMEZONE"
echo "--------------------------------------------------------------------------------"
if [ "$DB_SEEDER_DATABASE_BRAND" = "mysql" ]; then
    echo "CONNECTION_DATABASE      : $DB_SEEDER_MYSQL_CONNECTION_DATABASE"
    echo "CONNECTION_PORT          : $DB_SEEDER_MYSQL_CONNECTION_PORT"
    echo "CONNECTION_PREFIX        : $DB_SEEDER_MYSQL_CONNECTION_PREFIX"
    echo "CONNECTION_SUFFIX        : $DB_SEEDER_MYSQL_CONNECTION_SUFFIX"
    echo "MYSQL_USER               : $DB_SEEDER_MYSQL_USER"
    echo "MYSQL_USER_PASSWORD      : $DB_SEEDER_MYSQL_PASSWORD"
    echo "MYSQL_USER_SYS           : $DB_SEEDER_MYSQL_USER_SYS"
    echo "MYSQL_USER_PASSWORD_SYS  : $DB_SEEDER_MYSQL_PASSWORD_SYS"
fi
if [ "$DB_SEEDER_DATABASE_BRAND" = "oracle" ]; then
    echo "CONNECTION_PORT          : $DB_SEEDER_ORACLE_CONNECTION_PORT"
    echo "CONNECTION_PREFIX        : $DB_SEEDER_ORACLE_CONNECTION_PREFIX"
    echo "CONNECTION_SERVICE       : $DB_SEEDER_ORACLE_CONNECTION_SERVICE"
    echo "ORACLE_USER              : $DB_SEEDER_ORACLE_USER"
    echo "ORACLE_USER_PASSWORD     : $DB_SEEDER_ORACLE_PASSWORD"
    echo "ORACLE_USER_SYS          : $DB_SEEDER_ORACLE_USER_SYS"
    echo "ORACLE_USER_PASSWORD_SYS : $DB_SEEDER_ORACLE_PASSWORD_SYS"
fi
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

java --enable-preview -cp $DB_SEEDER_JAVA_CLASSPATH ch.konnexions.db_seeder.DatabaseSeeder $DB_SEEDER_DATABASE_BRAND

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
