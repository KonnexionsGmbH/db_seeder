@echo off

rem ------------------------------------------------------------------------------
rem
rem run_test_presto_sqlserver.bat: Demonstration Issues Presto SQL Server Connector.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - Demonstration Issues Presto SQL Server Connector.
echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------

if exist db_seeder.log del /f /q db_seeder.log

java -cp %CLASSPATH%;lib/* ch.konnexions.db_seeder.samples.presto.SampleSqlserver

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
