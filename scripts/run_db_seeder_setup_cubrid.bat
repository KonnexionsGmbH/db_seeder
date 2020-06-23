@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_cubrid.bat: Setup a CUBRID Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a CUBRID Docker container.
echo --------------------------------------------------------------------------------
echo VERSION_CUBRID            : %DB_SEEDER_VERSION_CUBRID%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem CUBRID                                  https://hub.docker.com/r/cubrid/cubrid
rem ------------------------------------------------------------------------------

echo CUBRID
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (CUBRID %DB_SEEDER_VERSION_CUBRID%)
docker create --name db_seeder_db -p 33000:33000/tcp -e CUBRID_DB=%DB_SEEDER_CUBRID_DATABASE% cubrid/cubrid:%DB_SEEDER_VERSION_CUBRID%

echo Docker start db_seeder_db (CUBRID %DB_SEEDER_VERSION_CUBRID%) ...
docker start db_seeder_db

rem ping -n 10 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER CUBRID was ready in %CONSUMED%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
