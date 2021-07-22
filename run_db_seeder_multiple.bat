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
    
    echo setting environment variables - start ------------------------------------------
    set DB_SEEDER_FILE_JSON_NAME=resources/json/db_seeder_schema.company_108000.json
    set DB_SEEDER
    echo setting environment variables - end   ------------------------------------------
    
    call scripts\run_db_seeder_generate_schema
    
@REM     echo setting environment variables - start ------------------------------------------
@REM     set DB_SEEDER_DROP_CONSTRAINTS=no
@REM     set DB_SEEDER
@REM     echo setting environment variables - end   ------------------------------------------

rem call run_db_seeder agens      yes 1
rem call run_db_seeder cockroach  yes 1
rem call run_db_seeder derby      yes 1
rem call run_db_seeder derby_emb  yes 1
    call run_db_seeder monetdb    yes 1
    call run_db_seeder mysql      yes 1
    call run_db_seeder oracle     yes 1
    call run_db_seeder postgresql yes 1
    
@REM     echo setting environment variables - start ------------------------------------------
@REM     set DB_SEEDER_DROP_CONSTRAINTS=yes
@REM     set DB_SEEDER
@REM     echo setting environment variables - end   ------------------------------------------
@REM 
@REM rem call run_db_seeder agens      yes 1
@REM rem call run_db_seeder cockroach  yes 1
@REM rem call run_db_seeder derby      yes 1
@REM rem call run_db_seeder derby_emb  yes 1
@REM     call run_db_seeder monetdb    yes 1
@REM     call run_db_seeder mysql      yes 1
@REM     call run_db_seeder oracle     yes 1
@REM     call run_db_seeder postgresql yes 1
    
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo --------------------------------------------------------------------------------
    echo End   %0
    echo ================================================================================
)
