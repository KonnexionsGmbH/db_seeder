@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_create_summary.bat: Creation of the benchmark data summary file.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_FILE_CONFIGURATION_NAME_DEFAULT=src\main\resources\db_seeder.properties
if ["%DB_SEEDER_FILE_CONFIGURATION_NAME%"] EQU [""] (
    set DB_SEEDER_FILE_CONFIGURATION_NAME=%DB_SEEDER_FILE_CONFIGURATION_NAME_DEFAULT%
)

set DB_SEEDER_FILE_SUMMARY_NAME=resources\statistics\db_seeder_summary_3.0.3-9.9.9.tsv

set DB_SEEDER_JAVA_CLASSPATH=".;lib/*;JAVA_HOME/lib"

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - Creation of the benchmark data summary file.
echo --------------------------------------------------------------------------------
echo FILE_CONFIGURATION_NAME        : %DB_SEEDER_FILE_CONFIGURATION_NAME%
echo FILE_SUMMARY_NAME              : %DB_SEEDER_FILE_SUMMARY_NAME%
echo FILE_SUMMARY_SOURCE            : %DB_SEEDER_FILE_SUMMARY_SOURCE%
echo JAVA_CLASSPATH                 : %DB_SEEDER_JAVA_CLASSPATH%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

call gradle copyJarToLib
if ERRORLEVEL 1 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

if exist db_seeder.log del /f /q db_seeder.log

java -cp %DB_SEEDER_JAVA_CLASSPATH% ch.konnexions.db_seeder.CreateSummaryFile
if ERRORLEVEL 1 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
