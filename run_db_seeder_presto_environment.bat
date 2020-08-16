@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_presto_environment.bat: Creating a Presto environment.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set ERRORLEVEL=

set DB_SEEDER_DBMS_DEFAULT=complete

if ["%1"] EQU [""] (
    echo ============================================================
    echo complete           - All implemented Presto enabled DBMSs
    echo sqlserver          - Microsoft SQL Server
    echo mysql              - MySQL Database
    echo oracle             - Oracle Database
    echo postgresql         - PostgreSQL Database
    echo -----------------------------------------------------------
    set /P DB_SEEDER_DBMS="Enter the desired database management system [default: %DB_SEEDER_DBMS_DEFAULT%] "

    if ["!DB_SEEDER_DBMS!"] EQU [""] (
        set DB_SEEDER_DBMS=%DB_SEEDER_DBMS_DEFAULT%
    )
) else (
    set DB_SEEDER_DBMS=%1
)

if ["!DB_SEEDER_DBMS!"] EQU ["complete"] (
    set DB_SEEDER_DBMS=mysql oracle postgresql sqlserver
)

set DB_SEEDER_JAVA_CLASSPATH=%CLASSPATH%;lib/*

set DB_SEEDER_DIRECTORY_CATALOG_PROPERTY=resources\docker\presto\catalog

set DB_SEEDER_VERSION_PRESTO=340

set DB_SEEDER_MYSQL_CONNECTION_HOST=db_seeder_db
set DB_SEEDER_MYSQL_CONNECTION_PORT=3306
set DB_SEEDER_MYSQL_CONNECTION_PREFIX="jdbc:mysql://"
set DB_SEEDER_MYSQL_CONNECTION_SUFFIX="?serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false?rewriteBatchedStatements=true"
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
echo DB Seeder - Creating a Presto environment.
echo --------------------------------------------------------------------------------
echo DBMS_DEFAULT                  : %DB_SEEDER_DBMS_DEFAULT%
echo DIRECTORY_CATALOG_PROPERTY    : %DB_SEEDER_DIRECTORY_CATALOG_PROPERTY%
echo JAVA_CLASSPATH                : %DB_SEEDER_JAVA_CLASSPATH%
echo VERSION_PRESTO                : %DB_SEEDER_VERSION_PRESTO%
echo --------------------------------------------------------------------------------
echo CONNECTION_HOST_PRESTO        : %DB_SEEDER_CONNECTION_HOST_PRESTO%
echo CONNECTION_PORT_PRESTO        : %DB_SEEDER_CONNECTION_PORT_PRESTO%
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
echo Compile and generate catalog property files.
echo --------------------------------------------------------------------------------

del -f %DB_SEEDER_DIRECTORY_CATALOG_PROPERTY%\db_seeder_*.properties

java --enable-preview -cp %DB_SEEDER_JAVA_CLASSPATH% ch.konnexions.db_seeder.PrestoEnvironment %DB_SEEDER_DBMS%
if %ERRORLEVEL% NEQ 0 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

echo --------------------------------------------------------------------------------
echo Stop and delete existing containers.
echo --------------------------------------------------------------------------------

echo Docker stop/rm db_seeder_presto ............................ before:
docker ps    | grep "db_seeder_presto" && docker stop db_seeder_presto
docker ps -a | grep "db_seeder_presto" && docker rm db_seeder_presto
echo ............................................................. after:
docker ps -a

echo Docker stop/rm db_seeder_db ................................ before:
docker ps    | grep "db_seeder_db" && docker stop db_seeder_db
docker ps -a | grep "db_seeder_db" && docker rm db_seeder_db
echo ............................................................. after:
docker ps -a

docker network prune --force

echo --------------------------------------------------------------------------------
echo Create Docker image.
echo --------------------------------------------------------------------------------

if exist tmp rmdir /Q/S tmp
mkdir tmp

xcopy /E /I resources\docker tmp
rename tmp\dockerfile_presto dockerfile

docker build -t konnexionsgmbh/db_seeder_presto tmp

docker images -q -f "dangling=true" -f "label=autodelete=true"

for /F %%I in ('docker images -q -f "dangling=true" -f "label=autodelete=true"') do (docker rmi -f %%I)

echo --------------------------------------------------------------------------------
echo Start Presto Distributed Query Engine - creating and starting the container
echo --------------------------------------------------------------------------------
docker network create db_seeder_net
docker network ls
lib\Gammadyne\timer.exe
echo Docker create presto (Presto Distributed Query Engine)
docker create --name    db_seeder_presto ^
              --network db_seeder_net ^
              -p        8080:8080/tcp ^
              konnexionsgmbh/db_seeder_presto
echo Docker start presto (Presto Distributed Query Engine) ...
docker start db_seeder_presto

ping -n 30 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo Docker Presto Distributed Query Engine was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
