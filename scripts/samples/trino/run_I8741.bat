@echo off

rem ------------------------------------------------------------------------------
rem
rem run_I8741.bat: Demonstration Issue 8741 (https://github.com/trinodb/trino/issues/8741).
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - Demonstration Issue 8741.
echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------

call gradle copyJarToLib

if exist db_seeder.log del /f /q db_seeder.log

java -cp %CLASSPATH%;lib/* ch.konnexions.db_seeder.samples.trino.I8741

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
