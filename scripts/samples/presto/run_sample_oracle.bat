@echo off

rem ------------------------------------------------------------------------------
rem
rem run_test_presto_mysql.bat: Demonstration Issues Presto Oracle Connector.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - Demonstration Issues Presto Oracle Connector.
echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------

dir *.log
if exist db_seeder.log del /f /q db_seeder.log
dir *.log

java -cp %CLASSPATH%;lib/* ch.konnexions.db_seeder.samples.presto.SampleOracle

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
