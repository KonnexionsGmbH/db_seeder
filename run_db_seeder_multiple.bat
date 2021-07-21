@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_multiple.bat: Run multiple databases.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set ERRORLEVEL=

echo.
echo Script %0 is now running
echo.
echo You can find the run log in the file run_db_seeder_complete_client.log
echo.
echo Please wait ...
echo.

> run_db_seeder_multiple.log 2>&1 (

    echo ================================================================================
    echo Start %0
    echo --------------------------------------------------------------------------------
    echo DBSeeder - Run multiple databases.
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo ================================================================================
    
    run_db_seeder monetdb    yes 1
    run_db_seeder oracle     yes 1
    run_db_seeder postgresql yes 1
    
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo --------------------------------------------------------------------------------
    echo End   %0
    echo ================================================================================
)
