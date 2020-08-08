@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_presto.bat: Setup a Presto Distributed Query Engine Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_RELEASE=2.1.0

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a Presto Distributed Query Engine Docker container.
echo --------------------------------------------------------------------------------
echo RELEASE                   : %DB_SEEDER_RELEASE%
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
echo Docker create presto (Presto Distributed Query Engine %DB_SEEDER_RELEASE%)
docker create --name db_seeder_presto -p 8080:8080/tcp konnexionsgmbh/db_seeder_presto:%DB_SEEDER_RELEASE%
echo Docker start presto (Presto Distributed Query Engine %DB_SEEDER_RELEASE%) ...
docker start db_seeder_presto

ping -n 30 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo Docker Presto Distributed Query Engine was ready in %CONSUMED%

GOTO EXIT

:CONTAINER_RUNNING

echo --------------------------------------------------------------------------------
echo Start Presto Distributed Query Engine - the container is already running
echo --------------------------------------------------------------------------------
docker ps

GOTO EXIT

:CONTAINER_START

echo --------------------------------------------------------------------------------
echo Start Presto Distributed Query Engine - starting the container
echo --------------------------------------------------------------------------------
docker start db_seeder_presto
docker ps

:EXIT

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
