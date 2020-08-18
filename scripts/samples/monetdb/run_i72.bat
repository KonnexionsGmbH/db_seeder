@echo off

rem ------------------------------------------------------------------------------
rem
rem run_i72.bat: Demonstration Issue 72.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - Demonstration Issue 72.
echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------

docker ps    | grep "db_seeder_db" && docker stop db_seeder_db
docker ps -a | grep "db_seeder_db" && docker rm db_seeder_db
docker create --name db_seeder_db -p 50000:50000/tcp monetdb/monetdb:latest
docker start db_seeder_db
docker ps

java --enable-preview -cp %CLASSPATH%;lib/* ch.konnexions.db_seeder.samples.monetdb.I72

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
