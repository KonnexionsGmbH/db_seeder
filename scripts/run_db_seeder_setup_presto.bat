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
rem Presto Distributed Query Engine
rem                                      https://hub.docker.com/r/prestosql/presto
rem ------------------------------------------------------------------------------

echo Docker stop presto ......................................... before:
docker ps    --filter "name=db_seeder_presto" | grep -q . && docker stop db_seeder_presto
set DB_SEEDER_PRESTO_RUNNING=
docker ps -a --filter "name=db_seeder_presto" | grep -q . && set DB_SEEDER_PRESTO_RUNNING=true
echo ............................................................. after:

if ["%DB_SEEDER_PRESTO_RUNNING%"] == ["true"] goto IF_ELSE
 
lib\Gammadyne\timer.exe /s
echo Start Presto Distributed Query Engine
echo --------------------------------------------------------------------------------
echo Docker create presto (Presto Distributed Query Engine %DB_SEEDER_RELEASE%)
docker create --name db_seeder_presto -p 8080%:8080/tcp konnexionsgmbh/db_seeder_presto:%DB_SEEDER_RELEASE%
echo Docker start presto (Presto Distributed Query Engine %DB_SEEDER_RELEASE%) ...
docker start db_seeder_presto

ping -n 30 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo Docker Presto Distributed Query Engine was ready in %CONSUMED%

goto IF_END

:IF_ELSE    

echo Docker Container Presto Distributed Query Engine is already existing
docker start db_seeder_presto
echo Docker Container Presto Distributed Query Engine is now running

:IF_END

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
