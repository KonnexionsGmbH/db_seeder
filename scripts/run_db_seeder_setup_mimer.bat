@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_mimer.bat: Setup a Mimer SQL Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a Mimer SQL Docker container.
echo --------------------------------------------------------------------------------
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem Mimer SQL                     https://hub.docker.com/r/mimersql/mimersql_v11.0
rem ------------------------------------------------------------------------------

echo Mimer SQL
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (Mimer SQL %DB_SEEDER_VERSION%)
docker create --name db_seeder_db -e SYSADM_PASSWORD=mimer -p 11360:1360/tcp mimersql/mimersql_v11.0:%DB_SEEDER_VERSION%
 
echo Docker start db_seeder_db (Mimer SQL %DB_SEEDER_VERSION%) ...
docker start db_seeder_db

ping -n 20 127.0.0.1>nul

docker exec -i db_seeder_db bash < scripts\run_db_seeder_setup_mimer.input

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER Mimer SQL was ready in %CONSUMED%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
