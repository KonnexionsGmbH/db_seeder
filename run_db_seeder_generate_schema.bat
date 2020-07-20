@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_generate_schema.bat: Generation of database schema.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_FILE_CONFIGURATION_NAME=src\main\resources\db_seeder.properties
set DB_SEEDER_FILE_JSON_NAME=
set DB_SEEDER_RELEASE=1.15.11

set DB_SEEDER_JAVA_CLASSPATH=%CLASSPATH%;lib/*

echo.
echo Script %0 is now running
echo.
echo You can find the run log in the file run_db_seeder_generate_schema.log
echo.
echo Please wait ...
echo.

> run_db_seeder_generate_schema.log 2>&1 (

    echo ================================================================================
    echo Start %0
    echo --------------------------------------------------------------------------------
    echo DB Seeder - Generation of database schema.
    echo --------------------------------------------------------------------------------
    echo FILE_CONFIGURATION_NAME : %DB_SEEDER_FILE_CONFIGURATION_NAME%
    echo FILE_JSON_NAME          : %DB_SEEDER_FILE_JSON_NAME%
    echo RELEASE                 : %DB_SEEDER_RELEASE%
    echo --------------------------------------------------------------------------------
    echo JAVA_CLASSPATH          : %DB_SEEDER_JAVA_CLASSPATH%
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo ================================================================================

    java --enable-preview -cp %DB_SEEDER_JAVA_CLASSPATH% ch.konnexions.db_seeder.SchemaGenerator %DB_SEEDER_RELEASE%
    if %ERRORLEVEL% NEQ 0 (
        exit %ERRORLEVEL%
    )
    
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo --------------------------------------------------------------------------------
    echo End   %0
    echo ================================================================================
)
