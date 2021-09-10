@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_compute_improvement.bat: Compute runtime improvement with and 
rem                                        without constraints.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_FILE_CONFIGURATION_NAME_DEFAULT=src\main\resources\db_seeder.properties
if ["%DB_SEEDER_FILE_CONFIGURATION_NAME%"] EQU [""] (
    set DB_SEEDER_FILE_CONFIGURATION_NAME=%DB_SEEDER_FILE_CONFIGURATION_NAME_DEFAULT%
)

set DB_SEEDER_FILE_IMPROVEMENT_NAME_DEFAULT=resources\statistics\db_seeder_cmd_summary_company_9.9.9_win10.tsv
if ["%DB_SEEDER_FILE_IMPROVEMENT_NAME%"] EQU [""] (
    set DB_SEEDER_FILE_IMPROVEMENT_NAME=%DB_SEEDER_FILE_IMPROVEMENT_NAME_DEFAULT%
)

set DB_SEEDER_FILE_STATISTICS_NAME_DEFAULT=resources\statistics\db_seeder_cmd_complete_company_9.9.9_win10.tsv
set DB_SEEDER_FILE_STATISTICS_NAME=%1
if ["%DB_SEEDER_FILE_STATISTICS_NAME%"] EQU [""] (
    set DB_SEEDER_FILE_STATISTICS_NAME=%DB_SEEDER_FILE_STATISTICS_NAME_DEFAULT%
)

set DB_SEEDER_JAVA_CLASSPATH=".;lib/*;JAVA_HOME/lib"

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DBSeeder - Compute runtime improvement with and without constraints.
echo --------------------------------------------------------------------------------
echo FILE_IMPROVEMENT_NAME   : %DB_SEEDER_FILE_IMPROVEMENT_NAME%
echo FILE_STATISTICS_NAME    : %DB_SEEDER_FILE_STATISTICS_NAME%
echo JAVA_CLASSPATH          : %DB_SEEDER_JAVA_CLASSPATH%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

call gradle copyJarToLib
if %ERRORLEVEL% NEQ 0 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

if exist db_seeder.log del /f /q db_seeder.log

java -cp %DB_SEEDER_JAVA_CLASSPATH% ch.konnexions.db_seeder.ComputeImprovement
if %ERRORLEVEL% NEQ 0 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
