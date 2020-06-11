@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_postgresql.bat: Setup a PostgreSQL Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a PostgreSQL database Docker container.
echo --------------------------------------------------------------------------------
echo VERSION_POSTGRESQL        : %DB_SEEDER_VERSION_POSTGRESQL%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem PostgreSQL Database                          https://hub.docker.com/_/postgres
rem ------------------------------------------------------------------------------

echo PostgreSQL Database
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (PostgreSQL Database %DB_SEEDER_VERSION_POSTGRESQL%)
docker create -e POSTGRES_DB=kxn_db_sys -e POSTGRES_PASSWORD=postgresql -e POSTGRES_USER=kxn_user_sys --name db_seeder_db -p 5432:5432 postgres:%DB_SEEDER_VERSION_POSTGRESQL%

echo Docker start db_seeder_db (PostgreSQL Database %DB_SEEDER_VERSION_POSTGRESQL%) ...
docker start db_seeder_db

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER PostgreSQL Database was ready in %CONSUMED%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
