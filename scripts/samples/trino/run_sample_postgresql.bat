@echo off

rem ------------------------------------------------------------------------------
rem
rem run_sample_postgresql.bat: Demonstration Issues Presto PostgreSQL Connector.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - Demonstration Issues Presto PostgreSQL Connector.
echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------

if exist db_seeder.log del /f /q db_seeder.log

java -cp %CLASSPATH%;lib/* ch.konnexions.db_seeder.samples.trino.SamplePostgresql

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
