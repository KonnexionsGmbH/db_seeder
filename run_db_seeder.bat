@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder.bat: Creation of dummy data in an empty database schema / user.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_DATABASE_BRAND_DEFAULT=oracle

if ["%1"] EQU [""] (
    echo ====================================
    echo ibmdb2      - IBM DB2 Database
    echo mariadb     - MariaDB Server
    echo mssqlserver - Microsoft SQL Server
    echo mysql       - MySQL
    echo oracle      - Oracle Database
    echo postgresql  - PostgreSQL Database
    echo ------------------------------------
    set /P DB_SEEDER_DATABASE_BRAND="Enter the desired database brand [default: %DB_SEEDER_DATABASE_BRAND_DEFAULT%] "

    if ["!DB_SEEDER_DATABASE_BRAND!"] EQU [""] (
        set DB_SEEDER_DATABASE_BRAND=%DB_SEEDER_DATABASE_BRAND_DEFAULT%
    )
) else (
    set DB_SEEDER_DATABASE_BRAND=%1
)

set DB_SEEDER_FILE_CONFIGURATION_NAME=src\main\resources\db_seeder.properties

set DB_SEEDER_JAVA_CLASSPATH=%CLASSPATH%;lib/*

set DB_SEEDER_JDBC_CONNECTION_HOST=

set DB_SEEDER_MAX_ROW_CITY=
set DB_SEEDER_MAX_ROW_COMPANY=
set DB_SEEDER_MAX_ROW_COUNTRY=
set DB_SEEDER_MAX_ROW_COUNTRY_STATE=
set DB_SEEDER_MAX_ROW_TIMEZONE=

if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["ibmdb2"] (
    set DB_SEEDER_IBMDB2_CONNECTION_PORT=
    set DB_SEEDER_IBMDB2_CONNECTION_PREFIX=
    set DB_SEEDER_IBMDB2_DATABASE=
    set DB_SEEDER_IBMDB2_PASSWORD=
    set DB_SEEDER_IBMDB2_SCHEMA=
)
if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["mariadb"] (
    set DB_SEEDER_MARIADB_CONNECTION_PORT=
    set DB_SEEDER_MARIADB_CONNECTION_PREFIX=
    set DB_SEEDER_MARIADB_DATABASE=
    set DB_SEEDER_MARIADB_PASSWORD=
    set DB_SEEDER_MARIADB_PASSWORD_SYS=
    set DB_SEEDER_MARIADB_USER=
)
if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["mssqlserver"] (
    set DB_SEEDER_MSSQLSERVER_CONNECTION_PORT=
    set DB_SEEDER_MSSQLSERVER_CONNECTION_PREFIX=
    set DB_SEEDER_MSSQLSERVER_DATABASE=
    set DB_SEEDER_MSSQLSERVER_PASSWORD=
    set DB_SEEDER_MSSQLSERVER_PASSWORD_SYS=
    set DB_SEEDER_MSSQLSERVER_SCHEMA=
    set DB_SEEDER_MSSQLSERVER_USER=
)
if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["mysql"] (
    set DB_SEEDER_MYSQL_CONNECTION_PORT=
    set DB_SEEDER_MYSQL_CONNECTION_PREFIX=
    set DB_SEEDER_MYSQL_CONNECTION_SUFFIX=
    set DB_SEEDER_MYSQL_DATABASE=
    set DB_SEEDER_MYSQL_PASSWORD=
    set DB_SEEDER_MYSQL_PASSWORD_SYS=
    set DB_SEEDER_MYSQL_USER=
)
if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["oracle"] (
    set DB_SEEDER_ORACLE_CONNECTION_PORT=
    set DB_SEEDER_ORACLE_CONNECTION_PREFIX=
    set DB_SEEDER_ORACLE_CONNECTION_SERVICE=
    set DB_SEEDER_ORACLE_PASSWORD=
    set DB_SEEDER_ORACLE_PASSWORD_SYS=
    set DB_SEEDER_ORACLE_USER=
)
if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["postgresql"] (
    set DB_SEEDER_POSTGRESQL_CONNECTION_PORT=
    set DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX=
    set DB_SEEDER_POSTGRESQL_DATABASE=
    set DB_SEEDER_POSTGRESQL_PASSWORD=
    set DB_SEEDER_POSTGRESQL_PASSWORD_SYS=
    set DB_SEEDER_POSTGRESQL_USER=
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - Creation of dummy data in an empty database schema / user.
echo --------------------------------------------------------------------------------
echo DATABASE_BRAND                  : %DB_SEEDER_DATABASE_BRAND%
echo --------------------------------------------------------------------------------
echo FILE_CONFIGURATION_NAME         : %DB_SEEDER_FILE_CONFIGURATION_NAME%
echo JAVA_CLASSPATH                  : %DB_SEEDER_JAVA_CLASSPATH%
echo --------------------------------------------------------------------------------
echo CONNECTION_HOST                 : %DB_SEEDER_JDBC_CONNECTION_HOST%
echo --------------------------------------------------------------------------------
echo MAX_ROW_CITY                    : %DB_SEEDER_MAX_ROW_CITY%
echo MAX_ROW_COMPANY                 : %DB_SEEDER_MAX_ROW_COMPANY%
echo MAX_ROW_COUNTRY                 : %DB_SEEDER_MAX_ROW_COUNTRY%
echo MAX_ROW_COUNTRY_STATE           : %DB_SEEDER_MAX_ROW_COUNTRY_STATE%
echo MAX_ROW_TIMEZONE                : %DB_SEEDER_MAX_ROW_TIMEZONE%
echo --------------------------------------------------------------------------------
if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["ibmdb2"] (
    echo IBMDB2_CONNECTION_PORT          : %DB_SEEDER_IBMDB2_CONNECTION_PORT%
    echo IBMDB2_CONNECTION_PREFIX        : %DB_SEEDER_IBMDB2_CONNECTION_PREFIX%
    echo IBMDB2_DATABASE                 : %DB_SEEDER_IBMDB2_DATABASE%
    echo IBMDB2_PASSWORD                 : %DB_SEEDER_IBMDB2_PASSWORD%
    echo IBMDB2_SCHEMA                   : %DB_SEEDER_IBMDB2_SCHEMA%
)
if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["mariadb"] (
    echo MARIADB_CONNECTION_PORT         : %DB_SEEDER_MARIADB_CONNECTION_PORT%
    echo MARIADB_CONNECTION_PREFIX       : %DB_SEEDER_MARIADB_CONNECTION_PREFIX%
    echo MARIADB_DATABASE                : %DB_SEEDER_MARIADB_DATABASE%
    echo MARIADB_PASSWORD                : %DB_SEEDER_MARIADB_PASSWORD%
    echo MARIADB_PASSWORD_SYS            : %DB_SEEDER_MARIADB_PASSWORD_SYS%
    echo MARIADB_USER                    : %DB_SEEDER_MARIADB_USER%
)
if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["mssqlserver"] (
    echo MSSQLSERVER_CONNECTION_PORT     : %DB_SEEDER_MSSQLSERVER_CONNECTION_PORT%
    echo MSSQLSERVER_CONNECTION_PREFIX   : %DB_SEEDER_MSSQLSERVER_CONNECTION_PREFIX%
    echo MSSQLSERVER_DATABASE            : %DB_SEEDER_MSSQLSERVER_DATABASE%
    echo MSSQLSERVER_PASSWORD            : %DB_SEEDER_MSSQLSERVER_PASSWORD%
    echo MSSQLSERVER_PASSWORD_SYS        : %DB_SEEDER_MSSQLSERVER_PASSWORD_SYS%
    echo MSSQLSERVER_SCHEMA              : %DB_SEEDER_MSSQLSERVER_SCHEMA%
    echo MSSQLSERVER_USER                : %DB_SEEDER_MSSQLSERVER_USER%
)
if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["mysql"] (
    echo MYSQL_CONNECTION_PORT           : %DB_SEEDER_MYSQL_CONNECTION_PORT%
    echo MYSQL_CONNECTION_PREFIX         : %DB_SEEDER_MYSQL_CONNECTION_PREFIX%
    echo MYSQL_CONNECTION_SUFFIX         : %DB_SEEDER_MYSQL_CONNECTION_SUFFIX%
    echo MYSQL_DATABASE                  : %DB_SEEDER_MYSQL_DATABASE%
    echo MYSQL_PASSWORD                  : %DB_SEEDER_MYSQL_PASSWORD%
    echo MYSQL_PASSWORD_SYS              : %DB_SEEDER_MYSQL_PASSWORD_SYS%
    echo MYSQL_USER                      : %DB_SEEDER_MYSQL_USER%
)
if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["oracle"] (
    echo ORACLE_CONNECTION_PORT          : %DB_SEEDER_ORACLE_CONNECTION_PORT%
    echo ORACLE_CONNECTION_PREFIX        : %DB_SEEDER_ORACLE_CONNECTION_PREFIX%
    echo ORACLE_CONNECTION_SERVICE       : %DB_SEEDER_ORACLE_CONNECTION_SERVICE%
    echo ORACLE_PASSWORD                 : %DB_SEEDER_ORACLE_PASSWORD%
    echo ORACLE_PASSWORD_SYS             : %DB_SEEDER_ORACLE_PASSWORD_SYS%
    echo ORACLE_USER                     : %DB_SEEDER_ORACLE_USER%
)
if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["postgresql"] (
    echo POSTGRESQL_CONNECTION_PORT      : %DB_SEEDER_POSTGRESQL_CONNECTION_PORT%
    echo POSTGRESQL_CONNECTION_PREFIX    : %DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX%
    echo POSTGRESQL_DATABASE             : %DB_SEEDER_POSTGRESQL_DATABASE%
    echo POSTGRESQL_PASSWORD             : %DB_SEEDER_POSTGRESQL_PASSWORD%
    echo POSTGRESQL_PASSWORD_SYS         : %DB_SEEDER_POSTGRESQL_PASSWORD_SYS%
    echo POSTGRESQL_USER                 : %DB_SEEDER_POSTGRESQL_USER%
)
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

java --enable-preview -cp %DB_SEEDER_JAVA_CLASSPATH% ch.konnexions.db_seeder.DatabaseSeeder %DB_SEEDER_DATABASE_BRAND%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
