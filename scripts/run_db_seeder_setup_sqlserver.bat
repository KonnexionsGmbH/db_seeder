@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_sqlserver.bat: Setup a Microsoft SQL Server Docker
rem                                    container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

if ["%DB_SEEDER_CONNECTION_PORT%"] EQU [""] (
    set DB_SEEDER_CONNECTION_PORT=1433
)

if ["%DB_SEEDER_CONTAINER_PORT%"] EQU [""] (
    set DB_SEEDER_CONTAINER_PORT=1433
)

if ["%DB_SEEDER_VERSION%"] EQU [""] (
    docker ps    | grep "db_seeder_db" && docker stop db_seeder_db
    docker ps -a | grep "db_seeder_db" && docker rm db_seeder_db
    set DB_SEEDER_VERSION=latest
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a Microsoft SQL Server Docker container.
echo --------------------------------------------------------------------------------
echo DBMS_PRESTO               : %DB_SEEDER_DBMS_PRESTO%
echo DB_SEEDER_CONNECTION_PORT : %DB_SEEDER_CONNECTION_PORT%
echo DB_SEEDER_CONTAINER_PORT  : %DB_SEEDER_CONTAINER_PORT%
echo VERSION                   : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem Microsoft SQL Server           https://hub.docker.com/_/microsoft-mssql-server
rem ------------------------------------------------------------------------------

echo Microsoft SQL Server
echo --------------------------------------------------------------------------------
lib\Gammadyne\timer.exe
echo Docker create db_seeder_db (Microsoft SQL Server %DB_SEEDER_VERSION%)

if ["%DB_SEEDER_DBMS_PRESTO%"] EQU ["yes"] (
    docker create --name db_seeder_db ^
                  -e        "ACCEPT_EULA=Y" ^
                  -e        "SA_PASSWORD=sqlserver_2019" ^
                  --network db_seeder_net ^
                  -p        %DB_SEEDER_CONNECTION_PORT%:%DB_SEEDER_CONTAINER_PORT% ^
                  mcr.microsoft.com/mssql/server:%DB_SEEDER_VERSION%
) else (
    docker create --name db_seeder_db ^
                  -e     "ACCEPT_EULA=Y" ^
                  -e     "SA_PASSWORD=sqlserver_2019" ^
                  -p     %DB_SEEDER_CONNECTION_PORT%:%DB_SEEDER_CONTAINER_PORT% ^
                  mcr.microsoft.com/mssql/server:%DB_SEEDER_VERSION%
)

echo Docker start db_seeder_db (Microsoft SQL Server %DB_SEEDER_VERSION%) ...
docker start db_seeder_db

ping -n 30 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER Microsoft SQL Server was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
