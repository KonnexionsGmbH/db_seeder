@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_mysql.bat: Setup a MySQL Database Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a MySQL Database Docker container.
echo --------------------------------------------------------------------------------
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem MySQL Database                                  https://hub.docker.com/_/mysql
rem ------------------------------------------------------------------------------

echo MySQL Database
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (MySQL Database %DB_SEEDER_VERSION%)
docker create --name db_seeder_db -e MYSQL_ROOT_PASSWORD=mysql -p %DB_SEEDER_CONNECTION_PORT%:%DB_SEEDER_CONTAINER_PORT%/tcp mysql:%DB_SEEDER_VERSION%

echo Docker start db_seeder_db (MySQL Database %DB_SEEDER_VERSION%) ...
docker start db_seeder_db

ping -n 20 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER MySQL Database was ready in %CONSUMED%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
