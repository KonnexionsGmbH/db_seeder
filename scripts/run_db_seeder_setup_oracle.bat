@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_oracle.bat: Setup a Oracle Database Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a Oracle Database Docker container.
echo --------------------------------------------------------------------------------
echo VERSION_ORACLE            : %DB_SEEDER_ORACLE_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem Oracle Database.
rem ------------------------------------------------------------------------------

echo Oracle Database
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (Oracle Database %DB_SEEDER_ORACLE_VERSION%)
docker create --name db_seeder_db -e ORACLE_PWD=oracle -p 1521:1521/tcp --shm-size 1G konnexionsgmbh/%DB_SEEDER_ORACLE_VERSION%

echo Docker start db_seeder_db (Oracle Database %DB_SEEDER_ORACLE_VERSION%) ...
docker start db_seeder_db

:check_health_status:
mkdir tmp >nul 2>&1
docker inspect -f {{.State.Health.Status}} db_seeder_db > tmp\docker_health_status.txt
set /P DOCKER_HEALTH_STATUS=<tmp\docker_health_status.txt
if NOT ["%DOCKER_HEALTH_STATUS%"] == ["healthy"] (
    docker ps --filter "name=db_seeder_db"
    ping -n 60 127.0.0.1 >nul
    goto :check_health_status
)

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER Oracle Database was ready in %CONSUMED%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
