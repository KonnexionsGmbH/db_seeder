@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_complete.bat: Run all DBMS variations.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_DBMS_DERBY_EMB=yes
set DB_SEEDER_DBMS_H2_EMB=yes
set DB_SEEDER_DBMS_HSQLDB_EMB=yes
set DB_SEEDER_DBMS_SQLITE=yes

echo.
echo Script %0 is now running
echo.
echo You can find the run log in the file run_db_seeder_complete.log
echo.
echo Please wait ...
echo.

> run_db_seeder_complete.log 2>&1 (

    set DB_SEEDER_FILE_STATISTICS_NAME=statistics/db_seeder_local_cmd_emb.tsv

    if exist %DB_SEEDER_FILE_STATISTICS_NAME% del /f /q %DB_SEEDER_FILE_STATISTICS_NAME%
    
    echo ================================================================================
    echo Start %0
    echo --------------------------------------------------------------------------------
    echo DB Seeder - Run all DBMS variations.
    echo --------------------------------------------------------------------------------
    echo DBMS_DERBY_EMB                  : %DB_SEEDER_DBMS_DERBY_EMB%
    echo DBMS_H2_EMB                     : %DB_SEEDER_DBMS_H2_EMB%
    echo DBMS_HSQLDB_EMB                 : %DB_SEEDER_DBMS_HSQLDB_EMB%
    echo DBMS_SQLITE                     : %DB_SEEDER_DBMS_SQLITE%
    echo --------------------------------------------------------------------------------
    echo FILE_STATISTICS_NAME            : %DB_SEEDER_FILE_STATISTICS_NAME%
    echo --------------------------------------------------------------------------------
    echo:| TIME
    echo ================================================================================
    
    rem ------------------------------------------------------------------------------
    rem Apache Derby - embedded version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_DERBY_EMB%"] EQU ["yes"] (
        call run_db_seeder.bat derby_emb yes 2
        if %ERRORLEVEL% NEQ 0 (
            exit %ERRORLEVEL%
        )
    )
    
    rem ------------------------------------------------------------------------------
    rem H2 Database Engine - embedded version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_H2_EMB%"] EQU ["yes"] (
        call run_db_seeder.bat h2_emb yes 2
        if %ERRORLEVEL% NEQ 0 (
            exit %ERRORLEVEL%
        )
    )
    
    rem ------------------------------------------------------------------------------
    rem HyperSQL Database - embedded version.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_HSQLDB_EMB%"] EQU ["yes"] (
        call run_db_seeder.bat hsqldb_emb yes 2
        if %ERRORLEVEL% NEQ 0 (
            exit %ERRORLEVEL%
        )
    )
    
    rem ------------------------------------------------------------------------------
    rem SQLite.
    rem ------------------------------------------------------------------------------
    
    if ["%DB_SEEDER_DBMS_SQLITE%"] EQU ["yes"] (
        call run_db_seeder.bat sqlite yes 2
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
