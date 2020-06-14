@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_setup_ibmdb2.bat: Setup a IBM DB2 Database Docker container.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - setup a IBM DB2 Database Docker container.
echo --------------------------------------------------------------------------------
echo VERSION_IBMDB2            : %DB_SEEDER_VERSION_IBMDB2%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

rem ------------------------------------------------------------------------------
rem IBM DB2 Database                               https://hub.docker.com/_/ibmdb2
rem ------------------------------------------------------------------------------

echo IBM DB2 Database
echo --------------------------------------------------------------------------------
echo Docker create db_seeder_db (IBM DB2 Database %DB_SEEDER_VERSION_IBMDB2%)
docker run -itd --name db_seeder_db --restart unless-stopped -e DBNAME=%DB_SEEDER_IBMDB2_DATABASE% -e DB2INST1_PASSWORD=ibmdb2 -e LICENSE=accept -p 50000:50000 --privileged=true ibmcom/db2:%DB_SEEDER_VERSION_IBMDB2%

ping -n 120 127.0.0.1>nul

for /f "delims=" %%A in ('lib\Gammadyne\timer.exe /s') do set "CONSUMED=%%A"
echo DOCKER IBM DB2 Database was ready in %CONSUMED%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
