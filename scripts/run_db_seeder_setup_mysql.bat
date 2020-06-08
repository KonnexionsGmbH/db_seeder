@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_mysql.bat: Setup a MySQL Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a MySQL database Docker container.
echo --------------------------------------------------------------------------------
echo VERSION_MYSQL             : %DB_SEEDER_VERSION_MYSQL%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem MySQL Database                                  https://hub.docker.com/_/mysql
rem ------------------------------------------------------------------------------

echo MySQL Database
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (MySQL %DB_SEEDER_VERSION_MYSQL%)
docker create -e MYSQL_ROOT_PASSWORD=mysql --name db_seeder_db -p 3306:3306/tcp mysql:%DB_SEEDER_VERSION_MYSQL%

echo Docker start db_seeder_db (MySQL %DB_SEEDER_VERSION_MYSQL%) ...
docker start db_seeder_db

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER MySQL Database was ready in %CONSUMED%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
