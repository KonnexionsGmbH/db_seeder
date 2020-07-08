@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_firebird.bat: Setup a Firebird Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a Firebird Docker container.
echo --------------------------------------------------------------------------------
echo VERSION_FIREBIRD          : %DB_SEEDER_VERSION_FIREBIRD%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem Firebird                        https://hub.docker.com/r/jacobalberty/firebird
rem ------------------------------------------------------------------------------

echo Firebird
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (Firebird %DB_SEEDER_VERSION_FIREBIRD%)
docker create --name db_seeder_db -e FIREBIRD_DATABASE=%DB_SEEDER_FIREBIRD_DATABASE% -e ISC_PASSWORD=firebird -p 3050:3050/tcp jacobalberty/firebird:%DB_SEEDER_VERSION_FIREBIRD%

echo Docker start db_seeder_db (Firebird %DB_SEEDER_VERSION_FIREBIRD%) ...
docker start db_seeder_db

:check_health_status:
mkdir tmp >nul 2>&1
docker inspect -f {{.State.Health.Status}} db_seeder_db > tmp\docker_health_status.txt
set /P DOCKER_HEALTH_STATUS=<tmp\docker_health_status.txt
if NOT ["%DOCKER_HEALTH_STATUS%"] == ["healthy"] (
    docker ps --filter "name=db_seeder_db"
    ping -n 10 127.0.0.1 >nul
    goto :check_health_status
)

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER Firebird was ready in %CONSUMED%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
