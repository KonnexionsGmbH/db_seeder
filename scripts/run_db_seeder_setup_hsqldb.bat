@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_hsqldb.bat: Setup a HyperSQL Database Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a HyperSQL Database Docker container.
echo --------------------------------------------------------------------------------
echo VERSION_HSQLDB            : %DB_SEEDER_VERSION_HSQLDB%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem HyperSQL Database
rem        https://hub.docker.com/repository/docker/konnexionsgmbh/hsqldb_database
rem ------------------------------------------------------------------------------

echo HyperSQL Database
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (HyperSQL Database %DB_SEEDER_VERSION_HSQLDB%)
docker create --name db_seeder_db -p 9001:9001/tcp konnexionsgmbh/hypersql_database:%DB_SEEDER_VERSION_HSQLDB%

echo Docker start db_seeder_db (HyperSQL Database %DB_SEEDER_VERSION_HSQLDB%) ...
docker start db_seeder_db

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER HyperSQL Database was ready in %CONSUMED%

:check_health_status:
mkdir tmp >nul 2>&1
docker inspect -f {{.State.Health.Status}} db_seeder_db > tmp\docker_health_status.txt
set /P DOCKER_HEALTH_STATUS=<tmp\docker_health_status.txt
if NOT ["%DOCKER_HEALTH_STATUS%"] == ["healthy"] (
    docker ps --filter "name=db_seeder_db"
    ping -n 10 127.0.0.1 >nul
    goto :check_health_status
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
