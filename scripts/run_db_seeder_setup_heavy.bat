@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_heavy.bat: Setup a HeavyDB Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

if ["%DB_SEEDER_CONNECTION_PORT%"] EQU [""] (
    set DB_SEEDER_CONNECTION_PORT=6274
)

if ["%DB_SEEDER_CONTAINER_PORT%"] EQU [""] (
    set DB_SEEDER_CONTAINER_PORT=6274
)

if ["%DB_SEEDER_VERSION%"] EQU [""] (
    docker ps    | find "db_seeder_db" && docker stop db_seeder_db
    docker ps -a | find "db_seeder_db" && docker rm --force db_seeder_db
    set DB_SEEDER_VERSION=latest
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - setup a HeavyDB Docker container.
echo --------------------------------------------------------------------------------
echo DBMS_TRINO                : %DB_SEEDER_DBMS_TRINO%
echo DB_SEEDER_CONNECTION_PORT : %DB_SEEDER_CONNECTION_PORT%
echo DB_SEEDER_CONTAINER_PORT  : %DB_SEEDER_CONTAINER_PORT%
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem HeavyDB                   https://hub.docker.com/_/omnisci-open-source-edition
rem ------------------------------------------------------------------------------

echo HeavyDB
echo --------------------------------------------------------------------------------
lib\Gammadyne\timer.exe
echo Docker create db_seeder_db (HeavyDB %DB_SEEDER_VERSION%)

rmdir /q /s tmp\heavy-docker-storage
mkdir       tmp\heavy-docker-storage

docker network create db_seeder_net 2>nul || echo Docker network db_seeder_net already existing
docker create --name    db_seeder_db ^
              --network db_seeder_net ^
              -p        %DB_SEEDER_CONNECTION_PORT%:%DB_SEEDER_CONTAINER_PORT% ^
              -v        %cd%\tmp\heavy-docker-storage:/omnisci-storage ^
              %DB_SEEDER_IMAGE%

              docker pull omnisci/core-os-cpu:v5.10.2

echo Docker start db_seeder_db (HeavyDB %DB_SEEDER_VERSION%) ...
docker start db_seeder_db

ping -n 60 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER HeavyDB was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
