@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_percona.bat: Setup a Percona Server Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

if ["%DB_SEEDER_CONNECTION_PORT%"] EQU [""] (
    set DB_SEEDER_CONNECTION_PORT=3306
)

if ["%DB_SEEDER_CONTAINER_PORT%"] EQU [""] (
    set DB_SEEDER_CONTAINER_PORT=3306
)

if ["%DB_SEEDER_VERSION%"] EQU [""] (
    docker ps    | grep "db_seeder_db" && docker stop db_seeder_db
    docker ps -a | grep "db_seeder_db" && docker rm db_seeder_db
    set DB_SEEDER_VERSION=latest
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a Percona Server Docker container.
echo --------------------------------------------------------------------------------
echo DBMS_PRESTO               : %DB_SEEDER_DBMS_PRESTO%
echo DB_SEEDER_CONNECTION_PORT : %DB_SEEDER_CONNECTION_PORT%
echo DB_SEEDER_CONTAINER_PORT  : %DB_SEEDER_CONTAINER_PORT%
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem Percona Server                         https://hub.docker.com/_/percona-server
rem ------------------------------------------------------------------------------

echo Percona Server
echo --------------------------------------------------------------------------------
lib\Gammadyne\timer.exe
echo Docker create db_seeder_db (Percona Server %DB_SEEDER_VERSION%)

docker network create db_seeder_net 2>null || echo Docker network db_seeder_net already existing
docker create -e        MYSQL_ROOT_PASSWORD=percona ^
              --name    db_seeder_db ^
              --network db_seeder_net ^
              -p        %DB_SEEDER_CONNECTION_PORT%:%DB_SEEDER_CONTAINER_PORT%/tcp ^
              store/percona/percona-server:%DB_SEEDER_VERSION%

echo Docker start db_seeder_db (Percona Server %DB_SEEDER_VERSION%) ...
docker start db_seeder_db

ping -n 60 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER Percona Server was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
