@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_derby.bat: Setup a Apache Derby Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a Apache Derby Docker container.
echo --------------------------------------------------------------------------------
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem Apache Derby
rem           https://hub.docker.com/repository/docker/konnexionsgmbh/apache_derby
rem ------------------------------------------------------------------------------

echo Apache Derby
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (Apache Derby %DB_SEEDER_VERSION%)
docker create --name db_seeder_db -p %DB_SEEDER_CONNECTION_PORT%:%DB_SEEDER_CONTAINER_PORT%/tcp konnexionsgmbh/apache_derby:%DB_SEEDER_VERSION%

echo Docker start db_seeder_db (Apache Derby %DB_SEEDER_VERSION%) ...
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
echo DOCKER Apache Derby was ready in %CONSUMED%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
