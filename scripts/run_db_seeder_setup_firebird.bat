@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_firebird.bat: Setup a Firebird Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

if ["%DB_SEEDER_CONNECTION_PORT%"] EQU [""] (
    set DB_SEEDER_CONNECTION_PORT=3050
)

if ["%DB_SEEDER_CONTAINER_PORT%"] EQU [""] (
    set DB_SEEDER_CONTAINER_PORT=3050
)

if ["%DB_SEEDER_VERSION%"] EQU [""] (
    docker ps    | grep "db_seeder_db" && docker stop db_seeder_db
    docker ps -a | grep "db_seeder_db" && docker rm db_seeder_db
    set DB_SEEDER_VERSION=latest
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a Firebird Docker container.
echo --------------------------------------------------------------------------------
echo DBMS_PRESTO               : %DB_SEEDER_DBMS_PRESTO%
echo DB_SEEDER_CONNECTION_PORT : %DB_SEEDER_CONNECTION_PORT%
echo DB_SEEDER_CONTAINER_PORT  : %DB_SEEDER_CONTAINER_PORT%
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem Firebird                        https://hub.docker.com/r/jacobalberty/firebird
rem ------------------------------------------------------------------------------

echo Firebird
echo --------------------------------------------------------------------------------
lib\Gammadyne\timer.exe
echo Docker create db_seeder_db (Firebird %DB_SEEDER_VERSION%)
docker create --name db_seeder_db -e FIREBIRD_DATABASE=%DB_SEEDER_DATABASE% -e ISC_PASSWORD=firebird -p %DB_SEEDER_CONNECTION_PORT%:%DB_SEEDER_CONTAINER_PORT%/tcp jacobalberty/firebird:%DB_SEEDER_VERSION%

echo Docker start db_seeder_db (Firebird %DB_SEEDER_VERSION%) ...
docker start db_seeder_db

:check_health_status:
mkdir %cd%\tmp >nul 2>&1
docker inspect -f {{.State.Health.Status}} db_seeder_db > %cd%\tmp\docker_health_status.txt
set /P DOCKER_HEALTH_STATUS=<tmp\docker_health_status.txt
if NOT ["%DOCKER_HEALTH_STATUS%"] == ["healthy"] (
    docker ps --filter "name=db_seeder_db"
    ping -n 10 127.0.0.1 >nul
    goto :check_health_status
)

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER Firebird was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
