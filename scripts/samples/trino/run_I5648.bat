@echo off

rem ------------------------------------------------------------------------------
rem
rem run_I5648.bat: Demonstration Issue 5648 (https://github.com/trinodb/trino/issues/5648).
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - Demonstration Issue 5648.
echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------

call gradle copyJarToLib

if exist db_seeder.log del /f /q db_seeder.log

java -cp %CLASSPATH%;lib/* ch.konnexions.db_seeder.samples.trino.I5648

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
