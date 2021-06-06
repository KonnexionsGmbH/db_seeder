@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_trino.bat: Setup a trino Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set ERRORLEVEL=

if ["%DB_SEEDER_VERSION_TRINO%"] EQU [""] (
    set DB_SEEDER_VERSION_TRINO=latest
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a trino Docker container.
echo --------------------------------------------------------------------------------
echo VERSION_TRINO                     : %DB_SEEDER_VERSION_TRINO%
echo "--------------------------------------------------------------------------------"
echo:| TIME
echo ================================================================================

echo --------------------------------------------------------------------------------
echo Stop and delete existing containers.
echo --------------------------------------------------------------------------------

echo Docker stop/rm db_seeder_trino .............................. before:
docker ps -a
docker ps | find "db_seeder_trino" && docker stop db_seeder_trino
docker ps -a | find "db_seeder_trino" && docker rm --force db_seeder_trino
echo ............................................................. after:
docker ps -a

echo Docker stop/rm db_seeder_db ................................. before:
docker ps -a
docker ps | find "db_seeder_db" && docker stop db_seeder_db
docker ps -a | find "db_seeder_db" && docker rm --force db_seeder_db
echo ............................................................. after:
docker ps -a

echo --------------------------------------------------------------------------------
echo Start trino - creating and starting the container
echo --------------------------------------------------------------------------------
lib\Gammadyne\timer.exe
echo Docker create trino (trino)

docker network create db_seeder_net 2>nul || echo Docker network db_seeder_net already existing
docker create --name    db_seeder_trino ^
              --network db_seeder_net ^
              -p        8080:8080/tcp ^
              -v        %cd%/resources/docker/trino:/etc/trino ^
              trinodb/trino:%DB_SEEDER_VERSION_TRINO%

echo Docker start trino (trino) ...
docker start db_seeder_trino

ping -n 30 127.0.0.1>nul

docker network ls
docker network inspect db_seeder_net

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo Docker trino was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
