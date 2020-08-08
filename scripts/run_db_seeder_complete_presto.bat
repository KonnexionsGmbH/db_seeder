@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_complete.bat: Run all DBMS variations.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_DBMS_MSSQLSERVER=yes
set DB_SEEDER_DBMS_MYSQL_PRESTO=yes
set DB_SEEDER_DBMS_ORACLE=yes
set DB_SEEDER_DBMS_POSTGRESQL=yes

echo.
echo Script %0 is now running
echo.
echo You can find the run log in the file run_db_seeder_complete.log
echo.
echo Please wait ...
echo.

> run_db_seeder_complete.log 2>&1 (

    set DB_SEEDER_FILE_STATISTICS_NAME=statistics/db_seeder_local_cmd_presto.tsv

    if exist %DB_SEEDER_FILE_STATISTICS_NAME% del /f /q %DB_SEEDER_FILE_STATISTICS_NAME%
    
    echo ================================================================================
    echo Start %0
    echo --------------------------------------------------------------------------------
    echo DB Seeder - Run all DBMS variations.
    echo --------------------------------------------------------------------------------
    echo DBMS_MSSQLSERVER                : %DB_SEEDER_DBMS_MSSQLSERVER%
    echo DBMS_MYSQL_PRESTO               : %DB_SEEDER_DBMS_MYSQL_PRESTO%
    echo DBMS_ORACLE                     : %DB_SEEDER_DBMS_ORACLE%
    echo DBMS_POSTGRESQL                 : %DB_SEEDER_DBMS_POSTGRESQL%
    echo --------------------------------------------------------------------------------
    echo FILE_STATISTICS_NAME            : %DB_SEEDER_FILE_STATISTICS_NAME%
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo ================================================================================
    
    rem ------------------------------------------------------------------------------
    rem Microsoft SQL Server.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_MSSQLSERVER%"] EQU ["yes"] (
        call run_db_seeder.bat mssqlserver yes 2
        if %ERRORLEVEL% NEQ 0 (
            exit %ERRORLEVEL%
        )
    )
    
    rem ------------------------------------------------------------------------------
    rem MySQL Database - via Presto.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_MYSQL_PRESTO%"] EQU ["yes"] (
        call run_db_seeder.bat mysql_presto yes 2
        if %ERRORLEVEL% NEQ 0 (
            exit %ERRORLEVEL%
        )
    )
    
    rem ------------------------------------------------------------------------------
    rem Oracle Database.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_ORACLE%"] EQU ["yes"] (
        call run_db_seeder.bat oracle yes 2
        if %ERRORLEVEL% NEQ 0 (
            exit %ERRORLEVEL%
        )
    )
    
    rem ------------------------------------------------------------------------------
    rem PostgreSQL Database.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_POSTGRESQL%"] EQU ["yes"] (
        call run_db_seeder.bat postgresql yes 2
        if %ERRORLEVEL% NEQ 0 (
            exit %ERRORLEVEL%
        )
    )
    
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo --------------------------------------------------------------------------------
    echo End   %0
    echo ================================================================================
)
