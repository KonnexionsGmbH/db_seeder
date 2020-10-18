@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_voltdb.bat: Setup a VoltDB Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

if ["%DB_SEEDER_CONNECTION_PORT%"] EQU [""] (
    set DB_SEEDER_CONNECTION_PORT=21212
)

if ["%DB_SEEDER_CONTAINER_PORT%"] EQU [""] (
    set DB_SEEDER_CONTAINER_PORT=21212
)

if ["%DB_SEEDER_VERSION%"] EQU [""] (
    docker ps    | grep "db_seeder_db" && docker stop db_seeder_db
    docker ps -a | grep "db_seeder_db" && docker rm db_seeder_db
    set DB_SEEDER_VERSION=latest
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a VoltDB Docker container.
echo --------------------------------------------------------------------------------
echo DBMS_PRESTO               : %DB_SEEDER_DBMS_PRESTO%
echo DB_SEEDER_CONNECTION_PORT : %DB_SEEDER_CONNECTION_PORT%
echo DB_SEEDER_CONTAINER_PORT  : %DB_SEEDER_CONTAINER_PORT%
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem VoltDB                        https://hub.docker.com/r/voltdb/voltdb-community
rem ------------------------------------------------------------------------------

echo VoltDB
echo --------------------------------------------------------------------------------
lib\Gammadyne\timer.exe
echo Docker create db_seeder_db (VoltDB %DB_SEEDER_VERSION%)

docker network create db_seeder_net 2>null || echo Docker network db_seeder_net already existing
docker run -d ^
           -e        HOST_COUNT=1 ^
           --name    db_seeder_db ^
           --network db_seeder_net ^
           -p        21212:21212 ^
           -v        %cd%/resources/voltdb/deployment.xml:/tmp/deployment.xml ^
           voltdb/voltdb-community:%DB_SEEDER_VERSION%

echo Docker start db_seeder_db (VoltDB %DB_SEEDER_VERSION%) ...

ping -n 20 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER VoltDB was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
