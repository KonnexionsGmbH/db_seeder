@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_hsqldb.bat: Setup a HSQLDB Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

if ["%DB_SEEDER_CONNECTION_PORT%"] EQU [""] (
    set DB_SEEDER_CONNECTION_PORT=9001
)

if ["%DB_SEEDER_CONTAINER_PORT%"] EQU [""] (
    set DB_SEEDER_CONTAINER_PORT=9001
)

if ["%DB_SEEDER_VERSION%"] EQU [""] (
    docker ps -a
    docker ps    | find "db_seeder_db" && docker stop db_seeder_db
    docker ps -a | find "db_seeder_db" && docker rm --force db_seeder_db
    docker ps -a
    set DB_SEEDER_VERSION=latest
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - setup a HSQLDB Docker container.
echo --------------------------------------------------------------------------------
echo DBMS_TRINO                : %DB_SEEDER_DBMS_TRINO%
echo DB_SEEDER_CONNECTION_PORT : %DB_SEEDER_CONNECTION_PORT%
echo DB_SEEDER_CONTAINER_PORT  : %DB_SEEDER_CONTAINER_PORT%
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem HSQLDB
rem        https://hub.docker.com/repository/docker/konnexionsgmbh/hsqldb_database
rem ------------------------------------------------------------------------------

echo HSQLDB
echo --------------------------------------------------------------------------------
lib\Gammadyne\timer.exe
echo Docker create db_seeder_db (HSQLDB %DB_SEEDER_VERSION%)

docker network create db_seeder_net 2>nul || echo Docker network db_seeder_net already existing
docker create --name    db_seeder_db ^
              --network db_seeder_net ^
              -p        %DB_SEEDER_CONNECTION_PORT%:%DB_SEEDER_CONTAINER_PORT%/tcp ^
              %DB_SEEDER_IMAGE%

echo Docker start db_seeder_db (HSQLDB %DB_SEEDER_VERSION%) ...
docker start db_seeder_db

:check_health_status:
docker inspect -f {{.State.Health.Status}} db_seeder_db > %cd%\tmp\docker_health_status.txt
set /P DOCKER_HEALTH_STATUS=<tmp\docker_health_status.txt
if NOT ["%DOCKER_HEALTH_STATUS%"] == ["healthy"] (
    docker ps --filter "name=db_seeder_db"
    ping -n 10 127.0.0.1 >nul
    goto :check_health_status
)

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER HSQLDB was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
