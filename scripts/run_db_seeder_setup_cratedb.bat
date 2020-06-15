@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_cratedb.bat: Setup a CrateDB Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a CrateDB Docker container.
echo --------------------------------------------------------------------------------
echo VERSION_CRATEDB           : %DB_SEEDER_VERSION_CRATEDB%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem CrateDB                                         https://hub.docker.com/_/crate
rem ------------------------------------------------------------------------------

echo CrateDB
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (CrateDB %DB_SEEDER_VERSION_CRATEDB%)
docker create --name db_seeder_db -p 5432:5432/tcp --env CRATE_HEAP_SIZE=2g crate:%DB_SEEDER_VERSION_CRATEDB% crate -Cnetwork.host=_site_ -Cdiscovery.type=single-node

echo Docker start db_seeder_db (CrateDB %DB_SEEDER_VERSION_CRATEDB%) ...
docker start db_seeder_db

ping -n 20 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER CrateDB was ready in %CONSUMED%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
