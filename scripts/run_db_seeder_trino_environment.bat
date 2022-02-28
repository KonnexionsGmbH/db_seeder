@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_trino_environment.bat: Generating trino catalog properties files.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set ERRORLEVEL=

set DB_SEEDER_DBMS_DEFAULT=complete

if ["%1"] EQU [""] (
    echo ============================================================
    echo complete           - All implemented trino enabled DBMSs
    echo mysql              - MySQL Database
    echo oracle             - Oracle Database
    echo postgresql         - PostgreSQL
    echo sqlserver          - SQL Server
    echo -----------------------------------------------------------
    set /P DB_SEEDER_DBMS="Enter the desired database management system [default: %DB_SEEDER_DBMS_DEFAULT%] "

    if ["!DB_SEEDER_DBMS!"] EQU [""] (
        set DB_SEEDER_DBMS=%DB_SEEDER_DBMS_DEFAULT%
    )
) else (
    set DB_SEEDER_DBMS=%1
)

if ["!DB_SEEDER_DBMS!"] EQU ["complete"] (
    set DB_SEEDER_DBMS="mysql oracle postgresql sqlserver"
)

set DB_SEEDER_JAVA_CLASSPATH=".;lib/*;JAVA_HOME/lib"

set DB_SEEDER_DIRECTORY_CATALOG_PROPERTY=resources\docker\trino\catalog

set DB_SEEDER_MYSQL_CONNECTION_HOST=db_seeder_db
set DB_SEEDER_MYSQL_CONNECTION_PORT=3306
set DB_SEEDER_MYSQL_CONNECTION_PREFIX="jdbc:mysql://"
set DB_SEEDER_MYSQL_CONNECTION_SUFFIX="?serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false&rewriteBatchedStatements=true"
set DB_SEEDER_MYSQL_PASSWORD=mysql
set DB_SEEDER_MYSQL_USER=kxn_user

set DB_SEEDER_ORACLE_CONNECTION_HOST=db_seeder_db
set DB_SEEDER_ORACLE_CONNECTION_PORT=1521
set DB_SEEDER_ORACLE_CONNECTION_PREFIX="jdbc:oracle:thin:@//"
set DB_SEEDER_ORACLE_CONNECTION_SERVICE=orclpdb1
set DB_SEEDER_ORACLE_PASSWORD=oracle
set DB_SEEDER_ORACLE_USER=kxn_user

set DB_SEEDER_POSTGRESQL_CONNECTION_HOST=db_seeder_db
set DB_SEEDER_POSTGRESQL_CONNECTION_PORT=5432
set DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX="jdbc:postgresql://"
set DB_SEEDER_POSTGRESQL_DATABASE=kxn_db
set DB_SEEDER_POSTGRESQL_PASSWORD=postgresql
set DB_SEEDER_POSTGRESQL_USER=kxn_user

set DB_SEEDER_SQLSERVER_CONNECTION_HOST=db_seeder_db
set DB_SEEDER_SQLSERVER_CONNECTION_PORT=1433
set DB_SEEDER_SQLSERVER_CONNECTION_PREFIX="jdbc:sqlserver://"
set DB_SEEDER_SQLSERVER_DATABASE=kxn_db
set DB_SEEDER_SQLSERVER_PASSWORD=sqlserver_2019
set DB_SEEDER_SQLSERVER_USER=kxn_user

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - Generating trino catalog properties files.
echo --------------------------------------------------------------------------------
echo DBMS_DEFAULT                  : %DB_SEEDER_DBMS_DEFAULT%
echo DB_SEEDER_DBMS                : %DB_SEEDER_DBMS%
echo DIRECTORY_CATALOG_PROPERTY    : %DB_SEEDER_DIRECTORY_CATALOG_PROPERTY%
echo JAVA_CLASSPATH                : %DB_SEEDER_JAVA_CLASSPATH%
echo --------------------------------------------------------------------------------
echo CONNECTION_HOST_TRINO         : %DB_SEEDER_CONNECTION_HOST_TRINO%
echo CONNECTION_PORT_TRINO         : %DB_SEEDER_CONNECTION_PORT_TRINO%
echo --------------------------------------------------------------------------------
echo MYSQL_CONNECTION_HOST         : %DB_SEEDER_MYSQL_CONNECTION_HOST%
echo MYSQL_CONNECTION_PORT         : %DB_SEEDER_MYSQL_CONNECTION_PORT%
echo MYSQL_CONNECTION_PREFIX       : %DB_SEEDER_MYSQL_CONNECTION_PREFIX%
echo MYSQL_CONNECTION_SUFFIX       : %DB_SEEDER_MYSQL_CONNECTION_SUFFIX%
echo MYSQL_PASSWORD                : %DB_SEEDER_MYSQL_PASSWORD%
echo MYSQL_USER                    : %DB_SEEDER_MYSQL_USER%
echo --------------------------------------------------------------------------------
echo ORACLE_CONNECTION_HOST        : %DB_SEEDER_ORACLE_CONNECTION_HOST%
echo ORACLE_CONNECTION_PORT        : %DB_SEEDER_ORACLE_CONNECTION_PORT%
echo ORACLE_CONNECTION_PREFIX      : %DB_SEEDER_ORACLE_CONNECTION_PREFIX%
echo ORACLE_CONNECTION_SERVICE     : %DB_SEEDER_ORACLE_CONNECTION_SERVICE%
echo ORACLE_PASSWORD               : %DB_SEEDER_ORACLE_PASSWORD%
echo ORACLE_USER                   : %DB_SEEDER_ORACLE_USER%
echo --------------------------------------------------------------------------------
echo POSTGRESQL_CONNECTION_HOST    : %DB_SEEDER_POSTGRESQL_CONNECTION_HOST%
echo POSTGRESQL_CONNECTION_PORT    : %DB_SEEDER_POSTGRESQL_CONNECTION_PORT%
echo POSTGRESQL_CONNECTION_PREFIX  : %DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX%
echo POSTGRESQL_DATABASE           : %DB_SEEDER_POSTGRESQL_DATABASE%
echo POSTGRESQL_PASSWORD           : %DB_SEEDER_POSTGRESQL_PASSWORD%
echo POSTGRESQL_USER               : %DB_SEEDER_POSTGRESQL_USER%
echo --------------------------------------------------------------------------------
echo SQLSERVER_CONNECTION_HOST     : %DB_SEEDER_SQLSERVER_CONNECTION_HOST%
echo SQLSERVER_CONNECTION_PORT     : %DB_SEEDER_SQLSERVER_CONNECTION_PORT%
echo SQLSERVER_CONNECTION_PREFIX   : %DB_SEEDER_SQLSERVER_CONNECTION_PREFIX%
echo SQLSERVER_DATABASE            : %DB_SEEDER_SQLSERVER_DATABASE%
echo SQLSERVER_PASSWORD            : %DB_SEEDER_SQLSERVER_PASSWORD%
echo SQLSERVER_USER                : %DB_SEEDER_SQLSERVER_USER%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================
echo Generate and compile catalog properties files.
echo --------------------------------------------------------------------------------

del -f %DB_SEEDER_DIRECTORY_CATALOG_PROPERTY%\db_seeder_*.properties 2>nul

java -cp %DB_SEEDER_JAVA_CLASSPATH% ch.konnexions.db_seeder.TrinoEnvironment %DB_SEEDER_DBMS%
if ERRORLEVEL 1 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
