@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_presto.bat: Setup a Presto Distributed Query Engine Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set ERRORLEVEL=

if ["%DB_SEEDER_VERSION_PRESTO%"] EQU [""] (
    set DB_SEEDER_VERSION_PRESTO=latest
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a Presto Distributed Query Engine Docker container.
echo --------------------------------------------------------------------------------
echo VERSION_PRESTO                    : %DB_SEEDER_VERSION_PRESTO%
echo "--------------------------------------------------------------------------------"
echo:| TIME
echo ================================================================================

echo --------------------------------------------------------------------------------
echo Stop and delete existing containers.
echo --------------------------------------------------------------------------------

echo Docker stop/rm db_seeder_presto ............................ before:
docker ps    | grep "db_seeder_presto" && docker stop db_seeder_presto
docker ps -a | grep "db_seeder_presto" && docker rm db_seeder_presto
echo ............................................................. after:
docker ps -a

echo Docker stop/rm db_seeder_db ................................ before:
docker ps    | grep "db_seeder_db" && docker stop db_seeder_db
docker ps -a | grep "db_seeder_db" && docker rm db_seeder_db
echo ............................................................. after:
docker ps -a

echo --------------------------------------------------------------------------------
echo Start Presto Distributed Query Engine - creating and starting the container
echo --------------------------------------------------------------------------------
lib\Gammadyne\timer.exe
echo Docker create presto (Presto Distributed Query Engine)

docker network create db_seeder_net 2>null || echo Docker network db_seeder_net already existing
docker create --name    db_seeder_presto ^
              --network db_seeder_net ^
              -p        8080:8080/tcp ^
              -v        %cd%/resources/docker/presto:/usr/lib/presto/etc ^
              prestosql/presto:%DB_SEEDER_VERSION_PRESTO%

echo Docker start presto (Presto Distributed Query Engine) ...
docker start db_seeder_presto

ping -n 30 127.0.0.1>nul

docker network ls
docker network inspect db_seeder_net

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo Docker Presto Distributed Query Engine was ready in %CONSUMED%

docker ps

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
