@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_agens.bat: Setup a AgensGraph Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

if ["%DB_SEEDER_CONNECTION_PORT%"] EQU [""] (
    set DB_SEEDER_CONNECTION_PORT=5432
)

if ["%DB_SEEDER_CONTAINER_PORT%"] EQU [""] (
    set DB_SEEDER_CONTAINER_PORT=5432
)

if ["%DB_SEEDER_VERSION%"] EQU [""] (
    docker ps    | find "db_seeder_db" && docker stop db_seeder_db
    docker ps -a | find "db_seeder_db" && docker rm --force db_seeder_db
    set DB_SEEDER_VERSION=latest
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - setup a AgensGraph Docker container.
echo --------------------------------------------------------------------------------
echo DBMS_TRINO                : %DB_SEEDER_DBMS_TRINO%
echo DB_SEEDER_CONNECTION_PORT : %DB_SEEDER_CONNECTION_PORT%
echo DB_SEEDER_CONTAINER_PORT  : %DB_SEEDER_CONTAINER_PORT%
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem AgensGraph                         https://hub.docker.com/r/bitnine/agensgraph
rem ------------------------------------------------------------------------------

echo AgensGraph
echo --------------------------------------------------------------------------------
lib\Gammadyne\timer.exe
echo Docker create db_seeder_db (AgensGraph %DB_SEEDER_VERSION%)

docker network create db_seeder_net 2>nul || echo Docker network db_seeder_net already existing
docker create --name    db_seeder_db ^
              --network db_seeder_net ^
              -e        POSTGRES_PASSWORD=agens ^
              -e        POSTGRES_USER=agens ^
              -p        %DB_SEEDER_CONNECTION_PORT%:%DB_SEEDER_CONTAINER_PORT% ^
              -t ^
              %DB_SEEDER_IMAGE%

echo Docker start db_seeder_db (AgensGraph %DB_SEEDER_VERSION%) ...
docker start db_seeder_db

ping -n 30 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER AgensGraph was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
