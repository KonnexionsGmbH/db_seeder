@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_complete.bat: Run all DBMS variations.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set ERRORLEVEL=

set DB_SEEDER_DBMS_SQLSERVER=yes
set DB_SEEDER_DBMS_MYSQL_PRESTO=yes
set DB_SEEDER_DBMS_ORACLE_PRESTO=yes
set DB_SEEDER_DBMS_POSTGRESQL=yes

echo.
echo Script %0 is now running
echo.
echo You can find the run log in the file run_db_seeder_complete.log
echo.
echo Please wait ...
echo.

> run_db_seeder_complete.log 2>&1 (

    set DB_SEEDER_FILE_STATISTICS_NAME=resources/statistics/db_seeder_local_cmd_presto.tsv

    if exist %DB_SEEDER_FILE_STATISTICS_NAME% del /f /q %DB_SEEDER_FILE_STATISTICS_NAME%
    
    echo ================================================================================
    echo Start %0
    echo --------------------------------------------------------------------------------
    echo DB Seeder - Run all DBMS variations.
    echo --------------------------------------------------------------------------------
    echo DBMS_MYSQL_PRESTO               : %DB_SEEDER_DBMS_MYSQL_PRESTO%
    echo DBMS_ORACLE_PRESTO              : %DB_SEEDER_DBMS_ORACLE_PRESTO%
    echo DBMS_POSTGRESQL                 : %DB_SEEDER_DBMS_POSTGRESQL%
    echo DBMS_SQLSERVER                  : %DB_SEEDER_DBMS_SQLSERVER%
    echo --------------------------------------------------------------------------------
    echo FILE_STATISTICS_NAME            : %DB_SEEDER_FILE_STATISTICS_NAME%
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo ================================================================================
    
    call run_db_seeder_generate_schema.bat
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
    
    call run_db_seeder_presto_environment.bat
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )

    rem ------------------------------------------------------------------------------
    rem MySQL Database - via Presto.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_MYSQL_PRESTO%"] EQU ["yes"] (
        call run_db_seeder.bat mysql_presto yes 2
        if %ERRORLEVEL% NEQ 0 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )
    
    rem ------------------------------------------------------------------------------
    rem Oracle Database - via Presto.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_ORACLE_PRESTO%"] EQU ["yes"] (
        call run_db_seeder.bat oracle_presto yes 2
        if %ERRORLEVEL% NEQ 0 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )
    
    rem ------------------------------------------------------------------------------
    rem PostgreSQL Database.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_POSTGRESQL%"] EQU ["yes"] (
        call run_db_seeder.bat postgresql yes 2
        if %ERRORLEVEL% NEQ 0 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )
    
    rem ------------------------------------------------------------------------------
    rem Microsoft SQL Server.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_SQLSERVER%"] EQU ["yes"] (
        call run_db_seeder.bat sqlserver yes 2
        if %ERRORLEVEL% NEQ 0 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )
    
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo --------------------------------------------------------------------------------
    echo End   %0
    echo ================================================================================
)
