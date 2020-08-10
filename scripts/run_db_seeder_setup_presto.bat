@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_presto.bat: Setup a Presto Distributed Query Engine Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a Presto Distributed Query Engine Docker container.
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem Presto Distributed Query Engine      https://hub.docker.com/r/prestosql/presto
rem ------------------------------------------------------------------------------

docker ps    | grep -r "db_seeder_presto" && goto CONTAINER_RUNNING
docker ps -a | grep -r "db_seeder_presto" && goto CONTAINER_START

:IMAGE_PULL

lib\Gammadyne\timer.exe
echo --------------------------------------------------------------------------------
echo Start Presto Distributed Query Engine - creating and starting the container
echo --------------------------------------------------------------------------------
echo Docker create presto (Presto Distributed Query Engine)
docker create --name db_seeder_presto -p 8080:8080/tcp konnexionsgmbh/db_seeder_presto
echo Docker start presto (Presto Distributed Query Engine) ...
docker start db_seeder_presto

ping -n 30 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo Docker Presto Distributed Query Engine was ready in %CONSUMED%

docker ps

GOTO END_OF_SCRIPT

:CONTAINER_RUNNING

echo --------------------------------------------------------------------------------
echo Start Presto Distributed Query Engine - the container is already running
echo --------------------------------------------------------------------------------
docker ps

GOTO END_OF_SCRIPT

:CONTAINER_START

echo --------------------------------------------------------------------------------
echo Start Presto Distributed Query Engine - starting the container
echo --------------------------------------------------------------------------------
docker start db_seeder_presto
docker ps

:END_OF_SCRIPT

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
