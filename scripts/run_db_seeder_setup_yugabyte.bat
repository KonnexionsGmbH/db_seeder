@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_yugabyte.bat: Setup a YugabyteDB Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

if ["%DB_SEEDER_CONNECTION_PORT%"] EQU [""] (
    set DB_SEEDER_CONNECTION_PORT=5433
)

if ["%DB_SEEDER_CONTAINER_PORT%"] EQU [""] (
    set DB_SEEDER_CONTAINER_PORT=5433
)

if ["%DB_SEEDER_VERSION%"] EQU [""] (
    docker ps    | grep "db_seeder_db" && docker stop db_seeder_db
    docker ps -a | grep "db_seeder_db" && docker rm db_seeder_db
    set DB_SEEDER_VERSION=latest
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a YugabyteDB Docker container.
echo --------------------------------------------------------------------------------
echo DBMS_PRESTO               : %DB_SEEDER_DBMS_PRESTO%
echo DB_SEEDER_CONNECTION_PORT : %DB_SEEDER_CONNECTION_PORT%
echo DB_SEEDER_CONTAINER_PORT  : %DB_SEEDER_CONTAINER_PORT%
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem YugabyteDB                          https://hub.docker.com/_/postgres
rem ------------------------------------------------------------------------------

echo YugabyteDB
echo --------------------------------------------------------------------------------
lib\Gammadyne\timer.exe
echo Docker create db_seeder_db (YugabyteDB %DB_SEEDER_VERSION%)

rd /q /s %cd%\tmp\yb_data 2>nul
md %cd%\tmp\yb_data

docker network create db_seeder_net 2>null || echo Docker network db_seeder_net already existing
docker run -d ^
           --name    db_seeder_db ^
           --network db_seeder_net ^
           -p        5433:5433 ^
           -p        7000:7000 ^
           -p        9001:9000 ^
           -p        9042:9042 ^
           -v        %cd%/tmp/yb_data:/home/yugabyte/var ^
           yugabytedb/yugabyte:%DB_SEEDER_VERSION% bin/yugabyted start --daemon=false

echo Docker start db_seeder_db (YugabyteDB %DB_SEEDER_VERSION%) ...

ping -n 30 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER YugabyteDB was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
