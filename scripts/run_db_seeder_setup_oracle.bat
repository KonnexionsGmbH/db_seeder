@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_oracle.bat: Setup a Oracle database Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a database Docker container.
echo --------------------------------------------------------------------------------
echo VERSION_ORACLE            : %DB_SEEDER_VERSION_ORACLE%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem Oracle Database.
rem ------------------------------------------------------------------------------

echo Oracle Database
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (Oracle %DB_SEEDER_VERSION_ORACLE%)
docker create -e ORACLE_PWD=oracle --name db_seeder_db -p 1521:1521/tcp --shm-size 1G konnexionsgmbh/%DB_SEEDER_VERSION_ORACLE%

echo Docker start db_seeder_db (Oracle %DB_SEEDER_VERSION_ORACLE%) ...
docker start db_seeder_db

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER Oracle Database was ready in %CONSUMED%

:check_health_status:
mkdir tmp >nul 2>&1
docker inspect -f {{.State.Health.Status}} db_seeder_db > tmp\docker_health_status.txt
set /P DOCKER_HEALTH_STATUS=<tmp\docker_health_status.txt
if NOT ["%DOCKER_HEALTH_STATUS%"] == ["healthy"] (
    docker ps --filter "name=db_seeder_db"
    ping -n 60 127.0.0.1 >nul
    goto :check_health_status
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
