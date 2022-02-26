@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_monetdb.bat: Setup a monetdb Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

if ["%DB_SEEDER_CONNECTION_PORT%"] EQU [""] (
    set DB_SEEDER_CONNECTION_PORT=50000
)

if ["%DB_SEEDER_CONTAINER_PORT%"] EQU [""] (
    set DB_SEEDER_CONTAINER_PORT=50000
)

if ["%DB_SEEDER_VERSION%"] EQU [""] (
    docker ps    | find "db_seeder_db" && docker stop db_seeder_db
    docker ps -a | find "db_seeder_db" && docker rm --force db_seeder_db
    set DB_SEEDER_VERSION=latest
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - setup a monetdb Docker container.
echo --------------------------------------------------------------------------------
echo DBMS_TRINO                : %DB_SEEDER_DBMS_TRINO%
echo DB_SEEDER_CONNECTION_PORT : %DB_SEEDER_CONNECTION_PORT%
echo DB_SEEDER_CONTAINER_PORT  : %DB_SEEDER_CONTAINER_PORT%
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem monetdb                               https://hub.docker.com/r/monetdb/monetdb
rem ------------------------------------------------------------------------------

echo monetdb
echo --------------------------------------------------------------------------------
lib\Gammadyne\timer.exe
echo Docker create db_seeder_db (monetdb %DB_SEEDER_VERSION%)

set DB_SEEDER_IMAGE=monetdb/monetdb:%DB_SEEDER_VERSION%

docker network create db_seeder_net 2>nul || echo Docker network db_seeder_net already existing
docker create --name    db_seeder_db ^
              --network db_seeder_net ^
              -p        %DB_SEEDER_CONNECTION_PORT%:%DB_SEEDER_CONTAINER_PORT%/tcp ^
              %DB_SEEDER_IMAGE%

echo Docker start db_seeder_db (monetdb %DB_SEEDER_VERSION%) ...
docker start db_seeder_db

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER monetdb was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
