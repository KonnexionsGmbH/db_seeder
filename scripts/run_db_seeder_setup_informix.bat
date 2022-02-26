@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_informix.bat: Setup a IBM Informix Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

if ["%DB_SEEDER_CONNECTION_PORT%"] EQU [""] (
    set DB_SEEDER_CONNECTION_PORT=9088
)

if ["%DB_SEEDER_CONTAINER_PORT%"] EQU [""] (
    set DB_SEEDER_CONTAINER_PORT=9088
)

if ["%DB_SEEDER_VERSION%"] EQU [""] (
    docker ps    | find "db_seeder_db" && docker stop db_seeder_db
    docker ps -a | find "db_seeder_db" && docker rm --force db_seeder_db
    set DB_SEEDER_VERSION=latest
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - setup a IBM Informix Docker container.
echo --------------------------------------------------------------------------------
echo DBMS_TRINO                : %DB_SEEDER_DBMS_TRINO%
echo DB_SEEDER_CONNECTION_PORT : %DB_SEEDER_CONNECTION_PORT%
echo DB_SEEDER_CONTAINER_PORT  : %DB_SEEDER_CONTAINER_PORT%
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem IBM Informix       https://hub.docker.com/r/ibmcom/informix-developer-database
rem ------------------------------------------------------------------------------

echo IBM Informix
echo --------------------------------------------------------------------------------
lib\Gammadyne\timer.exe
echo Docker create db_seeder_db (IBM Informix %DB_SEEDER_VERSION%)

set DB_SEEDER_IMAGE=ibmcom/informix-developer-database:%DB_SEEDER_VERSION%

docker network create db_seeder_net 2>nul || echo Docker network db_seeder_net already existing
docker create -e           DB_INIT=1 ^
              -e           LICENSE=accept ^
              --name       db_seeder_db ^
              --network    db_seeder_net ^
              -p           %DB_SEEDER_CONNECTION_PORT%:%DB_SEEDER_CONTAINER_PORT% ^
              --privileged ^
              %DB_SEEDER_IMAGE%

echo Docker start db_seeder_db (IBM Informix %DB_SEEDER_VERSION%)
docker start db_seeder_db

:check_health_status:
docker inspect -f {{.State.Health.Status}} db_seeder_db > %cd%\tmp\docker_health_status.txt
set /P DOCKER_HEALTH_STATUS=<tmp\docker_health_status.txt
if NOT ["%DOCKER_HEALTH_STATUS%"] == ["healthy"] (
    docker ps --filter "name=db_seeder_db"
    ping -n 10 127.0.0.1 >nul
    goto :check_health_status
)

docker exec -i db_seeder_db bash < scripts\run_db_seeder_setup_informix.input

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER IBM Informix was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
