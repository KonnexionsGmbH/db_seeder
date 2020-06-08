@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_mssqlserver.bat: Setup a Microsoft SQL Server Docker
rem                                      container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a Microsoft SQL Server database Docker container.
echo --------------------------------------------------------------------------------
echo VERSION_MSSQLSERVER       : %DB_SEEDER_VERSION_MSSQLSERVER%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem Microsoft SQL Server Database  https://hub.docker.com/_/microsoft-mssql-server
rem ------------------------------------------------------------------------------

echo Microsoft SQL Server Database
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (Microsoft SQL Server %DB_SEEDER_VERSION_MSSQLSERVER%)
docker create -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=mssqlserver_2019" --name db_seeder_db -p 1433:1433 mcr.microsoft.com/mssql/server:%DB_SEEDER_VERSION_MSSQLSERVER%

echo Docker start db_seeder_db (Microsoft SQL Server %DB_SEEDER_VERSION_MSSQLSERVER%) ...
docker start db_seeder_db

ping -n 20 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER Microsoft SQL Server Database was ready in %CONSUMED%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
