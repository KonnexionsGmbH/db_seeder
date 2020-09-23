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

java -cp %CLASSPATH%;lib/* ch.konnexions.db_seeder.samples.presto.SampleOracle
if %ERRORLEVEL% NEQ 0 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
